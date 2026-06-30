package com.library

import com.library.model.*

fun main() {
    val items: List<Loanable> = listOf(
        PrintedBook("Чистый код", "Р. Мартин", 2008, 1290.0, 3, pages = 464),
        EBook("Kotlin in Action", "Д. Жемеров", 2017, 990.0, sizeMb = 12.5, format = "PDF"),
        AudioBook("Гарри Поттер", "Дж. Роулинг", 1997, 599.0, initialCopies = 2, durationMinutes = 480, narrator = "С. Чонишвили"), )
    for (item in items) {
        if (item is Book) item.printCard() // smart cast в Book
        println("Статус: ${item.describeAvailability()}")
        item.lend()
        println("После выдачи: ${item.describeAvailability()}")
        println("---")
    }
}
