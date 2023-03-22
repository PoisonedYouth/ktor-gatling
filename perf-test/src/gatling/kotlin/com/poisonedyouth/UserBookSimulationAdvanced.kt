package com.poisonedyouth

import io.gatling.javaapi.core.CoreDsl
import io.gatling.javaapi.core.CoreDsl.StringBody
import io.gatling.javaapi.core.CoreDsl.bodyString
import io.gatling.javaapi.core.CoreDsl.feed
import io.gatling.javaapi.core.Simulation
import io.gatling.javaapi.http.HttpDsl
import kotlin.random.Random

private const val AMOUNT_INITIAL_DATA = 100

class UserBookSimulationAdvanced : Simulation() {

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
            bodyString().transform { body -> body.replace("\"", "") }.saveAs("userId")
        )

    private fun createCreateBookRequest() = HttpDsl.http("add book").post("/api/v1/book")
        .body(
            StringBody(
                """
                {
                    "title": "#{title}",
                    "author": "#{author}"
                 }
                 """.trimIndent()
            )
        ).asJson()
        .check(bodyString().transform { body ->
            body.replace("\"", "")
        }.saveAs("bookId"))


    private fun createAddBookRequest() = HttpDsl.http("add user book")
        .post("/api/v1/user/#{currentUserId}/book")
        .body(
            StringBody(
                """
                {
                    "books": #{currentBookIds}
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


    private val addUserBookScenario = CoreDsl.scenario("Add book to user")
        .repeat(AMOUNT_INITIAL_DATA)
        .on(
            feed(nameFeeder)
                .feed(bookFeeder)
                .exec(createCreateUserRequest())
                .exec { session ->
                    val userIds: List<String> = session.getList("userIds")
                    val userId: String = session.get("userId") ?: error("UserId not found")
                    session.set("userIds", userIds + userId)
                }
                .exec(createCreateBookRequest())
                .exec { session ->
                    val userIds: List<String> = session.getList("bookIds")
                    val userId: String = session.get("bookId") ?: error("BookId not found")
                    session.set("bookIds", userIds + userId)
                }
        )
        .exec { session ->
            val userIds: List<String> = session.getList("userIds")
            val userId = userIds.randomOrNull()
            session.set("currentUserId", userId)
        }
        .exec { session ->
            val bookIds: List<String> = session.getList("bookIds")
            val bookIdsTaken = bookIds.shuffled().take(Random.nextInt(1, AMOUNT_INITIAL_DATA))
            session.set("currentBookIds", bookIdsTaken.map { "\"$it\"" })
        }
        .exec(createAddBookRequest())

    init {
        setUp(
            addUserBookScenario.injectOpen(CoreDsl.atOnceUsers(10))
        ).protocols(httpProtocol)
    }
}
