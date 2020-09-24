import com.kotlin.aws.runtime.dsl.runtime

plugins {
  id("com.kotlin.aws.runtime") version "0.1.0" apply true
}

group = rootProject.group
version = rootProject.version

dependencies {
  implementation("com.kotlin.aws.runtime", "runtime", "0.1.0")
}

runtime {
  handler = "com.aws.example.Handler::handleRequest"
}