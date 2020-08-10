package com.kotlin.aws.runtime

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.kotlin.aws.runtime.dumps.DumpClass
import com.kotlin.aws.runtime.dumps.NoArgsClass
import com.kotlin.aws.runtime.handler.LambdaInvocationHandler
import com.kotlin.aws.runtime.objects.ApiGatewayProxyRequest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class LambdaInvocationHandlerTest {

    private val resourceFolder =
        DumpClass::class.java.classLoader.getResource("") ?: error("No resource found in LambdaInvocationHandlerTest")

    @Test
    fun `handle test`() {
        val result = LambdaInvocationHandler.handleInvocation(
            root = resourceFolder.path,
            handler = "com.kotlin.aws.runtime.dumps.DumpClass::handle",
            apiGatewayProxyRequest = ApiGatewayProxyRequest(body = "body")
        )
        Assertions.assertEquals("Hello!", result.toStringFromBytes())
    }

    @Test
    fun `handle w o arguments`() {
        val res = NoArgsClass::class.java.classLoader.getResource("") ?: error("No NoArgsClass found")
        val result = LambdaInvocationHandler.handleInvocation(
            root = res.path,
            handler = "com.kotlin.aws.runtime.dumps.NoArgsClass::handle",
            apiGatewayProxyRequest = ApiGatewayProxyRequest()
        )
        Assertions.assertEquals("Hello, from NoArgsClass!", result.toStringFromBytes())
    }

    @Test
    fun `no class found test`() {
        val exception = assertThrows<ClassNotFoundException> {
            LambdaInvocationHandler.handleInvocation(
                root = resourceFolder.path,
                handler = "com.kotlin.aws.runtime.NoClass::hadler",
                apiGatewayProxyRequest = ApiGatewayProxyRequest()
            )
        }
        Assertions.assertTrue(
            exception.message?.contains("com.kotlin.aws.runtime.NoClass") == true,
            "No class exception. Message: ${exception.message}"
        )

    }

    @Test
    fun `no method found test`() {
        val wrongMethod = "handler"
        val exception = assertThrows<IllegalStateException> {
            LambdaInvocationHandler.handleInvocation(
                root = resourceFolder.path,
                handler = "com.kotlin.aws.runtime.dumps.DumpClass::$wrongMethod",
                apiGatewayProxyRequest = ApiGatewayProxyRequest()
            )
        }
        Assertions.assertTrue(
            exception.message?.contains("Handler method not found.") == true,
            "No method exception. Message: ${exception.message}"
        )

    }

    @Test
    fun `read class & method test`() {
        val exception = assertThrows<IllegalStateException> {
            LambdaInvocationHandler.handleInvocation(
                root = "root",
                handler = "test:tests",
                apiGatewayProxyRequest = ApiGatewayProxyRequest()
            )
        }
        Assertions.assertTrue(
            exception.message?.contains("Specify class and method for invocation.") == true,
            "Parsing exception. Message: ${exception.message}"
        )
    }

    private fun ByteArray.toStringFromBytes(): String = jacksonObjectMapper().readValue(this, String::class.java)
}