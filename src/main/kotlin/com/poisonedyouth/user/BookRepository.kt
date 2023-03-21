package com.poisonedyouth.user

import com.poisonedyouth.user.UserRepositoryImpl.UserBookTable
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

interface BookRepository {

    fun save(book: Book): UUID
    fun findBy(user: User): List<Book>
    fun findBy(bookId: UUID): Book
    fun findBy(userId: UUID, bookId: UUID): Book
}

class BookRepositoryImpl : BookRepository {

    override fun save(book: Book): UUID = transaction {
        BookTable.insertAndGetId {
            it[id] = book.id
            it[title] = book.title
            it[author] = book.author
        }.value
    }

    override fun findBy(userId: UUID, bookId: UUID): Book = transaction {
        val userBook = UserBookTable.select { UserBookTable.userId eq userId }.map { it[UserBookTable.bookId] }.single()
        BookTable.select { BookTable.id eq userBook }.single().toBook()
    }

    override fun findBy(user: User): List<Book> = transaction {
        val userBooks = UserBookTable.select { UserBookTable.userId eq user.id }.map { it[UserBookTable.bookId] }
        BookTable.select { BookTable.id inList userBooks.toList() }.map { it.toBook() }
    }

    override fun findBy(bookId: UUID): Book = transaction {
        BookTable.select { BookTable.id eq bookId }.single().toBook()
    }

    private fun ResultRow.toBook() = Book(
        id = this[BookTable.id].value,
        title = this[BookTable.title],
        author = this[BookTable.author]
    )

    object BookTable : UUIDTable("book", "id") {
        val title = varchar("title", DEFAULT_COLUMN_LENGTH)
        val author = varchar("author", DEFAULT_COLUMN_LENGTH)
    }
}
