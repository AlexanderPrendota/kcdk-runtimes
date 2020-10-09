![Gradle Build](https://github.com/AlexanderPrendota/kotlin-aws-lambda-custom-runtimes/workflows/Gradle%20Build/badge.svg?branch=master)
![Gradle Release](https://github.com/AlexanderPrendota/kotlin-aws-lambda-custom-runtimes/workflows/Gradle%20Release/badge.svg)

# Runtimes for Kotlin Cloud Development Kit

KCDK runtimes is a Gradle plugin and library that helps run Kotlin application in AWS cloud.

Right now, KCDK  runtimes include:
* GraalVM runtime that fully eliminates the problem of cold starts
* KotlinJS runtime _(WIP)_

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


## Use KotlinJS for lambdas

_(WIP)_

## Examples

Any explanation becomes much better with a proper example.

In the repository's [examples](https://github.com/AlexanderPrendota/kotlin-aws-lambda-custom-runtimes/tree/master/examples) folder, you can find example projects built with KCDK runtimes:

+ [`aws-hello-world`](https://github.com/AlexanderPrendota/kotlin-aws-lambda-custom-runtimes/tree/master/examples/aws-hello-world) - simple example how to use KCDK runtimes for GraalVM.
+ [`ktor-kotless-hello-world`](https://github.com/AlexanderPrendota/kotlin-aws-lambda-custom-runtimes/tree/master/examples/ktor-kotless-hello-world) - the example how to use [Kotless](https://github.com/JetBrains/kotless) framework with Ktor-DSL and build your project as native image with GraalVM. 
