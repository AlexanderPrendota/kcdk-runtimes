package com.kotlin.aws.runtime.graal

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.kotlin.aws.runtime.dsl.runtime
import com.kotlin.aws.runtime.graal.tasks.ConfigureGraal
import com.kotlin.aws.runtime.graal.tasks.GenerateAdapter
import com.kotlin.aws.runtime.utils.Groups
import com.kotlin.aws.runtime.utils.mySourceSets
import org.gradle.api.Project
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.get


internal fun configureGraalVM(target: Project) {
    with(target) {
        pluginManager.apply("com.github.johnrengelman.shadow")
        pluginManager.apply("com.bmuschko.docker-remote-api")
        val jar = createGraalJar()
        val shadow = createShadowJarGraal(jar)
        afterEvaluate {
            //FIXME
            // for some reason if we add generated sources via that command
            // sources are ignored during compilation
            target.mySourceSets.apply {
                this["main"].java.srcDir(runtime.generationPath!!)
            }
        }
        generateAdapter()

        //TODO disabled for now, since generated sources are ignored during compilation
        // target.tasks["classes"].dependsOn(generateAdapter)
        ConfigureGraal.apply(target, shadow)
    }
}

private fun Project.generateAdapter() = tasks.create("generateAdapter", GenerateAdapter::class.java)

private fun Project.createGraalJar(): Jar {
    return tasks.create("graalJar", Jar::class.java) {
        it.group = Groups.build
        it.manifest { manifest ->
            manifest.attributes(mapOf("Main-Class" to "com.kotlin.aws.runtime.KotlinAWSCustomRuntimeKt"))
        }
    }
}

private fun Project.createShadowJarGraal(jar: Jar): ShadowJar {
    return tasks.create("shadowJarGraal", ShadowJar::class.java) {
        it.group = Groups.shadow
        it.archiveClassifier.set("graal")
        it.archiveVersion.set("")
        it.from(mySourceSets["main"].output)
        it.configurations.add(configurations["compileClasspath"])
        it.configurations.add(configurations["runtimeClasspath"])
        it.manifest.inheritFrom(jar.manifest)
    }
}