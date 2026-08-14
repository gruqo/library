package com.library.model

import com.library.notify.Notifier
import io.mockk.*
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals


class LibraryNotifierTest {
    @Test
    fun notifier() {
        val notifier = mockk<Notifier>(relaxUnitFun = true)
        val library = Library("T", notifier = notifier)
        val book = PrintedBook("X", "Y", 2020, Money(0.0), 1, pages = 1, isbn = null, genre = Genre.OTHER, tags = emptySet())
        library.addBook(book)
        verify(exactly = 1) {
            notifier.bookAdded("X")
        }
    }
    @Test
    fun slot() {
        val notifier = mockk<Notifier>(relaxUnitFun = true)
        val captured = slot<String>()
        every { notifier.bookAdded(capture(captured)) } answers { /* nothing */ }
        val library = Library("T", notifier = notifier)
        library.addBook(
            PrintedBook("Война и мир", "Толстой", 1869, Money(0.0), 1, pages = 1, isbn = null, genre = Genre.OTHER, tags = emptySet())
        )
        assertEquals("Война и мир", captured.captured)
    }
}