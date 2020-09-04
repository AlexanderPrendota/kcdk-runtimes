package com.kotlin.aws.runtime.tasks

import com.kotlin.aws.runtime.dsl.runtime
import com.kotlin.aws.runtime.utils.Groups
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File

internal fun Project.generateAdapter() = tasks.create("generateAdapter", GenerateAdapter::class.java)

open class GenerateAdapter : DefaultTask() {
    init {
        group = Groups.graal
    }

    @get:Input
    val handler: String?
        get() = runtime.handler

    @get:OutputDirectory
    val generationPath: File?
        get() = runtime.generationPath

    @TaskAction
    fun act() {
        val (klass, function) = handler?.split("::")
            ?: error("`handler` field should be set via `runtime` extension")

        val genDir = generationPath ?: project.file("src/main/kotlin-gen")

        with(File(genDir, "com/kotlin/aws/runtime/Adapter.kt")) {
            parentFile.mkdirs()
            writeText(
                //language=kotlin
                """
                    package com.kotlin.aws.runtime
                    
                    import $klass
                    import com.amazonaws.services.lambda.runtime.Context
                    import com.kotlin.aws.runtime.client.LambdaHTTPClient
                    import java.io.ByteArrayOutputStream

                    val server = ${klass}()

                    object Adapter {
                        fun handleLambdaInvocation(context: Context, apiGatewayProxyRequest: String) {
                            try {
                                val input = apiGatewayProxyRequest.byteInputStream()
                                val output = ByteArrayOutputStream()

                                server.${function}(input, output, context)

                                LambdaHTTPClient.invoke(context.awsRequestId, output.toByteArray())
                            } catch (t: Throwable) {
                                t.printStackTrace()
                                LambdaHTTPClient.postInvokeError(context.awsRequestId, t.message)
                            }
                        }
                    }
                """.trimIndent()
            )
        }
    }
}