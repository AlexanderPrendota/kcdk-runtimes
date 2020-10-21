package com.kotlin.aws.js.runtime.objects

import com.kotlin.aws.js.runtime.LambdaEnvironment
import kotlin.js.Date

data class LambdaContext(
    private val requestId: String,
    private val deadlineTime: Long?,
    private val invokedFuncArn: String?,
) {
    fun getAwsRequestId(): String = requestId
    fun getRemainingTimeInMillis(): Int? = if (deadlineTime != null) (Date().getTime() - deadlineTime).toInt() else null
    fun getInvokedFunctionArn(): String? = invokedFuncArn
    fun getLogStreamName(): String = LambdaEnvironment.LOG_STREAM_NAME
    fun getMemoryLimitInMB(): Int = LambdaEnvironment.MEMORY_LIMIT
    fun getLogGroupName(): String = LambdaEnvironment.LOG_GROUP_NAME
    fun getFunctionVersion(): String = LambdaEnvironment.FUNCTION_VERSION
    fun getFunctionName(): String = LambdaEnvironment.FUNCTION_NAME
}
