package com.library

import com.library.model.*
import com.library.util.cleanIsbn
import com.library.util.isValidIsbn

fun main() {
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

    val harryPotter = AudioBook(
        "Гарри Поттер",
        "Дж. Роулинг",
        1997,
        Money(599.0),
        initialCopies = 2,
        durationMinutes = 480,
        narrator = "С. Чонишвили",
        genre = Genre.FICTION
    )

    listOf(cleanCode, kotlinAction, warAndPeace, harryPotter).forEach { library.addBook(it) }

    library.place(cleanCode, 0, 0)
    library.place(warAndPeace, 0, 1)
    library.printShelves()

    repeat(3) { cleanCode.lend() }
    warAndPeace.lend()

    println("\n--- Статистика ---")
    println("Средняя цена: ${library.averagePrice()}")
    println("Есть доступные книги: ${library.hasAvailable()}")
    println("Недоступных книг: ${library.unavailableCount()}")

    println("\n--- Электронные книги ---")
    for (ebook in library.ebooks()) {
        println("  ${ebook.title} (${ebook.format}, ${ebook.sizeMb} МБ)")
    }

    println("\n--- Аудиокниги ---")
    for (audio in library.audioBooks()) {
        println("  ${audio.title} (${audio.durationMinutes} мин, ${audio.narrator})")
    }
    println("Общая длительность аудиокниг: ${library.totalAudioMinutes()} мин")

    println("\n--- Каталог по жанрам ---")
    for ((genre, list) in library.byGenre()) {
        println("${genre.displayName}: ${list.joinToString { it.title }}")
    }

    println("\n--- Счётчик по жанрам ---")
    println("  groupBy + mapValues:  ${library.countByGenre().mapKeys { it.key.displayName }}")
    println("  groupingBy + eachCount: ${library.countByGenre2().mapKeys { it.key.displayName }}")

    println("\n--- Топ по выдачам ---")
    for (book in library.topByLoans(3)) {
        println("  ${book.title} (${book.totalLoans} выдач)")
    }

    println("\n--- Самые толстые ---")
    for (book in library.topThickest(3)) {
        println("  ${book.title} (${book.pages} стр.)")
    }

    println("\n--- Топ авторов ---")
    for ((author, count) in library.topAuthors(3)) {
        println("  $author — $count книг")
    }

    println("\n--- Comparable: PrintedBook по страницам ---")
    val printed = library.allPrintedBooks()
    println("  Самая тонкая:  ${printed.minOrNull()?.title}")
    println("  Самая толстая: ${printed.maxOrNull()?.title}")
    for (book in printed.sorted()) {
        println("    ${book.title} — ${book.pages} стр.")
    }

    println("\n--- Очередь резервирования ---")
    library.reserve("Аня", cleanCode)
    library.reserve("Боря", cleanCode)
    println("Очередь: ${library.queueSize()}")
    val next = library.nextReservation()
    println("Следующий — ${next?.first} получит «${next?.second?.title}»")
    println("В очереди осталось: ${library.queueSize()}")

    println("\n--- Поиск через search { } ---")
    println("Классика до 1950 года: ${library.search { it.year < 1950 }.map(Book::title)}")
    println("Дешевле 500 руб.: ${library.search { it.price.amount < 500 }.map(Book::title)}")
    println("Книги Л. Толстого: ${library.search { it.author == "Л. Толстой" }.map(Book::title)}")
    println(
        "Художественные после 2000 года: " +
            library.search { it.genre == Genre.FICTION && it.year > 2000 }.map(Book::title)
    )

    println("\n--- Function references ---")
    val titles = library.all().map(Book::title)
    println("Все названия: $titles")

    val available = library.all().filter(Book::isAvailable)
    println("Доступные книги: ${available.map(Book::title)}")

    val doLend = warAndPeace::lend
    val lendResult = doLend()
    println("Результат lend через bound reference: $lendResult")

    val isPositive: (Int) -> Boolean = { it > 0 }
    println("Положительных среди [-2, 0, 3, 5]: ${listOf(-2, 0, 3, 5).count(isPositive)}")

    println("\n--- apply: конфигурация при создании ---")
    val newBook = PrintedBook(
        "Test",
        "Тест",
        2020,
        Money(100.0),
        1,
        pages = 100,
        isbn = null,
        genre = Genre.PROGRAMMING,
        tags = setOf("test")
    ).apply {
        println("Создана: $title, автор $author, $pages стр.")
    }
    library.addBook(newBook)

    println("\n--- also: side effect при поиске ---")
    val found = library.findByIsbn("9781617293290")?.also {
        println("Найдена книга: ${it.title}")
    }
    println("also вернул тот же объект: ${found?.title}")

    println("\n--- let: null-safety и преобразование ---")
    val titleLength: Int? = library.findByIsbn("9781617293290")?.let { it.title.length }
    println("Длина названия Kotlin in Action: $titleLength")
    val missingLength: Int? = library.findByIsbn("0000000000000")?.let { it.title.length }
    println("Длина названия ненайденной книги: $missingLength")

    println("\n--- run: блок с результатом ---")
    val description = library.run {
        val total = totalCopies()
        val genres = byGenre().size
        "В библиотеке $total экземпляров, $genres жанров"
    }
    println(description)

    println("\n--- with: несколько обращений к одному объекту ---")
    val cardText = with(library.findByIsbn("9781617293290")) {
        """
        ${this?.title}
        ${this?.author}, ${this?.year}
        Цена: ${this?.price}
        """.trimIndent()
    }
    println(cardText)

    println("\n--- ?.let: чистый null-safety кейс ---")
    val isbn: String? = "9781617293290"
    isbn?.let { cleanIsbn(it) }
        ?.takeIf(::isValidIsbn)
        ?.let { println("Валидный ISBN: $it") }

    println("\n--- forEachBook / mapBooks ---")
    library.forEachBook { println("- ${it.title} (${it.year})") }
    val totalPages = library.mapBooks { (it as? PrintedBook)?.pages ?: 0 }.sum()
    println("Всего страниц в печатных книгах: $totalPages")
}
