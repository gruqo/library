package com.library.dsl

import com.library.model.*
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class LibraryDslTest {

    @Test
    fun `configure DSL builds library with books`() {
        val lib = library("Test") {
            book {
                title = "Чистый код"
                author = "Р. Мартин"
                year = 2008
                pages = 464
            }
            book {
                title = "Война и мир"
                author = "Л. Толстой"
                year = 1869
            }
        }
        assertEquals(2, lib.size)
        assertEquals("Чистый код", lib.all()[0].title)
        assertEquals("Р. Мартин", lib.all()[0].author)
        assertEquals(464, lib.all()[0].pages.toInt())
        assertEquals(100, lib.all()[1].pages.toInt())
    }
}
