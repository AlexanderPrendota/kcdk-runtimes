group = "org.example"
version = "1.0-SNAPSHOT"

plugins {
  kotlin("jvm") version "1.3.72"
}

repositories {
  mavenCentral()
}

dependencies {
  implementation(kotlin("stdlib-jdk8"))
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.10.3")

  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.2")
  testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.2")
}

tasks.withType<Jar> {
  manifest {
    attributes(
      mapOf(
        "Main-Class" to "com.kotlin.aws.runtime.KotlinAWSCustomRuntimeKt"
      )
    )
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
}

tasks {
  compileKotlin {
    kotlinOptions.jvmTarget = "11"
  }
  compileTestKotlin {
    kotlinOptions.jvmTarget = "11"
  }
}