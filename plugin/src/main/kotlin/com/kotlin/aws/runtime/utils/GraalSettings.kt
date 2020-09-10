package com.kotlin.aws.runtime.utils

object GraalSettings {
    const val DEFAULT_REFLECT_FILE_NAME = "reflect.json"
    const val GRAAL_VM_DOCKER_IMAGE = "oracle/graalvm-ce:20.1.0-java11"

    val BASE_GRAAL_FLAGS = listOf(
        "--enable-url-protocols=https",
        "-Djava.net.preferIPv4Stack=true",
        "--no-server",
        "-jar"
    )
    val FULL_GRAAL_VM_FLAGS = BASE_GRAAL_FLAGS + listOf(
        "-H:+AllowIncompleteClasspath",
        "-H:ReflectionConfigurationFiles=/working/build/${DEFAULT_REFLECT_FILE_NAME}",
        "-H:+ReportUnsupportedElementsAtRuntime",
        "--initialize-at-build-time=io.ktor,kotlinx,kotlin,org.apache.logging.log4j,org.apache.logging.slf4j,org.apache.log4j"
    )
}