package com.kotlin.aws.js.runtime

import com.kotlin.aws.js.runtime.client.LambdaHTTPClient
import com.kotlin.aws.js.runtime.objects.LambdaContext
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Suppress("unused")
fun launchRuntime(handler: (context: LambdaContext, apiGatewayProxyRequest: String) -> String) {

    GlobalScope.launch() {
        try {
            while (true) {
                val invocation = LambdaHTTPClient.init()
                try {
                    LambdaHTTPClient.invoke(
                        invocation.context.getAwsRequestId(),
                        handler(invocation.context, invocation.body)
                    )
                } catch (t: Throwable) {
                    LambdaHTTPClient.postInvokeError(
                        invocation.context.getAwsRequestId(),
                        t.message ?: "Unknown handler error"
                    )
                }
            }
        } catch (t: Throwable) {
            LambdaHTTPClient.postInitError(t.message ?: "Unknown invocation error")
        }
    }

}
