package com.library

import com.library.model.*
import com.library.util.*

fun main() {

    // --- 10 --- Домашнее задание 6/7
    val library = Library("Городская библиотека №1", rows = 3, cols = 5)

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

    library.addBook(cleanCode)
    library.addBook(kotlinAction)
    library.addBook(warAndPeace)

    library.place(cleanCode, 0, 0)
    library.place(warAndPeace, 0, 1)
    library.printShelves()

    //for ((genre, list) in library.byGenre()) {
    //    println("${genre.displayName}: ${list.joinToString { it.title }}")
    //}

    //println(library)
    //println("Всего: ${library.size}")
    //println("По ISBN 9785916719892: ${library.findByIsbn("9785916719892")?.title}")

    //println("\nПо жанрам:")

    //println("\n--- Статистика ---")
    //println("Всего экземпляров: ${library.totalCopies()}")
    //println("Средняя цена: ${library.averagePrice()}")
    //println("Есть доступные: ${library.hasAvailable()}")
    //println("Недоступных: ${library.unavailableCount()}")

    //println("\n--- Электронные книги ---")
    //for (ebook in library.ebooks()) {
    //    println("  ${ebook.title} (${ebook.format}, ${ebook.sizeMb} МБ)")
    //}

    //println("\n--- Аудиокниги ---")
    //for (audio in library.audioBooks()) {
    //    println("  ${audio.title} (${audio.durationMinutes} мин, ${audio.narrator})")
    //}

    //println("Общая длительность аудиокниг: ${library.totalAudioMinutes()} мин")

    //println("\n--- Топ по выдачам ---")
    //for (book in library.topByLoans(3)) {
    //    println("  ${book.title} (${book.totalLoans} выдач)")
    //}

    //println("\n--- Самые толстые ---")
    //for (book in library.topThickest(3)) {
    //    println("  ${book.title} (${book.pages} стр.)")
    //}

    //println("\n--- Топ авторов ---")
    //for ((author, count) in library.topAuthors(3)) {
    //    println("  $author — $count книг")
    //}

    //println("\n--- Общая стоимость всего каталога (цена × экземпляры) ---")
    //println("  fold:    ${library.totalCatalogValue1()} руб.")
    //println("  sumOf:   ${library.totalCatalogValue2()} руб.")

    // --- 11 --- Домашнее задание 6/8
    //println("\n--- Comparable: PrintedBook по страницам ---")
    //val printed = library.allPrintedBooks()
    //println("  Самая тонкая:  ${printed.minOrNull()}")
    //println("  Самая толстая: ${printed.maxOrNull()}")
    //println("  Отсортированные:")
    //printed.sorted().forEach { println("    ${it.title} — ${it.pages} стр.") }
    //
    //val printed = library.allPrintedBooks()
    //printed.sorted()
    //
    //val sortedByPages = printed.sorted()
    //println("  Отсортированные по страницам:")
    //sortedByPages.forEach { println("    ${it.title} — ${it.pages} стр.") }
    //
    //val byYear = compareBy<PrintedBook> { it.year }
    //println("  Отсортированные по году:")
    //printed.sortedWith(byYear).forEach { println("    ${it.title} — ${it.year} год") }
    //
    //val byPrice = compareBy<PrintedBook> { it.price.amount }
    //println("  Отсортированные по цене:")
    //printed.sortedWith(byPrice).forEach { println("    ${it.title} — ${it.price.amount} руб.") }


    // --- 11 --- Домашнее задание 7/8
    //library.reserve("Аня", cleanCode)
    //library.reserve("Боря", cleanCode)
    //println("Очередь: ${library.queueSize()}")
    //val next = library.nextReservation()
    //println("Следующий — ${next?.first} получит «${next?.second?.title}»")


    // --- 12 --- Домашнее задание 1/6
    //val classics = library.search { it.year < 1950u }
    //val cheap = library.search { it.price.amount < 500 }
    //val byAuthor = library.search { it.author == "Л. Толстой" }
    //val combo = library.search { it.genre == Genre.FICTION && it.year > 2000u }

    // --- 12 --- Домашнее задание 2/6
    //val titles = library.all().map(Book::title)
    //println("Все названия: $titles")

    //val available = library.all().filter(Book::isAvailable)
    //println("Доступные: ${available.size}")

    //val doLend = cleanCode::lend
    //val result = doLend()
    //println("Результат lend: $result")

    // --- 12 --- Домашнее задание 3.1/6
    //val newBook = PrintedBook("Test", "Тест", 2020, Money(100.0), 1, pages = 100,
    //    isbn = null, genre = Genre.PROGRAMMING, tags = setOf("test")).apply {
    //    println("Создана: $title")
    //}

    //val book01 = library.findByIsbn("9785916719892")?.let {
    //  println("Найдена книга: ${it.title}")
    //}

    //val book02 = library.findByIsbn("9785916719892")?.run {
    //    println("Найдена книга: ${this.title}")
    //}

    //val book03 = library.findByIsbn("9785916719892")
    //val text03 = with(book03) {
    //    println("Найдена книга: ${this?.title}")
    //}

    //val book04 = library.findByIsbn("9785916719892")?.apply {
    //    println("Найдена книга: ${this.title}")
    //}

    //val book05 = library.findByIsbn("9785916719892")?.also {
    //    println("Найдена книга: ${it.title}")
    //}

    // --- 12 --- Домашнее задание 3.2/6
    //val titleLength: Int? = library.findByIsbn("9785916719892")?.let { it.title.length }
    //val description = library.run {
    //val total = totalCopies()
    //val genres = byGenre().size
    //"В библиотеке $total экземпляров $genres жанров"
    //}

    //println(description)

    //val cardText = with(library.findByIsbn("9785916719892")) {
    //    """
    //    ${this?.title}
    //    ${this?.author}, ${this?.year}
    //    Цена: ${this?.price}
    //    """.trimIndent()
    //}

    // --- 12 --- Домашнее задание 4/6
    //val isbn: String? = "9785916719892"
    //isbn?.let { isbnClean(it) }
    //    ?.takeIf(::isbnValidate)
    //    ?.let { println("Валидный ISBN: $it") }

    // --- 12 --- Домашнее задание 5/6
    //library.forEachBook { println("- ${it.title} (${it.year})") }
    //val totalPages = library.mapBooks { (it as? PrintedBook)?.pages?.toInt() ?: 0 }.sum()
}
