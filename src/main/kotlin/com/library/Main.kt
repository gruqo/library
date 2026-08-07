package com.library

import kotlin.io.path.*
import com.library.demo.runDemos
import com.library.error.*
import com.library.io.printCard
import com.library.model.*
import com.library.util.*

private fun setupLibrary(): Pair<Library, PrintedBook> {
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

    library.addBook(cleanCode)

    printCard(cleanCode)

    return library to cleanCode
}

private fun demoExceptions(library: Library, cleanCode: PrintedBook) {

    try {
        library.addBook(cleanCode)
    } catch (e: BookAlreadyExistsException) {
        println("Ожидаемая ошибка: ${e.message}")
    }

    try {
        library.getByIsbn("0000000000000")
    } catch (e: BookNotFoundException) {
        println("Ожидаемая ошибка: ${e.message}, ISBN был: ${e.isbn}")
    }

    try {
        library.lendOrThrow(cleanCode)
    } catch (e: NotAvailableException) {
        println("Ожидаемая ошибка: ${e.message}")
    }

    try {
        parseIsbnExplicit("abc")
    } catch (e: InvalidIsbnException) {
        println("Ожидаемая ошибка: ${e.message}")
    }

    fun safelyParseYear(input: String): Int? = try {
        input.toInt().also {
            if (it !in 1450..2100) throw IllegalArgumentException("Год $it вне диапазона") }
    } catch (_: NumberFormatException) {
        println("Не число: $input")
        null
    } catch (e: IllegalArgumentException) {println("Логическая ошибка: ${e.message}")
        null
    }

        println(safelyParseYear("2020"))
        println(safelyParseYear("abc"))
        println(safelyParseYear("3000"))

    val csv = library.exportToString()
    println(csv)
}

private fun demoShelves(library: Library, cleanCode: PrintedBook) {
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

    library.addBook(kotlinAction)
    library.addBook(warAndPeace)

    printCard(warAndPeace)

    library.place(cleanCode, 0, 0)
    library.place(warAndPeace, 0, 1)
    library.printShelves()

    for ((genre, list) in library.byGenre()) {
        println("${genre.displayName}: ${list.joinToString { it.title }}")
    }

    println(library)
    println("Всего: ${library.size}")
    println("По ISBN 9785916719892: ${library.findByIsbn("9785916719892")?.title}")

    println("\nПо жанрам:")
}

private fun demoStatistics(library: Library) {
    println("\n--- Статистика ---")
    println("Всего экземпляров: ${library.totalCopies()}")
    println("Средняя цена: ${library.averagePrice()}")
    println("Есть доступные: ${library.hasAvailable()}")
    println("Недоступных: ${library.unavailableCount()}")

    println("\n--- Электронные книги ---")
    for (ebook in library.ebooks()) {
        println("${ebook.title} (${ebook.format}, ${ebook.sizeMb} МБ)")
    }

    println("\n--- Аудиокниги ---")
    for (audio in library.audioBooks()) {
        println("${audio.title} (${audio.durationMinutes} мин, ${audio.narrator})")
    }

    println("Общая длительность аудиокниг: ${library.totalAudioMinutes()} мин")

    println("\n--- Топ по выдачам ---")
    for (book in library.topByLoans(3)) {
        println("${book.title} (${book.totalLoans} выдач)")
    }

    println("\n--- Самые толстые ---")
    for (book in library.topThickest(3)) {
        println("${book.title} (${book.pages} стр.)")
    }

    println("\n--- Топ авторов ---")
    for ((author, count) in library.topAuthors(3)) {
        println("$author — $count книг")
    }

    println("\n--- Общая стоимость всего каталога (цена × экземпляры) ---")
    val fmt = java.text.DecimalFormat("#,##0.00", java.text.DecimalFormatSymbols(java.util.Locale.of("ru", "RU")))
    println("fold:    ${fmt.format(library.totalCatalogValue1())} руб.")
    println("sumOf:   ${fmt.format(library.totalCatalogValue2())} руб.")
}

private fun demoSorting(library: Library) {
    println("\n--- Comparable: PrintedBook по страницам ---")

    val printed = library.allPrintedBooks()
    println("Самая тонкая:  ${printed.minOrNull()}")
    println("Самая толстая: ${printed.maxOrNull()}")
    println("Отсортированные:")
    printed.sorted().forEach { println("    ${it.title} — ${it.pages} стр.") }

    val byYear = compareBy<PrintedBook> { it.year }
    println("  Отсортированные по году:")
    printed.sortedWith(byYear).forEach { println("    ${it.title} — ${it.year} год") }

    val byPrice = compareBy<PrintedBook> { it.price.amount }
    println("  Отсортированные по цене:")
    printed.sortedWith(byPrice).forEach { println("    ${it.title} — ${it.price.amount} руб.") }
}

private fun demoScopeFunctions(library: Library, cleanCode: PrintedBook) {
    library.reserve("Аня", cleanCode)
    library.reserve("Боря", cleanCode)
    println("Очередь: ${library.queueSize()}")
    val next = library.nextReservation()
    println("Следующий — ${next?.first} получит «${next?.second?.title}»")

    val titles = library.all().map(Book::title)
    println("Все названия: $titles")

    val available = library.all().filter(Book::isAvailable)
    println("Доступные: ${available.size}")

    val doLend = cleanCode::lend
    val result = doLend()
    println("Результат lend: $result")

    val description = library.run {
    val total = totalCopies()
    val genres = byGenre().size
    "В библиотеке $total экземпляров $genres жанров"
    }

    println(description)

    val isbn = "9785916719892"
    isbnClean(isbn)
        .takeIf(::isValidChecksum)
        ?.let { println("Валидный ISBN: $it") }

    library.forEachBook { println("- ${it.title} (${it.year})") }

}

fun main() {
    val (library, cleanCode) = setupLibrary()
    demoExceptions(library, cleanCode)
    demoShelves(library, cleanCode)
    demoStatistics(library)
    demoSorting(library)
    demoScopeFunctions(library, cleanCode)
    runDemos(library, cleanCode)

    val original = Library("Original").apply {
        addBook(PrintedBook(
            "Чистый код",
            "Р. Мартин",
            2008,
            Money(1290.0),
            3,
            pages = 464,
            isbn ="9785916719892",
            genre = Genre.PROGRAMMING,
            tags = emptySet()))
        addBook(PrintedBook(
            "Война и мир",
            "Л. Толстой",
            1869,
            Money(750.0),
            2,
            pages = 1225,
            isbn ="9785170123469",
            genre = Genre.FICTION,
            tags = emptySet()))
        }

    val customPath = "project_files_output"
    val fileName= "library.tsv"
    val fullPathToFile = "$customPath/$fileName"

    original.saveToTsv((Path(fullPathToFile)))
    val loaded = library.loadLibraryFromTsv("Loaded",(Path(fullPathToFile)))
    println("Оригинал: ${original.size} книг, после загрузки: ${loaded.size}")
    loaded.all().forEach {
        println(" - ${it.title} (${it.year})")
    }

    println()
    println("Книг Толстого: ${countLinesContaining((Path(fullPathToFile)), "Толстой")}")

    println()
    library.saveWithBackup(Path(fullPathToFile))

    println()
    listBackups(Path("$customPath/backups"))

    println()
    library.exportZip(Path("$customPath/library.zip"))

}
