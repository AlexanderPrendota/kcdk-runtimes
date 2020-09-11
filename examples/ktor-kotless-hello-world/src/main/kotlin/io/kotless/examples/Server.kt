package io.kotless.examples

import io.kotless.dsl.ktor.Kotless
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*

class Server : Kotless() {
  override fun prepare(app: Application) {
    app.routing {
      get("/") {
        call.respondText("Hello world!")
      }
    }
  }
}
