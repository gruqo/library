package com.library.model

import com.library.error.BookAlreadyExistsException
import com.library.error.BookNotFoundException
import org.junit.jupiter.api.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import org.junit.jupiter.api.assertThrows

class LibraryTest {

    private lateinit var library: Library
    private lateinit var sampleBook: PrintedBook

    @BeforeEach
    fun setup() {
        library = Library("Test Library")
        sampleBook = PrintedBook(
            "Чистый код", "Р. Мартин", 2008, Money(1290.0), 3, pages = 464,
            isbn = "9785916719892", genre = Genre.PROGRAMMING, tags = emptySet()
        )
    }

    @Test
    @DisplayName("Новая библиотека пустая")
    fun emptyLibrary() {
        assertEquals(0, library.size)
        assertNull(library.findByIsbn("9785916719892"))
    }

    @Test
    @DisplayName("Добавление книги увеличивает size и индекс по ISBN")
    fun addBook() {
        library.addBook(sampleBook)

        assertEquals(1, library.size)
        assertNotNull(library.findByIsbn("9785916719892"))
        assertEquals(sampleBook, library.findByIsbn("9785916719892"))
    }

    @Test
    @DisplayName("Дубликат по ISBN бросает исключение")
    fun duplicateIsbnThrows() {
        library.addBook(sampleBook)
        assertThrows<BookAlreadyExistsException> {
            library.addBook(sampleBook)
        }
    }

    @Test
    @DisplayName("getByIsbn бросает, если книги нет")
    fun getByIsbnThrows() {
        val ex = assertThrows<BookNotFoundException> {
            library.getByIsbn("0000000000000")
        }
        assertEquals("0000000000000", ex.isbn)
    }

    @Test
    @DisplayName("Поиск по предикату возвращает корректные книги")
    fun searchPredicate() {
        library.addBook(sampleBook)
        val results = library.search { it.year > 2000u }
        assertEquals(1, results.size)
    }
}
