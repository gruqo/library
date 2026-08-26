package com.library.model

import java.util.Collections
import kotlin.concurrent.thread
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class LibraryConcurrencyTest {

    @Test
    @DisplayName("5 потоков на книге с 3 копиями: ровно 3 успеха и 2 отказа")
    fun concurrentLendingStressTest() {
        val library = Library("Stress")
        val book = PrintedBook(
            "Test", "Test", 2020, Money(0.0), 3, pages = 100,
            isbn = "1234567890123", genre = Genre.OTHER, tags = emptySet()
        )
        library.addBook(book)
        library.defaultLoanPolicy = LoanPolicy(maxLoansPerUser = 5, maxDays = 14)

        val results = Collections.synchronizedList(mutableListOf<LoanResult>())

        val threads = List(5) { idx ->
            thread {
                val r = library.lend(book)
                results.add(r)
                println("Thread $idx: $r")
            }
        }
        threads.forEach { it.join() }

        val successes = results.count { it is LoanResult.Success }
        val failures = results.size - successes
        println("\nПроверка '5 потоков на книге с 3 копиями'")
        println("Всего успешных: $successes")
        println("Всего отказов: $failures")
        println()

        assertEquals(3, successes)
        assertEquals(2, failures)
    }
}
