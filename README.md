# Kotlin AWS Runtimes

Kotlin AWS runtimes is a Gradle plugin and library that helps run Kotlin application in AWS cloud.

Right now, Kotlin AWS Runtimes include:
* GraalVM runtime that fully eliminates the problem of cold starts


### Kotlin JS

Now it's possible to create the AWS Lambda function from your Kotlin JS application.
There are two options available:

+ Create NodeJS Lambda
+ Create Custom Runtime Lambda

Both operations are almost transparent from the user's perpspective.
The only thing you have to do here is just to write a basic Kotlin function receiving parameters related to a request, define its name in a buildscript - and that's it.
Just start `buildNodeJsRuntimeLambda` or `buildCustomRuntimeLambda` Gradle task - and your Lambda is ready for uploading to the AWS.

Take a look on some example.

Let's say, we want deploy such Kotlin function as the AWS Lambda function:

```kotlin
fun handler(context: LambdaContext, request: String) = object {
    val statusCode = 200
    val requestId = context.getAwsRequestId()
    val requestBody = request
    val requestLength = request.length
}
```

As you can see, our handler-function receives two parameters - a context and a request string - and returns simple Kotlin-object.

Then you must set a name of the handler (as a string `full.package.name::functionName`) in `build.gradle.kts` file like that:

```gradle
runtime {
    handler = "com.js.sample::handler"
}
```

It's also possible to specify an output directory (where your Lambda will be available) in the same block as `outputDir` parameter. By default, the `distributions` directory will be used.