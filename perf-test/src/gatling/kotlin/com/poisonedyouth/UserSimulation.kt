package com.poisonedyouth

import io.gatling.javaapi.core.CoreDsl
import io.gatling.javaapi.core.CoreDsl.StringBody
import io.gatling.javaapi.core.CoreDsl.bodyString
import io.gatling.javaapi.core.Simulation
import io.gatling.javaapi.http.HttpDsl

const val HTTP_STATUS_CREATED = 201

class UserSimulation : Simulation() {
    private fun createCreateUserRequest() = HttpDsl.http("add user").post("/api/v1/user")
        .body(
            StringBody(
                """
                {
                    "firstName": "#{firstname}",
                    "lastName": "#{lastname}",
                    "birthDate" : "1999-01-01"
                }
                """.trimIndent()
            )
        ).asJson()
        .check(
            HttpDsl.status().`is`(HTTP_STATUS_CREATED),
        )
        .check(
            bodyString().exists()
        )
        .check(bodyString().transform { body -> body.replace("\"", "") }.saveAs("userId"))

    private fun createAddBookRequest() = HttpDsl.http("add book")
        .post("/api/v1/user/#{userId}/book")
        .body(StringBody(
            """
            {
                "books": ["#{bookId}"]
            }    
            """.trimIndent()
        )).asJson()
        .check(
            HttpDsl.status().`is`(HTTP_STATUS_CREATED),
        )
        .check(
            bodyString().exists()
        )

    private fun createCreateBookRequest() = HttpDsl.http("add book").post("/api/v1/book")
        .body(
            CoreDsl.StringBody(
                """
                {
                    "title": "#{title}",
                    "author": "#{author}"
                }
                """.trimIndent()
            )
        ).asJson()
        .check(
            HttpDsl.status().`is`(HTTP_STATUS_CREATED),
        )
        .check(
            CoreDsl.bodyString().exists()
        )
        .check(CoreDsl.bodyString().transform { body -> body.replace("\"", "") }.saveAs("bookId"))

    private val addBookScenario = CoreDsl.scenario("Add book to user")
        .feed(nameFeeder)
        .feed(bookFeeder)
        .exec(
            createCreateUserRequest()
        )
        .exec(
            createCreateBookRequest()
        )
        .exec(
            createAddBookRequest()
        )

    init {
        setUp(
            addBookScenario.injectOpen(CoreDsl.atOnceUsers(100))
        ).protocols(httpProtocol)
    }
}
