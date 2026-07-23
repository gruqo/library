package com.library.demo

import com.library.model.*
import com.library.util.*

fun runDemos(library: Library, cleanCode: PrintedBook) {
    println("\n═══════════════════════════════════════")
    println("         ДЕМО-ПРИМЕРЫ (учебные)")
    println("═══════════════════════════════════════")

    println("\n--- Демо: результаты поиска ---")
    demoSearchResults(library)

    println("\n--- Демо: scope functions ---")
    demoScopeFunctionDemos(library)

    println("\n--- Демо: вычисление страниц ---")
    demoTotalPages(library)

    println("\n--- Демо: тестовые объекты ---")
    demoUnusedTestObjects()

    println("\n--- Демо: try-as-expression ---")
    demoTryResults()

    println("\n--- Демо: методы Library ---")
    demoLibraryMethods(library)

    println("\n--- Демо: удаление книги ---")
    demoRemoveBook(library, cleanCode)

    println("\n--- Демо: AudioBook ---")
    demoAudioBook()
}

private fun demoSearchResults(library: Library) {
    val classics = library.search { it.year < 1950u }
    println("Классика (до 1950): ${classics.map { it.title }}")

    val cheap = library.search { it.price.amount < 500 }
    println("Дешёвые (<500): ${cheap.map { it.title }}")

    val byAuthor = library.search { it.author == "Л. Толстой" }
    println("Толстой: ${byAuthor.map { it.title }}")

    val combo = library.search { it.genre == Genre.FICTION && it.year > 2000u }
    println("Fiction после 2000: ${combo.map { it.title }}")
}

private fun demoScopeFunctionDemos(library: Library) {

    val newBook = PrintedBook("Test", "Тест", 2020, Money(100.0), 1, pages = 100,
        isbn = null, genre = Genre.PROGRAMMING, tags = setOf("test")).apply {
        println("Создана (apply): $title")
    }

    val book01 = library.findByIsbn("9785916719892")?.let {
        println("Найдена книга (let): ${it.title}")
    }

    val book02 = library.findByIsbn("9785916719892")?.run {
        println("Найдена книга (run): ${this.title}")
    }

    val book03 = library.findByIsbn("9785916719892")
    val text03 = with(book03) {
        println("Найдена книга (with): ${this?.title}")
    }

    val book04 = library.findByIsbn("9785916719892")?.apply {
        println("Найдена книга (apply): ${this.title}")
    }

    val book05 = library.findByIsbn("9785916719892")?.also {
        println("Найдена книга (also): ${it.title}")
    }

    val titleLength: Int? = library.findByIsbn("9785916719892")?.let { it.title.length }
    println("Длина названия (let): $titleLength")

    val cardText = with(library.findByIsbn("9785916719892")) {
        """
        ${this?.title}
        ${this?.author}, ${this?.year}
        Цена: ${this?.price}
        """.trimIndent()
    }
    println("Карточка (with): $cardText")
}

private fun demoTotalPages(library: Library) {
    val totalPages = library.mapBooks { (it as? PrintedBook)?.pages?.toInt() ?: 0 }.sum()
    println("Всего страниц (mapBooks): $totalPages")
}

private fun demoUnusedTestObjects() {
    val libTest = Library("Test")
    val cleanCodeTest = PrintedBook("Чистый код", "Р. Мартин", 2008, Money(1290.0), 0, pages = 464,
        isbn = "9785916719892", genre = Genre.PROGRAMMING, tags = setOf("код", "паттерны"))
    println("Тестовая библиотека: ${libTest.name}, тестовая книга: ${cleanCodeTest.title}")
}

private fun demoTryResults() {
    fun safelyParseYear(input: String): Int? = try {
        input.toInt().also {
            if (it !in 1450..2100) throw IllegalArgumentException("Год $it вне диапазона")
        }
    } catch (e: NumberFormatException) {
        println("Не число: $input")
        null
    } catch (e: IllegalArgumentException) {
        println("Логическая ошибка: ${e.message}")
        null
    }

    val year = safelyParseYear("2008")
    println("Год (try-as-expression): $year")

    val bad = safelyParseYear("xyz")
    println("Невалидный год (try-as-expression): $bad")
}

// Старая версия addBook (require) — вытеснена явными исключениями:
//fun addBook(book: Book) {
//    books.add(book)
//    book.isbn?.let { isbn ->
//        require(isbn !in byIsbn) { "Книга с ISBN $isbn уже есть в каталоге" }
//        byIsbn[isbn] = book
//    }
//}

private fun demoLibraryMethods(library: Library) {
    println("По жанрам (countByGenre): ${library.countByGenre()}")
    println("По жанрам (countByGenre2): ${library.countByGenre2()}")
    
    val testIsbn = "9785916719892"
    println("hasIsbn($testIsbn): ${library.hasIsbn(testIsbn)}")
    println("hasIsbn(nonexistent): ${library.hasIsbn("0000000000000")}")
}

private fun demoRemoveBook(library: Library, cleanCode: PrintedBook) {
    println("Книг до удаления: ${library.size}")
    library.removeBook(cleanCode)
    println("Книг после удаления: ${library.size}")

    library.books.add(cleanCode)
    cleanCode.isbn?.let { library.byIsbn[it] = cleanCode }
    println("Книг после возврата: ${library.size}")
}

private fun demoAudioBook() {
    val audio = AudioBook(
        title = "Война и мир (аудио)",
        author = "Л. Толстой",
        year = 2005,
        price = Money(500.0),
        durationMinutes = 2880,
        narrator = "Иван Иванов"
    )
    println("Создана: ${audio.title}")
    println("Категория: ${audio.category}")
    println("Чтец: ${audio.narrator}")
    println("Длительность: ${audio.durationMinutes} мин")
}
