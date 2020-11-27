package com.kotlin.aws.native.plugin

import com.kotlin.aws.native.plugin.tasks.GenerateNativeRuntimeHandlerWrapper
import com.kotlin.aws.native.plugin.utils.createTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.bundling.Zip
import org.gradle.kotlin.dsl.create
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeCompilation
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetWithHostTests
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType
import java.io.File

const val AWS_TASK_GROUP = "AWS Lambda Runtime"

class RuntimeKotlinGradlePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.pluginManager.withPlugin("kotlin-multiplatform") {
            val mppProjectExtension = target.extensions.getByName("kotlin") as KotlinMultiplatformExtension
            mppProjectExtension.linuxX64 {
                val main = compilations.getByName("main")
                val wrapperCompilation = createWrapperCompilation(main)
                binaries.executable {
                    entryPoint = "launchLambdaRuntime"
                    compilation = wrapperCompilation
                    // `/lib64` on Amazon Linux 2 contains libcurl.so.4 which is used by Ktor.
                    linkerOpts.addAll(listOf("-L", "/lib64"))
                }
                createBootstrapZipTask(NativeBuildType.DEBUG)
                createBootstrapZipTask(NativeBuildType.RELEASE)
            }
        }
    }

    private fun KotlinNativeTargetWithHostTests.createWrapperCompilation(mainCompilation: KotlinNativeCompilation): KotlinNativeCompilation {
        val wrapper = compilations.create("wrapper")
        val wrapperGenerationTask = project.createTask(GenerateNativeRuntimeHandlerWrapper::class.java)
        wrapper.compileKotlinTask.dependsOn(wrapperGenerationTask)
        wrapper.defaultSourceSet.kotlin.setSrcDirs(listOf(wrapperGenerationTask.generatedFile.parentFile))
        wrapper.associateWith(mainCompilation)
        return wrapper
    }

    private fun KotlinNativeTargetWithHostTests.createBootstrapZipTask(buildType: NativeBuildType) {
        val executable = binaries.getExecutable(buildType)
        val buildTypeName = buildType.name.toLowerCase()
        val directory = File("${project.buildDir}/distributions").also { it.mkdir() }
        val archiveName = "aws-lambda-$buildTypeName"
        val taskName = "buildNative${buildTypeName.capitalize()}Lambda"
        project.tasks.create<Zip>(taskName) {
            description = "Create archive with $buildTypeName executable that can be uploaded to AWS"
            group = AWS_TASK_GROUP
            // We cannot create archive without appropriate executable.
            dependsOn(executable.linkTask)
            destinationDirectory.set(directory)
            archiveBaseName.set(archiveName)
            from(executable.outputFile.absolutePath)
            rename {
                when (it) {
                    // AWS expects executable to be named as "bootstrap".
                    executable.outputFile.name -> "bootstrap"
                    else -> it
                }
            }
        }
    }
}