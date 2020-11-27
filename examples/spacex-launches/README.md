# Kotlin/Native AWS Runtime example

This example shows how to use Kotlin/Native AWS Lambda runtime in multiplatform project.
It is based on [KMM hands-on tutorial](https://play.kotlinlang.org/hands-on/Networking%20and%20Data%20Storage%20with%20Kotlin%20Multiplatfrom%20Mobile/01_Introduction
).

Note that we use Ktor curl-based client. This means you need a Linux environment to compile 
your lambda function with `buildNativeReleaseLambda` task.
