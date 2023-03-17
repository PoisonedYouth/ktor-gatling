package com.poisonedyouth

import io.gatling.javaapi.core.CoreDsl
import io.gatling.javaapi.core.CoreDsl.StringBody
import io.gatling.javaapi.core.CoreDsl.bodyString
import io.gatling.javaapi.core.Simulation
import io.gatling.javaapi.http.HttpDsl

const val HTTP_STATUS_CREATED = 201

fun createCreateUserRequest() = HttpDsl.http("add user").post("/api/v1/user")
    .body(
        StringBody(
            """
                            {
                                "firstName": "#{name}",
                                "lastName": "#{name}",
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

fun createAddBookRequest() = HttpDsl.http("add book")
    .post("/api/v1/user/\${userId}/book/\${bookId}")
    .check(
        HttpDsl.status().`is`(HTTP_STATUS_CREATED),
    )
    .check(
        bodyString().exists()
    )
    .check(bodyString().saveAs("userId"))

class UserSimulation : Simulation() {

    private val baseUrl: String = System.getProperty("baseurl")

    private val httpProtocol = HttpDsl.http
        .baseUrl(baseUrl)

    private val addUserScenario = CoreDsl.scenario("Add new user")
        .feed(nameFeeder)
        .exec(
            createCreateUserRequest(),
        )

    private val addBookScenario = CoreDsl.scenario("Add book to user")
        .feed(nameFeeder)
        .feed(bookFeeder)
        .exec(
            createCreateUserRequest(),
        )
        .exec(
            createCreateBookRequest()
        )
        .exec(
            createAddBookRequest()
        )

    init {
        setUp(
            addUserScenario.injectOpen(CoreDsl.atOnceUsers(100)),
            addBookScenario.injectOpen(CoreDsl.atOnceUsers(100))
        ).protocols(httpProtocol)
    }
}
