package com.kotlin.aws.js.runtime.objects

import com.kotlin.aws.js.runtime.utils.getEnv
import kotlin.js.Date



data class LambdaContext(
    private val requestId: String,
    private val deadlineTime: Long?,
    private val invokedFuncArn: String?,
) {
    fun getAwsRequestId(): String = requestId
    fun getRemainingTimeInMillis(): Int? = if (deadlineTime != null) (Date().getTime() - deadlineTime).toInt() else null
    fun getInvokedFunctionArn(): String? = invokedFuncArn
    fun getLogStreamName(): String = getEnv("AWS_LAMBDA_LOG_STREAM_NAME") ?: "local"
    fun getMemoryLimitInMB(): Int = getEnv("AWS_LAMBDA_FUNCTION_MEMORY_SIZE")?.toInt() ?: 1024
    fun getLogGroupName(): String = getEnv("AWS_LAMBDA_LOG_GROUP_NAME") ?: "local"
    fun getFunctionVersion(): String = getEnv("AWS_LAMBDA_FUNCTION_VERSION") ?: "-1"
    fun getFunctionName(): String = getEnv("AWS_LAMBDA_FUNCTION_NAME") ?: ""

}
