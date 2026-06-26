package com.library

import com.library.model.*

fun main() {
    val lib = Library("Городская библиотека №1", rows = 3, cols = 5)

    val cleanCode = PrintedBook(
        "Чистый код",
        "Р. Мартин",
        2008,
        Money(1290.0),
        3,
        pages = 464,
        isbn = "9785916719892",
        genre = Genre.PROGRAMMING,
        tags = setOf("classic", "must-read")
    )

    val kotlinAction = EBook(
        "Kotlin in Action",
        "Д. Жемеров",
        2017,
        Money(990.0),
        pages = 464,
        sizeMb = 12.5,
        format = "PDF",
        isbn = "9781617293290",
        genre = Genre.PROGRAMMING
    )

    val warAndPeace = PrintedBook(
        "Война и мир",
        "Л. Толстой",
        1869,
        Money(750.0),
        2,
        pages = 1225,
        isbn = "9785170123469",
        genre = Genre.FICTION,
        tags = setOf("classic", "russian")
    )

    lib.addBook(cleanCode)
    lib.addBook(kotlinAction)
    lib.addBook(warAndPeace)

    println(lib) // toString
    println("Всего: ${lib.size}")
    println("По ISBN 9785916719892: ${lib.findByIsbn("9785916719892")?.title}")

    lib.place(cleanCode, 0, 0)
    lib.place(warAndPeace, 0, 1)
    lib.printShelves()

    println("\nПо жанрам:")

    for ((genre, list) in lib.byGenre()) {
        println("${genre.displayName}: ${list.joinToString { it.title }}")
    }
}