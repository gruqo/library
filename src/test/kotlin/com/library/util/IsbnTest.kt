package com.library.util

import com.library.error.InvalidIsbnException
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource
import kotlin.test.assertEquals

class IsbnTest {
    @ParameterizedTest
    @CsvSource(
        "978-5-91671-989-5, 9785916719895",
        "9785916719895, 9785916719895",
        "978 5 916 71 989 5, 9785916719895"
    )
    fun cleanIsbn (input: String, expected: String) {
        assertEquals(expected, parseIsbnExplicit(input))
    }
    @ParameterizedTest
    @ValueSource(strings = ["abc", "12345", "978-5-91671-989-X", ""])
    fun isbnTest (input: String) {
        assertThrows<InvalidIsbnException> {
            parseIsbnExplicit(input)
        }
    }
}