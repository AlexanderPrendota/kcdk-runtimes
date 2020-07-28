package com.kotlin.aws.runtime

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.kotlin.aws.runtime.LambdaEnvironment.REQUEST_HEADER_NAME

val mapper: ObjectMapper = jacksonObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

fun main() {
    initRuntime()
}

internal fun initRuntime() {
    while (true) {
        initLambdaInvocation { requestId, body ->
            handleLambdaInvocation(requestId, body)
        }
    }
}


private fun initLambdaInvocation(handle: (String, String) -> Unit) {
    try {
        val (requestId, apiGatewayProxyRequest) = createLambdaInvocation()
        handle(requestId, apiGatewayProxyRequest.body)
    } catch (t: Throwable) {
        t.printStackTrace()
        LambdaHttpClient.postInitError(t.message)
    }
}

private fun createLambdaInvocation(): AwsLambdaInvocation {
    val response = LambdaHttpClient.init()
    val requestId = response.headers().firstValue(REQUEST_HEADER_NAME).orElse(null)
        ?: error("Header: $REQUEST_HEADER_NAME was not found")
    val apiGatewayProxyRequest = jacksonObjectMapper().readValue(response.body(), ApiGatewayProxyRequest::class.java)
    return AwsLambdaInvocation(requestId, apiGatewayProxyRequest)
}


private fun handleLambdaInvocation(requestId: String, body: String) {
    try {
        val result = LambdaInvocationHandler.handleInvocation(body)
        LambdaHttpClient.invoke(requestId, result)
    } catch (t: Throwable) {
        t.printStackTrace()
        LambdaHttpClient.postInvokeError(requestId, t.message)
    }
}


