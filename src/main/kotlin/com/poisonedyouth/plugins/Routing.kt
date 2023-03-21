package com.poisonedyouth.plugins

import com.poisonedyouth.user.ApplicationService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import org.koin.ktor.ext.inject
import java.util.UUID

fun Application.configureRouting() {
    val service by inject<ApplicationService>()

    routing {
        post("/api/v1/user") {
            call.respond(
                status = HttpStatusCode.Created,
                message = service.saveUser(
                    call.receive()
                )
            )
        }
        get("/api/v1/user/{userId}") {
            call.respond(
                status = HttpStatusCode.OK,
                message = service.getUser(
                    UUID.fromString(call.parameters["userId"])
                )
            )
        }
        post("/api/v1/user/{userId}/book") {
            call.respond(
                status = HttpStatusCode.Created,
                message = service.addBooks(
                    userId = UUID.fromString(call.parameters["userId"]),
                    bookIdsDto = call.receive()
                )
            )
        }
        post("/api/v1/book") {
            call.respond(
                status = HttpStatusCode.Created,
                message = service.saveBook(
                    call.receive()
                )
            )
        }
    }
}
