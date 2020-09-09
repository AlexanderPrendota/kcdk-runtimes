package com.kotlin.aws.runtime.tasks

import com.bmuschko.gradle.docker.tasks.container.DockerCreateContainer
import com.bmuschko.gradle.docker.tasks.container.DockerLogsContainer
import com.bmuschko.gradle.docker.tasks.container.DockerStartContainer
import com.bmuschko.gradle.docker.tasks.image.DockerBuildImage
import com.bmuschko.gradle.docker.tasks.image.Dockerfile
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.kotlin.aws.runtime.utils.Groups
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.provider.Property
import org.gradle.api.tasks.bundling.Zip
import shadow.org.codehaus.plexus.util.Os
import java.io.File
import java.nio.file.Files
import java.nio.file.attribute.PosixFilePermissions


internal object ConfigureGraal {
    private val reflectFileName = "reflect.json"

    private val GRAAL_VM_FLAGS = listOf(
        "--enable-url-protocols=https",
        "-Djava.net.preferIPv4Stack=true",
        "-H:+AllowIncompleteClasspath",
        "-H:ReflectionConfigurationFiles=/working/build/$reflectFileName",
        "-H:+ReportUnsupportedElementsAtRuntime",
        "--initialize-at-build-time=io.ktor,kotlinx,kotlin,org.apache.logging.log4j,org.apache.logging.slf4j,org.apache.log4j",
        "--no-server",
        "-jar"
    ).joinToString(" ")


    internal fun apply(project: Project, shadowJar: ShadowJar) {
        with(project) {
            val outputDirectory = File(buildDir, "native")
            val nativeFileName = shadowJar.archiveFile.get().asFile.nameWithoutExtension
            generateReflect(buildDir)

            val dockerfile = createDockerfile(GRAAL_VM_FLAGS, shadowJar.archiveFile.get().asFile)
            val nativeImage = createNativeImage(dockerfile)
            val nativeContainer = createNativeContainer(nativeImage)
            val logs = createLogsContainer(nativeContainer.containerId)
            val startContainer = createStartContainer(nativeContainer, logs)
            val nativeBuild = createNativeBuild(shadowJar, startContainer)

            initGraalRuntimeTask(nativeBuild, nativeFileName, outputDirectory)
        }
    }

    private fun Project.initGraalRuntimeTask(nativeBuild: Task, nativeFileName: String, outputDirectory: File) {
        tasks.create("buildGraalRuntime", Zip::class.java) {
            it.group = Groups.graal
            it.dependsOn(nativeBuild)
            it.from(outputDirectory)
            it.from(generateBootstrap(buildDir, nativeFileName))
        }
    }

    private fun Project.createDockerfile(graalVmFlags: String, file: File): Dockerfile {
        val jarFileName = file.name
        val nativeFileName = file.nameWithoutExtension
        return tasks.create("createDockerfile", Dockerfile::class.java) { dockerfile ->
            dockerfile.group = Groups.`graal setup`
            dockerfile.from("oracle/graalvm-ce:20.1.0-java11")
            dockerfile.instruction("RUN gu install native-image")
            dockerfile.instruction("RUN mkdir -p /working/build")
            dockerfile.entryPoint("bash")
            project.afterEvaluate {
                dockerfile.defaultCommand(
                    "-c",
                    """
                        ls /working/build/libs; \
                        native-image $graalVmFlags /working/build/libs/$jarFileName; \
                        mkdir -p /working/build/native; \
                        cp -f $nativeFileName /working/build/native/$nativeFileName; \
                        chmod -R 777 /working/build/native; \
                        chmod +x /working/build/native/$nativeFileName
                    """.trimIndent()
                )
            }
        }
    }


    private fun Project.createNativeImage(dockerfile: Dockerfile): DockerBuildImage {
        return tasks.create("buildGraalImage", DockerBuildImage::class.java) {
            it.group = Groups.`graal setup`
            it.dependsOn(dockerfile)
            it.images.add("kotlin/graal-native-build:latest")
        }
    }


    private fun Project.createNativeContainer(nativeImage: DockerBuildImage): DockerCreateContainer {
        return tasks.create("createGraalContainer", DockerCreateContainer::class.java) {
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
            it.hostConfig.binds.set(mapOf(buildDir to "/working/build"))
        }
    }


    private fun Project.createLogsContainer(containerId: Property<String>): DockerLogsContainer {
        return tasks.create("graalContainerLogs", DockerLogsContainer::class.java) { logsContainer ->
            logsContainer.group = Groups.`graal setup`
            logsContainer.targetContainerId(containerId)
            logsContainer.follow.set(true)
            logsContainer.tailAll.set(true)
            logsContainer.onNext {
                // Each log message from the container will be passed as it's made available
                logger.quiet(it.toString())
            }
        }
    }

    private fun Project.createStartContainer(
        nativeContainer: DockerCreateContainer,
        logs: DockerLogsContainer
    ): DockerStartContainer {
        return tasks.create("startGraalContainer", DockerStartContainer::class.java) {
            it.group = Groups.`graal setup`
            it.dependsOn(nativeContainer)
            it.targetContainerId(nativeContainer.containerId)
            it.finalizedBy(logs)
        }
    }

    private fun Project.createNativeBuild(shadowJar: ShadowJar, startContainer: DockerStartContainer): Task {
        return tasks.create("buildGraalExecutable") {
            it.group = Groups.`graal setup`
            it.dependsOn(shadowJar, startContainer)
        }
    }

    private fun generateBootstrap(buildDir: File, nativeFileName: String): File {
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

    //TODO probable reflect.json should be configurable and we should have few preconfigured
    private fun generateReflect(buildDir: File): File {
        val reflect = ConfigureGraal::class.java.getResource("/$reflectFileName").readText()
        val file = File(buildDir, reflectFileName)
        file.parentFile.mkdirs()
        file.createNewFile()
        file.writeText(reflect)
        return file
    }
}
