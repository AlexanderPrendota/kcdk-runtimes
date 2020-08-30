package com.kotlin.aws.runtime

import com.kotlin.aws.runtime.client.LambdaHTTPClient
import java.io.ByteArrayOutputStream

/**
 * Example class.
 * That code will be replaced on client code function.
 * See example in: [com.kotlin.aws.runtime.tasks.GenerateAdapter]
 */
object Adapter {
    fun handleLambdaInvocation(requestId: String, apiGatewayProxyRequest: String) {
        try {
            val input = apiGatewayProxyRequest.byteInputStream()
            val output = ByteArrayOutputStream()
            // here goes call
            error("Initial Adapter should never be called")
            // here goes call
            LambdaHTTPClient.invoke(requestId, output.toByteArray())
        } catch (t: Throwable) {
            t.printStackTrace()
            LambdaHTTPClient.postInvokeError(requestId, t.message)
        }
    }
}

