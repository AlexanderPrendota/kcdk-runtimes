package com.kotlin.aws.runtime.client

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.kotlin.aws.runtime.LambdaRouters
import com.kotlin.aws.runtime.log
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

object LambdaHttpClient {
    private val mapper: ObjectMapper = jacksonObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    private val httpClient = HttpClient.newHttpClient()


    fun init(): HttpResponse<String> {
        val request = HttpRequest.newBuilder(URI.create(LambdaRouters.INVOKE_NEXT)).build()
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString())
    }

    fun invoke(requestId: String, bytes: ByteArray?) {
        val request = HttpRequest.newBuilder()
            .uri(URI.create(LambdaRouters.getInvocationResponse(requestId)))
            .POST(HttpRequest.BodyPublishers.ofByteArray(bytes))
            .build()
        httpClient.send(request, HttpResponse.BodyHandlers.ofString())
    }

    fun postInitError(message: String?) {
        log.severe("Init lambda failed with $message")
        postLambdaError(LambdaRouters.RUNTIME_INITIALIZE_ERROR, message)
    }

    fun postInvokeError(requestId: String, message: String?) {
        log.severe("Invocation failed for request: $requestId with message: $message")
        val url = LambdaRouters.getInvocationError(requestId)
        postLambdaError(url, message)
    }

    private fun postLambdaError(url: String, message: String?) {
        val request = HttpRequest
            .newBuilder(URI.create(url))
            .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(LambdaError(message))))
            .build()
        httpClient.send(request, HttpResponse.BodyHandlers.ofString())
    }

    private data class LambdaError(val errorMessage: String?)
}