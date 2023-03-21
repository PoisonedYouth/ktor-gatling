package com.poisonedyouth.user

import java.time.LocalDate
import java.util.UUID

interface ApplicationService {
    fun saveUser(userDto: UserDto): UUID
    fun getUser(userId: UUID): UserWithBooksDto
    fun saveBook(bookDto: BookDto): UUID
    fun addBooks(userId: UUID, bookIdsDto: BookIdsDto): List<UUID>
}

class ApplicationServiceImpl(
    private val userRepository: UserRepository,
    private val bookRepository: BookRepository
) : ApplicationService {

    override fun saveUser(userDto: UserDto): UUID {
        val user = userDto.toUser()
        return userRepository.save(user)
    }

    override fun getUser(userId: UUID): UserWithBooksDto {
        val user = userRepository.findBy(userId)
        val books = bookRepository.findBy(user)
        return user.toUserDto().copy(
            books = books.map { it.toBookDto() }
        )
    }

    override fun addBooks(userId: UUID, bookIdsDto: BookIdsDto): List<UUID> {
        val user = userRepository.findBy(userId)
        val bookIds = mutableListOf<UUID>()
        bookIdsDto.books.forEach {
            val book = bookRepository.findBy(UUID.fromString(it))
            bookIds.add(userRepository.addBook(user, book))
        }
        return bookIds
    }

    override fun saveBook(bookDto: BookDto): UUID {
        val book = bookDto.toBook()
        return bookRepository.save(book)
    }

    private fun UserDto.toUser() = User(
        id = this.id,
        firstName = this.firstName,
        lastName = this.lastName,
        birthDate = LocalDate.parse(this.birthDate)
    )

    private fun User.toUserDto() = UserWithBooksDto(
        id = this.id,
        firstName = this.firstName,
        lastName = this.lastName,
        birthDate = this.birthDate.toString(),
        books = this.books.map { it.toBookDto() }
    )

    private fun BookDto.toBook() = Book(
        id = this.id,
        title = this.title,
        author = this.author
    )

    private fun Book.toBookDto() = BookDto(
        id = this.id,
        title = this.title,
        author = this.author
    )
}
