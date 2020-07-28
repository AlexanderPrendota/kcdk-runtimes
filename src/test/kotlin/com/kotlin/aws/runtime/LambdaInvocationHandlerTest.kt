package com.kotlin.aws.runtime

import com.kotlin.aws.runtime.dumps.DumpClass
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
            body = "Runtime"
        )
        Assertions.assertEquals("Hello, Runtime", result)
    }

    @Test
    fun `no class found test`() {
        val exception = assertThrows<ClassNotFoundException> {
            LambdaInvocationHandler.handleInvocation(
                root = resourceFolder.path,
                handler = "com.kotlin.aws.runtime.NoClass::hadler",
                body = "test"
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
                body = "test"
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
            LambdaInvocationHandler.handleInvocation(root = "root", handler = "test:tests", body = "test")
        }
        Assertions.assertTrue(
            exception.message?.contains("Specify class and method for invocation.") == true,
            "Parsing exception. Message: ${exception.message}"
        )
    }
}