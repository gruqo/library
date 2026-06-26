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

//Домашнее задание 4/5Задача
// 4. Полиморфный сценарий в main
//Создайте три разные книги и обработайте их единообразно:
//fun main() {
//    val items: List<Loanable> = listOf(
//        PrintedBook("Чистый код", "Р. Мартин", 2008, 1290.0, 3, pages = 464),
//        EBook("Kotlin in Action", "Д. Жемеров", 2017, 990.0, sizeMb = 12.5, format = "PDF"),
//        AudioBook("Гарри Поттер", "Дж. Роулинг", 1997, 599.0, initialCopies = 2, durationMinutes = 480, narrator = "С. Чонишвили"), )
//    for (item in items) {
//        if (item is Book) item.printCard() // smart cast в Book
//        println("Статус: ${item.describeAvailability()}")
//        item.lend()
//        println("После выдачи: ${item.describeAvailability()}")
//        println("---")
//    }
//}
//Здесь работают:
//•List<Loanable> — список разнородных объектов под общим интерфейсом.
// •is Book + smart cast — проверка типа и автоматическое приведение.
// •Полиморфный вызов lend() — каждый класс делает по-своему.
