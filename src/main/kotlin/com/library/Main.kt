package com.library

import com.library.model.*

fun main() {
    val items: List<Loanable> = listOf(
        PrintedBook("Чистый код", "Р. Мартин", 2008, Money(1290.0), 3, pages = 464),
        EBook("Kotlin in Action", "Д. Жемеров", 2017, Money(990.0), sizeMb = 12.5, format = "PDF"),
        AudioBook("Гарри Поттер", "Дж. Роулинг", 1997, Money(599.0), initialCopies = 2, durationMinutes = 480, narrator = "С. Чонишвили"), )

    LibraryRegistry.register(items[0] as Book)
    LibraryRegistry.register(items[1] as Book)
    LibraryRegistry.register(items[2] as Book)

    for (item in items) {
        if (item is Book) item.printCard() // smart cast в Book
        println("Статус: ${item.describeAvailability()}")

        when (val result = item.lend()) {
            is LoanResult.Success -> println("✓ Выдано")
            is LoanResult.NotAvailable -> println("✗ Нет в наличии (доступно: ${result.available})")
            is LoanResult.TooManyOnHand -> println("✗ Превышен лимит ${result.limit}")
            is LoanResult.BookNotInLibrary -> println("✗ Книги нет в каталоге")
        }

        println("После выдачи: ${item.describeAvailability()}")
        println("---")
    }

    println(LibraryRegistry.summary())
}
