package com.kotlin.aws.runtime.handler

import com.kotlin.aws.runtime.LambdaEnvironment.HANDLER_CLASS
import com.kotlin.aws.runtime.LambdaEnvironment.LAMBDA_TASK_ROOT
import java.io.File
import java.lang.reflect.Method
import java.net.MalformedURLException
import java.net.URL
import java.net.URLClassLoader

object LambdaInvocationHandler {

    private const val JAR_EXTENSION = ".jar"
    private const val LIB_FOLDER_NAME = "lib"

    /**
     * Get the handler class and method name from the Lambda Configuration in the format of <class>::<method>
     * Find the Handler and Method on the classpath.
     */
    @Throws(java.lang.Exception::class)
    fun handleInvocation(body: String): String = handleInvocation(LAMBDA_TASK_ROOT, HANDLER_CLASS, body)

    @Throws(java.lang.Exception::class)
    internal fun handleInvocation(root: String, handler: String, arguments: String): String {
        val handlerParts = handler.split("::")
        if (handlerParts.size != 2)
            error("Specify class and method for invocation. Example: `Class::method`. Current: $handler")
        val handlerClass = getHandlerClass(root, handlerParts[0])
            ?: error("Handler class not found. Full class: $handler, ROOT_TASK: $root")
        val handlerMethod = getHandlerMethod(handlerClass, handlerParts[1])
            ?: error("Handler method not found. Full class: $handler")
        return invokeClasses(handlerClass, handlerMethod, arguments)
    }

    @Throws(java.lang.Exception::class)
    private fun invokeClasses(
        handlerClass: Class<*>,
        handlerMethod: Method,
        payload: String
    ): String {
        val myClassObj = handlerClass.getConstructor().newInstance()
        val args = arrayOf(payload)
        return handlerMethod.invoke(myClassObj, *args) as String
    }

    @Throws(java.lang.Exception::class)
    private fun getHandlerClass(taskRoot: String, className: String): Class<*>? {
        val classPathUrls = initClasspath(taskRoot)
        val cl = URLClassLoader.newInstance(classPathUrls.toTypedArray())
        return cl.loadClass(className)
    }

    @Throws(java.lang.Exception::class)
    private fun getHandlerMethod(handlerClass: Class<*>, methodName: String): Method? =
        handlerClass.methods.find { it.name == methodName }

    @Throws(MalformedURLException::class)
    private fun initClasspath(taskRoot: String): List<URL> {
        val cwd = File(taskRoot)
        val classPath = mutableListOf(File(taskRoot)) // Add top level folders
        // Find any Top level jars or jars in the lib folder
        val rootFiles =
            cwd.listFiles { _: File?, name: String -> name.endsWith(JAR_EXTENSION) || name == LIB_FOLDER_NAME }
                ?: error("No files found from $taskRoot directory")
        for (file in rootFiles) {
            if (file.name == LIB_FOLDER_NAME && file.isDirectory) {
                // Collect all Jars in /lib directory
                val libFolderFiles =
                    file.listFiles { _: File?, name: String -> name.endsWith(JAR_EXTENSION) } ?: continue
                for (jar in libFolderFiles) {
                    classPath.add(jar)
                }
            } else {
                // Add top level dirs and jar files
                classPath.add(file)
            }
        }
        // Convert Files to URLs
        return classPath.mapNotNull { it.toURI().toURL() }
    }

}
