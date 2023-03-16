package com.poisonedyouth

import io.gatling.javaapi.core.CoreDsl
import io.gatling.javaapi.core.CoreDsl.StringBody
import io.gatling.javaapi.core.CoreDsl.bodyString
import io.gatling.javaapi.core.Simulation
import io.gatling.javaapi.http.HttpDsl


class UserSimulation : Simulation() {

    private val baseUrl: String = System.getProperty("baseurl")

    private val httpProtocol = HttpDsl.http
        .baseUrl(baseUrl)

    private val addUserScenario = CoreDsl.scenario("Add new user")
        .exec(
            HttpDsl.http("add user").post("/api/v1/user")
                .body(
                    StringBody(
                        """
                        {
                            "firstName": "John",
                            "lastName": "Doe",
                            "birthDate" : "1999-01-01"
                        }
                    """.trimIndent()
                    )
                ).asJson()
                .check(
                    HttpDsl.status().`is`(201),
                )
                .check(
                    bodyString().exists()
                ),
        )

    init {
        setUp(
            addUserScenario.injectOpen(CoreDsl.atOnceUsers(100)),
        ).protocols(httpProtocol)
    }
}