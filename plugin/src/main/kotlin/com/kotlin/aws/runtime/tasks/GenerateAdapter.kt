package com.kotlin.aws.runtime.tasks

import com.kotlin.aws.runtime.runtime
import com.kotlin.aws.runtime.utils.Groups
import com.kotlin.aws.runtime.utils.myKtSourceSetFiles
import com.kotlin.aws.runtime.utils.mySourceSets
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File


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
        val (klass, function) = handler?.split("::") ?: error("`handler` field should be set via `runtime` extension")

        val genDir = generationPath ?: project.file("src/main/kotlin-gen")

        with(File(genDir, "com/kotlin/aws/runtime/Adapter.kt")) {
            parentFile.mkdirs()
            writeText(
                //TODO remove ktor related parts
                //language=kotlin
                """
                    package com.kotlin.aws.runtime
                    
                    import $klass
                    import com.kotlin.aws.runtime.client.LambdaHTTPClient
                    import java.io.ByteArrayOutputStream
                    import io.ktor.server.engine.*

                    val server = ${klass}()

                    @OptIn(EngineAPI::class)
                    object Adapter {
                        fun handleLambdaInvocation(requestId: String, apiGatewayProxyRequest: String) {
                            try {
                                val input = apiGatewayProxyRequest.byteInputStream()
                                val output = ByteArrayOutputStream()

                                server.${function}(input, output, null)

                                LambdaHTTPClient.invoke(requestId, output.toByteArray())
                            } catch (t: Throwable) {
                                t.printStackTrace()
                                LambdaHTTPClient.postInvokeError(requestId, t.message)
                            }
                        }
                    }
                """.trimIndent()
            )
        }
    }
}