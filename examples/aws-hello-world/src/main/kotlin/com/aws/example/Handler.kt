package com.aws.example

import com.amazonaws.services.lambda.runtime.Context
import com.kotlin.aws.runtime.objects.LambdaContext
import java.io.InputStream
import java.io.OutputStream

class Handler {
    fun handleRequest(inputStream: InputStream, outputStream: OutputStream, context: Context): String {
        return "Hello from graal lambda!"
    }
}