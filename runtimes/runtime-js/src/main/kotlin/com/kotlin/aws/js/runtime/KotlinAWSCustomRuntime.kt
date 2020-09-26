package com.kotlin.aws.js.runtime

import com.kotlin.aws.js.runtime.client.LambdaHTTPClient
import com.kotlin.aws.js.runtime.objects.LambdaContext
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Suppress("unused")
fun launchRuntime(handler: (context: LambdaContext, apiGatewayProxyRequest: String) -> String) {

    GlobalScope.launch() {
        while (true) {
            val invocation = LambdaHTTPClient.init()
            try {
                LambdaHTTPClient.invoke(
                    invocation.context.getAwsRequestId(),
                    handler(invocation.context, invocation.body)
                )
            } catch (e: Exception) {
                LambdaHTTPClient.postInvokeError(
                    invocation.context.getAwsRequestId(),
                    e.message ?: "Unknown handler error"
                )
            }
        }
    }

}
