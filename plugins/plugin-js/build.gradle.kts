plugins {
    kotlin("jvm") version "1.4.0" //apply true
    id("java-gradle-plugin")
    `maven-publish`
}

group = "com.kotlin.aws.js"
version = "0.0.1"

repositories {
    mavenLocal()
    jcenter()
    mavenCentral()
}

dependencies {
    api(kotlin("stdlib"))
    //implementation(kotlin("stdlib-jdk8"))

    implementation(kotlin("stdlib-js"))
    implementation(gradleApi())
    implementation(gradleKotlinDsl())
    implementation(kotlin("gradle-plugin-api"))

    api("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.0")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

gradlePlugin {
    plugins {
        create("kotlinjs-lambda") {
            id = "com.kotlin.aws.js.js-plugin"
            version = "0.0.1"
            implementationClass = "com.kotlin.aws.js.runtime.RuntimeKotlinGradlePlugin"
            displayName = "Fucking plugin"
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("default") {
            from(components["kotlin"])
        }
    }
    repositories {
        mavenLocal()
    }
}
