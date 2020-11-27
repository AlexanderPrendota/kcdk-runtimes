package com.kotlin.aws.native.plugin.tasks

import com.kotlin.aws.native.plugin.AWS_TASK_GROUP
import com.kotlin.aws.native.plugin.utils.runtime
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.File

open class GenerateNativeRuntimeHandlerWrapper : DefaultTask() {
    init {
        group = AWS_TASK_GROUP
        description = "Generate a wrapper function for calling your Lambda"
    }

    @get:Input
    val handler: String
        get() = project.runtime.handler

    @OutputFile
    val generatedFile: File =
        File("${project.buildDir}/kotlin-gen/com/kotlin/aws/native/plugin").let {
            it.mkdirs()
            File(it, "generated_handler_wrapper.kt")
        }

    @TaskAction
    fun generate() {
        with(generatedFile) {
            writeText(
                // language=kotlin
                """
                    fun launchLambdaRuntime(args: Array<String>) {
                        com.kotlin.aws.native.runtime.launchRuntime(::$handler)
                    }
                """.trimIndent()
            )
        }
    }
}