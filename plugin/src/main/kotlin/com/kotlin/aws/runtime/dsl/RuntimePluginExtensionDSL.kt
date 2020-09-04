package com.kotlin.aws.runtime.dsl

import java.io.File
import java.io.Serializable

@DslMarker
annotation class RuntimeDSLTag

class RuntimePluginExtension : Serializable {
    var handler: String? = null
    var generationPath: File? = null

    internal val config: RuntimeConfig = RuntimeConfig()

    @RuntimeDSLTag
    fun config(configure: RuntimeConfig.() -> Unit) {
        config.configure()
    }

}

internal var runtime = RuntimePluginExtension()

@RuntimeDSLTag
fun runtime(configure: RuntimePluginExtension.() -> Unit) {
    runtime.configure()
}