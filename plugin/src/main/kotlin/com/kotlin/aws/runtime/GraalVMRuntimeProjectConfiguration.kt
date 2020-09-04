package com.kotlin.aws.runtime

import com.kotlin.aws.runtime.dsl.runtime
import com.kotlin.aws.runtime.tasks.ConfigureGraal
import com.kotlin.aws.runtime.tasks.createGraalJar
import com.kotlin.aws.runtime.tasks.createShadowJarGraal
import com.kotlin.aws.runtime.tasks.generateAdapter
import com.kotlin.aws.runtime.utils.mySourceSets
import org.gradle.api.Project
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
        //TODO disabled for now, since generated sources are ignored during compilation
        tasks.getByName("classes").dependsOn(generateAdapter())
        ConfigureGraal.apply(target, shadow)
    }
}