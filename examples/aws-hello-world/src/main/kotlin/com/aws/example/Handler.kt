package com.aws.example

import com.amazonaws.services.lambda.runtime.Context
import java.io.InputStream
import java.io.OutputStream

class Handler {
    fun handleRequest(inputStream: InputStream, outputStream: OutputStream, context: Context) {
        outputStream.write("Hello world".toByteArray())
    }
}