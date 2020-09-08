package com.kotlin.aws.runtime.dsl

import java.io.File
import java.io.Serializable
import java.nio.file.Path

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

    @RuntimeDSLTag
    class RuntimeConfig : Serializable {
        var reflectConfigurationFile: Path? = null
        var flags: List<String>? = null
        var image: String? = null
    }

}

internal var runtime = RuntimePluginExtension()

@RuntimeDSLTag
fun runtime(configure: RuntimePluginExtension.() -> Unit) {
    runtime.configure()
}
