package com.js.sample

import com.kotlin.aws.js.runtime.client.LambdaHTTPClient
import com.kotlin.aws.js.runtime.objects.LambdaContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class Handler {
    suspend fun handler(context: LambdaContext, request: String) {
        LambdaHTTPClient.invoke(
            context.getAwsRequestId(),
            Json.encodeToString(LambdaResponse.serializer(), LambdaResponse(request))
        )
    }
}

@Serializable
data class LambdaResponse(
    val requestContent: String
)