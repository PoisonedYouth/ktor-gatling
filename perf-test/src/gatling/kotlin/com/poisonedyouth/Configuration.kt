package com.poisonedyouth

import io.gatling.javaapi.core.CoreDsl.csv
import io.gatling.javaapi.http.HttpDsl
import org.apache.commons.lang3.RandomStringUtils

private const val DEFAULT_STRING_LENGTH = 10

val baseUrl: String = System.getProperty("baseurl")

val httpProtocol = HttpDsl.http
    .baseUrl(baseUrl)

val bookFeeder = generateSequence {
    val title = RandomStringUtils.randomAlphabetic(DEFAULT_STRING_LENGTH)
    val author = RandomStringUtils.randomAlphabetic(DEFAULT_STRING_LENGTH)
    mapOf("title" to title, "author" to author)
}.iterator()

val nameFeeder = generateSequence {
    val firstname = RandomStringUtils.randomAlphabetic(DEFAULT_STRING_LENGTH)
    val lastname = RandomStringUtils.randomAlphabetic(DEFAULT_STRING_LENGTH)
    mapOf("firstname" to firstname, "lastname" to lastname)
}.iterator()

val nameFeederCsv = csv(
    "data/names.csv"
).eager()
