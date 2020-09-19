package com.js.sample

import com.kotlin.aws.js.runtime.objects.LambdaContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Suppress("unused")
class Handler {
    fun handler(context: LambdaContext, request: String) = Json.encodeToString(LambdaResponse.serializer(), LambdaResponse(request))
}

@Serializable
data class LambdaResponse(
    val requestContent: String
)