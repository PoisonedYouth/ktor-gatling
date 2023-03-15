package com.poisonedyouth.user

import java.util.*

data class UserDto(
    val id: UUID = UUID.randomUUID(),
    val firstName: String,
    val lastName: String,
    val birthDate: String
)

data class UserWithBooksDto(
    val id: UUID = UUID.randomUUID(),
    val firstName: String,
    val lastName: String,
    val birthDate: String,
    val books: List<BookDto> = emptyList()
)

data class BookDto(
    val id: UUID = UUID.randomUUID(),
    val title: String,
    val author: String
)
