package com.poisonedyouth.user

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

const val DEFAULT_COLUMN_LENGTH = 255

interface UserRepository {
    fun save(user: User): UUID
    fun findBy(userId: UUID): User
    fun addBook(user: User, book: Book): UUID
}

class UserRepositoryImpl : UserRepository {

    override fun save(user: User): UUID = transaction {
        val userId = saveUser(user)
        user.books.forEach {
            saveUserBook(userId, it)
        }
        userId
    }

    private fun saveUserBook(userId: UUID, it: Book) {
        UserBookTable.insertAndGetId { insertStatement ->
            insertStatement[UserBookTable.userId] = userId
            insertStatement[bookId] = it.id
        }
    }

    private fun saveUser(user: User) = UserTable.insertAndGetId { insertStatement ->
        insertStatement[id] = user.id
        insertStatement[firstName] = user.firstName
        insertStatement[lastName] = user.lastName
        insertStatement[birthDate] = user.birthDate
    }.value

    override fun findBy(userId: UUID): User = transaction {
        UserTable.select { UserTable.id eq userId }.single().toUser()
    }

    private fun ResultRow.toUser() = User(
        id = this[UserTable.id].value,
        firstName = this[UserTable.firstName],
        lastName = this[UserTable.lastName],
        birthDate = this[UserTable.birthDate]
    )

    override fun addBook(user: User, book: Book): UUID = transaction {
        UserBookTable.insertAndGetId { insertStatement ->
            insertStatement[userId] = user.id
            insertStatement[bookId] = book.id
        }.value
    }

    object UserTable : UUIDTable("user", "id") {
        val firstName = varchar("first_name", DEFAULT_COLUMN_LENGTH)
        val lastName = varchar("last_name", DEFAULT_COLUMN_LENGTH)
        val birthDate = date("birth_date")

        init {
            uniqueIndex(firstName, lastName)
        }
    }

    object UserBookTable : UUIDTable("user_book", "id") {
        val userId = uuid("user_id")
        val bookId = uuid("book_id")
    }
}
