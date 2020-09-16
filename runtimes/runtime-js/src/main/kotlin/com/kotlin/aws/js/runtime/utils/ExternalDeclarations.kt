@file:JsModule("node-fetch")
@file:JsNonModule

package com.kotlin.aws.js.runtime.utils

import org.w3c.fetch.RequestInit
import org.w3c.fetch.Response
import kotlin.js.Promise

@JsName("default")
internal external fun fetch(url: String): Promise<Response>

@JsName("default")
internal external fun fetch(url: String, params: RequestInit): Promise<Response>