package com.poisonedyouth

import io.gatling.javaapi.core.CoreDsl
import io.gatling.javaapi.core.CoreDsl.StringBody
import io.gatling.javaapi.core.CoreDsl.bodyString
import io.gatling.javaapi.core.Simulation
import io.gatling.javaapi.http.HttpDsl


class UserCSVFeederSimulation : Simulation() {
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


    private val addBookScenario = CoreDsl.scenario("Add book to user")
        .feed(nameFeederCsv)
        .exec(
            createCreateUserRequest()
        )

    init {
        setUp(
            addBookScenario.injectOpen(CoreDsl.atOnceUsers(10))
        ).protocols(httpProtocol)
    }
}
