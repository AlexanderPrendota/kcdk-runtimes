package com.kotlin.aws.native.runtime.client

import com.kotlin.aws.native.runtime.LambdaEnvironment
import com.kotlin.aws.native.runtime.LambdaRouters
import com.kotlin.aws.native.runtime.objects.LambdaContext
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

internal object LambdaHttpClient {

    private val client = HttpClient()

    suspend fun init(): FetchResponse = sendGet(LambdaRouters.INVOKE_NEXT)

    suspend fun invoke(requestId: String, response: String, type: ContentType = ContentType.Application.Json) {
        sendPost(LambdaRouters.getInvocationResponse(requestId), response, type)
    }

    suspend fun postInvokeError(requestId: String, message: String) {
        sendPost(LambdaRouters.getInvocationError(requestId), message, ContentType.Text.Plain)
    }

    suspend fun postInitError(message: String) {
        sendPost(LambdaRouters.RUNTIME_INITIALIZE_ERROR, message, ContentType.Text.Plain)
    }

    private suspend inline fun sendGet(url: String): FetchResponse =
        client.get<HttpResponse>(url).let { response ->
            FetchResponse(
                LambdaContext(
                    response.headers[LambdaEnvironment.REQUEST_HEADER_NAME]!!,
                    response.headers[LambdaEnvironment.DEADLINE_HEADER_NAME]?.toLong() ?: 0,
                    response.headers[LambdaEnvironment.INVOKED_FUNCTION_ARN]
                ),
                response.readText()
            )
        }

    private suspend fun sendPost(url: String, body: String, type: ContentType): String =
        client.post(url) {
            contentType(type)
            this.body = body
        }
}

internal data class FetchResponse(
    val context: LambdaContext,
    val body: String
)