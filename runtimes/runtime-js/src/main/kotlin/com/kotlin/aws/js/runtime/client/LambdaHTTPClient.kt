package com.kotlin.aws.js.runtime.client

import com.kotlin.aws.js.runtime.utils.fetch
import com.kotlin.aws.js.runtime.objects.LambdaContext
import com.kotlin.aws.js.runtime.utils.getEnv
import org.w3c.fetch.RequestInit
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

private external val process: Process

private external interface Process {
    val env: dynamic
}

object LambdaHTTPClient {

    //private val runtimeApiEndpoint = "http://${getEnv("AWS_LAMBDA_RUNTIME_API")}"
    private val runtimeApiEndpoint = "http://localhost:8080"

    suspend fun init(): FetchResponse = sendGet("$runtimeApiEndpoint/2018-06-01/runtime/invocation/next")

    suspend fun invoke(requestId: String, response: String, type: MIME = MIME.APPLICATION_JSON) {
        sendPost(
            "$runtimeApiEndpoint/2018-06-01/runtime/invocation/$requestId/response",
            response,
            type
        )
    }

    suspend fun postInvokeError(requestId: String, message: String) {
        sendPost(
            "$runtimeApiEndpoint/2018-06-01/runtime/invocation/$requestId/error",
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
                            headers.get("lambda-runtime-aws-request-id")!!,
                            headers.get("lambda-runtime-deadline-ms")?.toLong() ?: 0,
                            headers.get("lambda-runtime-invoked-function-arn")
                        ),
                        //Json.decodeFromString(bodyString)
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