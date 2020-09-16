package com.kotlin.aws.js.runtime

import com.kotlin.aws.js.runtime.client.LambdaHTTPClient
import com.kotlin.aws.js.runtime.objects.LambdaContext
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

suspend fun launchRuntime(handler: suspend (context: LambdaContext, apiGatewayProxyRequest: String) -> Unit) {

    GlobalScope.launch() {
        while (true) {
            val invocation = LambdaHTTPClient.init()
            println("got invocation")
            handler(invocation.context, invocation.body)
        }
    }

}
