package com.kotlin.aws.native.runtime

import com.kotlin.aws.native.runtime.client.LambdaHttpClient
import com.kotlin.aws.native.runtime.objects.LambdaContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@Suppress("unused")
actual fun launchRuntime(handler: suspend (context: LambdaContext, request: String) -> String) = runBlocking<Unit> {
    launch(Dispatchers.Default) {
        try {
            while (true) {
                val invocation = LambdaHttpClient.init()
                try {
                    LambdaHttpClient.invoke(
                        invocation.context.getAwsRequestId(),
                        handler(invocation.context, invocation.body)
                    )
                } catch (t: Throwable) {
                    LambdaHttpClient.postInvokeError(
                        invocation.context.getAwsRequestId(),
                        t.message ?: "Unknown handler error"
                    )
                }
            }
        } catch (t: Throwable) {
            LambdaHttpClient.postInitError(t.message ?: "Unknown invocation error")
        }
    }
}