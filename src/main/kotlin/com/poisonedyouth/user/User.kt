package com.poisonedyouth.user

import java.time.LocalDate
import java.util.UUID

data class User(
    val id: UUID,
    val firstName: String,
    val lastName: String,
    val birthDate: LocalDate,
    val books: List<Book> = emptyList()
)

data class Book(
    val id: UUID,
    val title: String,
    val author: String
)
