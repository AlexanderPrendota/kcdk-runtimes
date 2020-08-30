package com.kotlin.aws.runtime.tasks

import com.bmuschko.gradle.docker.tasks.container.DockerCreateContainer
import com.bmuschko.gradle.docker.tasks.container.DockerLogsContainer
import com.bmuschko.gradle.docker.tasks.container.DockerStartContainer
import com.bmuschko.gradle.docker.tasks.image.DockerBuildImage
import com.bmuschko.gradle.docker.tasks.image.Dockerfile
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.kotlin.aws.runtime.utils.Groups
import org.gradle.api.Project
import org.gradle.api.tasks.bundling.Zip
import shadow.org.codehaus.plexus.util.Os
import java.io.File
import java.nio.file.Files
import java.nio.file.attribute.PosixFilePermissions

object ConfigureGraal {
    //FIXME docker tend to create folders with sudo that are not deletable under original user
    fun apply(project: Project, shadowJar: ShadowJar) {
        with(project) {

            //TODO definitely should split it to separate functions and make a bit more configurable
            val outputDirectory = File(buildDir, "native")
            val jarFileName = shadowJar.archiveFile.get().asFile.name
            val nativeFileName = shadowJar.archiveFile.get().asFile.nameWithoutExtension

            println(jarFileName)

            val graalVmFlags = listOf(
                "--enable-url-protocols=https",
                "-Djava.net.preferIPv4Stack=true",
                "-H:+AllowIncompleteClasspath",
                "-H:ReflectionConfigurationFiles=/working/build/reflect.json",
                "-H:+ReportUnsupportedElementsAtRuntime",
                "--initialize-at-build-time=io.ktor,kotlinx,kotlin,org.apache.logging.log4j,org.apache.logging.slf4j,org.apache.log4j",
                "--no-server",
                "-jar"
            ).joinToString(" ")

            generateReflect(buildDir)

            val dockerfile = tasks.create("createDockerfile", Dockerfile::class.java) {
                it.group = Groups.`graal setup`

                it.from("oracle/graalvm-ce:20.1.0-java11")
                it.instruction("RUN gu install native-image")
                it.instruction("RUN mkdir -p /working/build")
                it.entryPoint("bash")
                it.defaultCommand(
                    "-c",
                    """
                        ls /working/build/libs; \
                        native-image $graalVmFlags /working/build/libs/$jarFileName; \
                        mkdir -p /working/native; \
                        cp -f $nativeFileName /working/build/native/$nativeFileName;
                    """.trimIndent()
                )
            }

            val nativeImage = tasks.create("buildGraalImage", DockerBuildImage::class.java) {
                it.group = Groups.`graal setup`

                it.dependsOn(dockerfile)
                it.images.add("kotlin/graal-native-build:latest")
            }

            val nativeContainer =
                tasks.create("createGraalContainer", DockerCreateContainer::class.java) {
                    it.group = Groups.`graal setup`

                    val buildDir = when {
                        Os.isFamily(Os.FAMILY_WINDOWS) -> buildDir.absolutePath
                            .replace('\\', '/')
                            .replace("C:", "//c", ignoreCase = true)
                        else -> buildDir.absolutePath
                    }

                    println("Build directory: $buildDir")
                    it.dependsOn(nativeImage)
                    it.targetImageId(nativeImage.imageId)
                    it.hostConfig.autoRemove.set(true)
                    it.hostConfig.binds.set(
                        mapOf(
                            buildDir to "/working/build"
                        )
                    )
                }

            val logs = tasks.create("graalContainerLogs", DockerLogsContainer::class.java) {
                it.group = Groups.`graal setup`

                it.targetContainerId(nativeContainer.containerId)
                it.follow.set(true)
                it.tailAll.set(true)
                it.onNext {
                    // Each log message from the container will be passed as it's made available
                    logger.quiet(it.toString())
                }
            }

            val startContainer = tasks.create(
                "startGraalContainer",
                DockerStartContainer::class.java
            ) {
                it.group = Groups.`graal setup`
                it.dependsOn(nativeContainer)
                it.targetContainerId(nativeContainer.containerId)
                it.finalizedBy(logs)
            }

            val nativeBuild = tasks.create("buildGraalExecutable") {
                it.group = Groups.`graal setup`
                it.dependsOn(shadowJar, startContainer)
            }

            fun generateBootstrap(): File {
                val file = File(buildDir, "bootstrap")
                file.delete()
                val posix = PosixFilePermissions.fromString("rwxr-xr-x")
                Files.createFile(file.toPath(), PosixFilePermissions.asFileAttribute(posix))

                file.parentFile.mkdirs()
                file.createNewFile()
                //language=sh
                file.writeText(
                    """
                        #!/bin/sh
                        set -euo pipefail
                        ./${nativeFileName}
                    """.trimIndent()
                )
                return file
            }

            val buildRuntime = tasks.create("buildGraalRuntime", Zip::class.java) {
                it.group = Groups.graal
                it.dependsOn(nativeBuild)
                it.from(outputDirectory)
                it.from(generateBootstrap())
            }
        }
    }


    //TODO probable reflect.json should be configurable and we should have few preconfigured
    private fun generateReflect(buildDir: File): File {
        val reflect = ConfigureGraal::class.java.getResource("/reflect.json").readText()

        val file = File(buildDir, "reflect.json")
        file.parentFile.mkdirs()
        file.createNewFile()
        file.writeText(reflect)
        return file
    }
}