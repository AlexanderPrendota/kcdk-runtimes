package com.kotlin.aws.js.runtime.client

import com.kotlin.aws.js.runtime.LambdaEnvironment
import com.kotlin.aws.js.runtime.LambdaRouters
import com.kotlin.aws.js.runtime.utils.fetch
import com.kotlin.aws.js.runtime.objects.LambdaContext
import org.w3c.fetch.RequestInit
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

internal object LambdaHTTPClient {

    suspend fun init(): FetchResponse = sendGet("${LambdaRouters.RUNTIME_BASE_URL}/invocation/next")

    suspend fun invoke(requestId: String, response: String, type: MIME = MIME.APPLICATION_JSON) {
        sendPost(
            "${LambdaRouters.RUNTIME_BASE_URL}/invocation/$requestId/response",
            response,
            type
        )
    }

    suspend fun postInvokeError(requestId: String, message: String) {
        sendPost(
            "${LambdaRouters.RUNTIME_BASE_URL}/invocation/$requestId/error",
            message,
            MIME.TEXT_PLAIN
        )
    }

    private suspend inline fun sendGet(url: String): FetchResponse {
        return suspendCoroutine { continuation ->
            fetch(url).then {
                val headers = it.headers
                it.text().then { bodyString ->
                    continuation.resume(FetchResponse(
                        LambdaContext(
                            headers.get(LambdaEnvironment.REQUEST_HEADER_NAME)!!,
                            headers.get(LambdaEnvironment.DEADLINE_HEADER_NAME)?.toLong() ?: 0,
                            headers.get(LambdaEnvironment.INVOKED_FUNCTION_ARN)
                        ),
                        bodyString
                    ))
                }
            }
        }
    }

    private suspend fun sendPost(url: String, body: String, type: MIME): String {
        val request = RequestInit(
            method = "POST",
            headers = mapOf("Content-Type" to type.value),
            body = body
        )
        return suspendCoroutine { continuation ->
            fetch(
                url,
                request
            ).then { it.text() }
                .then { continuation.resume(it) }
        }
    }

}

data class FetchResponse (
    val context: LambdaContext,
    val body: String
)

enum class MIME(val value: String) {
    APPLICATION_JSON("application/json"),
    TEXT_PLAIN("text/plain"),
}