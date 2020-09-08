package com.kotlin.aws.runtime

import com.amazonaws.services.lambda.runtime.Context
import com.kotlin.aws.runtime.client.LambdaHTTPClient
import java.io.ByteArrayOutputStream

/**
 * Example class.
 * That code will be replaced on client code function.
 * See example in: [com.kotlin.aws.runtime.tasks.GenerateAdapter]
 */
object Adapter {
    fun handleLambdaInvocation(context: Context, apiGatewayProxyRequest: String) {
        try {
            val input = apiGatewayProxyRequest.byteInputStream()
            val output = ByteArrayOutputStream()
            // here goes call
            error("Initial Adapter should never be called")
            // here goes call
            LambdaHTTPClient.invoke(context.awsRequestId, output.toByteArray())
        } catch (t: Throwable) {
            context.logger.log("Invocation error: " + t.message)
            LambdaHTTPClient.postInvokeError(context.awsRequestId, t.message)
        }
    }
}

