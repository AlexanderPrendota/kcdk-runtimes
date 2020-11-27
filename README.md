![Gradle Build](https://github.com/AlexanderPrendota/kotlin-aws-lambda-custom-runtimes/workflows/Gradle%20Build/badge.svg?branch=master)
![Gradle Release](https://github.com/AlexanderPrendota/kotlin-aws-lambda-custom-runtimes/workflows/Gradle%20Release/badge.svg)

# Runtimes for Kotlin Cloud Development Kit

KCDK runtimes is a Gradle plugin and library that helps run Kotlin application in AWS cloud.

Right now, KCDK  runtimes include:
* GraalVM runtime that fully eliminates the problem of cold starts
* KotlinJS runtime

## Getting started

### Use Kotlin with GraalVM

If you already use Gradle, you only need to do two things.
First, set up the `io.kcdk` Gradle plugin. You'll need to apply the plugin:

```diff
plugins {
+  id("io.kcdk") version "0.1.1" apply true
}
```

Second, add KCDK to your application as a library:

```diff
repositories {
+  jcenter()
}

dependencies {
+  implementation("com.kotlin.aws.runtime", "runtime", "0.1.1")
}
```

This gives you access to the DSL to configure a GraalVM environment.

Set up your handler class as shown in the example below:

```kotlin
import com.kotlin.aws.runtime.dsl.runtime


runtime {
    handles = "com.aws.example.Handler::handleRequest"
}
```
You can also configure `reflect.json`, native image, etc. via DSL.

And that's the whole setup!

Now you can create your first function:

```kotlin
class Handler {
    fun handleRequest(inputStream: InputStream, outputStream: OutputStream, context: Context) {
        outputStream.write("Hello world".toByteArray())
    }
}
```

Build it via:

```shell script
$ ./gradlew buildGraalRuntime
```

Next, take your zip archive from `build/distributions` and deploy it to AWS.


### Use KotlinJS for lambdas

Now it's possible to create the AWS Lambda function from your Kotlin JS application.
There are two options available:

+ Create NodeJS Lambda
+ Create Custom Runtime Lambda

Both operations are almost transparent from the user's perspective.
The only thing you have to do here is just to write a basic Kotlin function receiving parameters related to a request, define its name in a buildscript - and that's it.
Just start `buildNodeJsRuntimeLambda` or `buildCustomRuntimeLambda` Gradle task - and your Lambda is ready for uploading to the AWS.

Let's see how it looks.

If you already use Gradle, you only need to do two things.

First, set up the `io.kcdk.js` Gradle plugin. You'll need to apply the plugin:

```diff
plugins { 
+    id("io.kcdk.js") version "0.1.2" apply true
}
```

This gives you access to the DSL to configure a lambda environment.

And that's it! There is nothing else to it. All the Kotlin JS configuration will work out of the box. (also, feel free to add JavaScript libraries from npm).

Let's create the first function:

```kotlin
fun handler(context: LambdaContext, request: String) = object {
    val statusCode = 200
    val requestId = context.getAwsRequestId()
    val requestBody = request
    val requestLength = request.length
}
```

As you can see, our handler-function receives two parameters - a context, and a request string - and returns simple Kotlin-object.

Then you must set a name of the handler (as a string `full.package.name::functionName`) in `build.gradle.kts` file like that:

```gradle
runtime {
    handler = "com.js.sample::handler"
}
```

It's also possible to specify an output directory (where your Lambda will be available) in the same block as `outputDir` parameter. By default, the `distributions` directory will be used.

### Kotlin/Native Runtime

You can also use Kotlin/Native to create AWS Lambda function.
To do so, you need to create a [Multiplatform project](https://kotlinlang.org/docs/reference/mpp-intro.html) with `linuxX64()` target.
`build.gradle.kts` will look something like this:
```kotlin
plugins {
    kotlin("multiplatform") version "1.4.20"
}

repositories {
    mavenCentral()
}

kotlin {
    linuxX64()

    sourceSets {
        val commonMain by getting {}
        val commonTest by getting {}
    }
}
```
Now, you need to add a `io.kcdk.native` plugin:
```diff
plugins {
    kotlin("multiplatform") version "1.4.20"
+    id("io.kcdk.native") version "0.1.2"
}
```
and Native runtime:
```diff
        val commonMain by getting {
+            dependencies {
+                implementation("com.kotlin.aws.runtime:runtime-native:0.1.2")
+            }
        }
```
Then add a source file with a function that will handle requests. Note that we use `suspend` modifier. 
It allows to call other asynchronous functions (e.g. make requests with [Ktor](https://ktor.io/docs/request.html#making-request)):
```kotlin
import com.kotlin.aws.native.runtime.objects.LambdaContext

suspend fun friendlyHandler(context: LambdaContext, request: String): String =
    "Hello, friend!!"
```
The only thing left is to add a `runtime` block to your `build.gradle.kts`:
```diff
runtime {
    handler = "friendlyHandler"
}
```
Now run either of two Gradle tasks:
* `buildNativeDebugLambda` -- compiles faster, contains debug info, but slower machine code.
* `buildNativeReleaseLambda` -- produces faster machine code, but longer compilation time.

It will produce an archive in `build/distributions` directory that can be uploaded to AWS.

## Examples

Any explanation becomes much better with a proper example.

In the repository's [examples](https://github.com/AlexanderPrendota/kotlin-aws-lambda-custom-runtimes/tree/master/examples) folder, you can find example projects built with KCDK runtimes:

+ [`aws-hello-world`](examples/aws-hello-world) - simple example how to use KCDK runtimes for GraalVM.
+ [`ktor-kotless-hello-world`](examples/ktor-kotless-hello-world) - the example how to use [Kotless](https://github.com/JetBrains/kotless) framework with Ktor-DSL and build your project as native image with GraalVM. 
+ [`hello-word-js-aws`](examples/js) - simple example how to use KDCK runtime for JS.
+ [`hello-native`](examples/hello-native) - trivial Native KCDK example.
+ [`spacex-launches`](examples/spacex-launches) - Native KCDK multiplatform sample.