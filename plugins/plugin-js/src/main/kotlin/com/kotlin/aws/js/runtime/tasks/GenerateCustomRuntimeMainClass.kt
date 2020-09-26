package com.kotlin.aws.js.runtime.tasks

import com.kotlin.aws.js.runtime.dsl.RuntimePluginExtension
import com.kotlin.aws.js.runtime.utils.Groups
import com.kotlin.aws.js.runtime.utils.runtime
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import java.io.File

open class GenerateCustomRuntimeMainClass : DefaultTask() {

    init {
        group = Groups.`aws setup`
    }

    @get:Input
    val extension: RuntimePluginExtension = project.runtime

    private val defaultPath: File by lazy {
        val default = File(project.buildDir, "kotlin-gen")
        default.mkdirs()
        default
    }

    @TaskAction
    fun generate() {
        val (_package, function) = extension.getClassAndFunction()
        with(File(defaultPath, "com/kotlin/aws/js/runtime/Main.kt")) {
            parentFile.mkdirs()
            writeText(
                    // language=kotlin
                    """
                        package com.kotlin.aws.js.runtime
                        
                        import com.kotlin.aws.js.runtime.objects.LambdaContext
                        
                        fun main() {
                            launchRuntime { context: LambdaContext, request: String ->
                                JSON.stringify($_package.$function(context, request))
                            }
                        }
                        
                    """.trimIndent()
            )
        }

    }
}