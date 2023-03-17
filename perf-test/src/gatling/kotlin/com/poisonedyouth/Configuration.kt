package com.poisonedyouth

import org.apache.commons.lang3.RandomStringUtils

private const val DEFAULT_STRING_LENGTH = 10

val bookFeeder = generateSequence {
    val title = RandomStringUtils.randomAlphabetic(DEFAULT_STRING_LENGTH)
    val author = RandomStringUtils.randomAlphabetic(DEFAULT_STRING_LENGTH)
    mapOf("title" to title, "author" to author)
}.iterator()

val nameFeeder = generateSequence {
    val name = RandomStringUtils.randomAlphabetic(DEFAULT_STRING_LENGTH)
    mapOf("name" to name)
}.iterator()
