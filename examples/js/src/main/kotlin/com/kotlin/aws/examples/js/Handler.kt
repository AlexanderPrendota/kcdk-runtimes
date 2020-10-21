package com.kotlin.aws.examples.js

import com.kotlin.aws.js.runtime.objects.LambdaContext

@Suppress("unused")
fun handler(context: LambdaContext, request: String) = object {
    val statusCode = 200
    val requestId = context.getAwsRequestId()
    val requestBody = request
    val requestLength = request.length
    val requestIsNumber = isNumber(request)
}