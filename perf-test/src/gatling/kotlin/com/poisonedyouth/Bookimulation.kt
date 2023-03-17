package com.poisonedyouth

import io.gatling.javaapi.core.CoreDsl
import io.gatling.javaapi.core.Simulation
import io.gatling.javaapi.http.HttpDsl

fun createCreateBookRequest() = HttpDsl.http("add book").post("/api/v1/book")
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
    .check(CoreDsl.bodyString().transform { body-> body.replace("\"", "") }.saveAs("bookId"))

class BookSimulation : Simulation() {

    private val baseUrl: String = System.getProperty("baseurl")

    private val httpProtocol = HttpDsl.http
        .baseUrl(baseUrl)

    private val addBookScenario = CoreDsl.scenario("Add new book")
        .feed(bookFeeder)
        .exec(
            createCreateBookRequest(),
        )
    init {
        setUp(
           addBookScenario.injectOpen(CoreDsl.atOnceUsers(100)),
        ).protocols(httpProtocol)
    }
}
