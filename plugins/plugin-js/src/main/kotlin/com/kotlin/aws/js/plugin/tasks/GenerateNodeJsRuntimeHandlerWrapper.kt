package com.kotlin.aws.js.plugin.tasks

import com.kotlin.aws.js.plugin.dsl.RuntimePluginExtension
import com.kotlin.aws.js.plugin.utils.Groups
import com.kotlin.aws.js.plugin.utils.runtime
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import java.io.File

open class GenerateNodeJsRuntimeHandlerWrapper : DefaultTask() {

    init {
        group = Groups.`js setup`
        description = "Generate a wrapper-function for calling your Lambda"
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
        val (klass, function) = extension.getClassAndFunction()
        with(File(defaultPath, "com/kotlin/aws/js/plugin/Main.kt")) {
            parentFile.mkdirs()
            writeText(
                    // language=kotlin
                    """
                        import com.kotlin.aws.js.runtime.objects.LambdaContext
                        import kotlin.js.Promise
                        
                        @JsExport
                        fun $function(request: dynamic, context: dynamic): Promise<Any> {
                            val requestId = context.awsRequestId as String
                            val arn = context.invokedFunctionArn as String
                            val context = LambdaContext(requestId, null, arn)
                            val result: Any = $klass.$function(context, JSON.stringify(request))
                            return Promise.resolve(result)
                        }
                        
                    """.trimIndent()
            )
        }
    }

}