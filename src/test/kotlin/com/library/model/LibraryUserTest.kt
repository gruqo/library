package com.library.model

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.assertEquals

class LibraryUserTest {
    @ParameterizedTest
    @MethodSource("emailCases")
    fun email(email: String, expected: Boolean) {
        assertEquals(expected, LibraryUser.isValidEmail(email))
    }
    companion object {
        @JvmStatic
        fun emailCases() = listOf(
            Arguments.of("ann@example.com", true),Arguments.of("user.name+tag@sub.example.com", true),
            Arguments.of("not-an-email", false),
            Arguments.of("@example.com", false),
            Arguments.of("user@", false),
            Arguments.of("user@example", false),
            Arguments.of("", false),
        )
    }
}
//    Здесь:
//    •@MethodSource ссылается на функцию по имени в companionobject.
//    •@JvmStatic — обязательно, иначе JUnit не найдёт.