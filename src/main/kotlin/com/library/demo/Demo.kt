package com.library.demo

import com.library.model.*
import com.library.util.*
import com.library.dsl.library
import com.library.dsl.loadBooksFromYaml
import com.library.repo.Identifiable
import com.library.repo.Repository
import com.library.repo.BookSource


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

    println("\n--- Демо: extractIsbns ---")
    demoExtractIsbns()

    println("\n--- Демо: LibraryUser ---")
    demoLibraryUser()

    println("\n--- Демо: searchHighlighted ---")
    demoSearchHighlighted(library)

    println("\n--- Демо: extractLastName ---")
    demoExtractLastName()

    println("\n--- Демо: Library.report ---")
    println(library.report())

    println("\n--- Демо: lazy authorIndex ---")
    demoLazy(library, cleanCode)

    println("\n--- Демо: lateinit defaultLoanPolicy ---")
    demoLateinit()

    println("\n--- Демо: memoized genreCount ---")
    demoMemo(library)

    println("\n--- Демо: IndexedLibrary (делегирование) ---")
    demoIndexedLibrary()

    println("\n--- Демо: YAML-DSL библиотека ---")
    demoYamlLibrary()

    println("\n--- Демо: @DslMarker в действии ---")
    demoDslMarker()

    println("\n--- Демо: Repository<T> ---")
    demoRepository()

    println("\n--- Демо: BookSource (variance) ---")
    demoBookSource()

    println("\n--- Демо: reified ofType ---")
    demoOfType()

    println("\n--- Демо: ISBN extensions ---")
    demoIsbnExtensions()

    println("\n--- Демо: операторы Library ---")
    demoOperators()

    println("\n--- Демо: infix byAuthor ---")
    demoByAuthor()

    println("")
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
    val cleanCodeTest = loadBooksFromYaml("src_files/printed_books.yaml").first() as PrintedBook
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
    val audio = loadBooksFromYaml("src_files/audiobooks.yaml").first() as AudioBook
    println("Создана: ${audio.title}")
    println("Категория: ${audio.category}")
    println("Чтец: ${audio.narrator}")
    println("Длительность: ${audio.durationMinutes} мин")
}

fun extractIsbns(text: String): List<String> {
 val pattern = Regex("""(?:97[89])(?:[-\s]?\d){10}""")
    return pattern.findAll(text)
        .map { it.value.replace(Regex("[-\\s]"), "") } // нормализуем
        .toList()
}

private fun demoExtractIsbns() {
    val texts = listOf(
        "Серия: Бестселлеры O'Reilly. ISBN: 978-5-91671-989-2. Страниц: 464.",
        "isbn 9785916719892, мягкая обложка",
        "Артикул 978-5-9907763-1-3 (13 цифр)",
        "Книга без ISBN — самиздат"
    )
    for (text in texts) {
        val found = extractIsbns(text)
        println("  Вход: \"${text.take(40)}...\" → Найдено: ${if (found.isEmpty()) "нет" else found}")
    }
}

private fun demoLibraryUser() {
    val ann = LibraryUser("Анна", "ann@example.com")
    println("Создан пользователь: ${ann.name} <${ann.email}>")

    try {
        val bad = LibraryUser("Боб", "not-an-email")
    } catch (e: IllegalArgumentException) {
        println(e.message)
    }
}

private fun demoSearchHighlighted(library: Library) {
    println("Поиск 'кот': ${library.searchHighlighted("кот")}")
    println("Поиск 'толстой': ${library.searchHighlighted("толстой")}")
}

private fun demoExtractLastName() {
    val samples = listOf("Лев Толстой", "Толстой Л.Н.", "Л. Н. Толстой", "Tolstoy, Leo")
    for (sample in samples) {
        println("  \"$sample\" -> \"${extractLastName(sample)}\"")
    }
}

private fun demoLazy(library: Library, cleanCode: PrintedBook) {
    val book1 = cleanCode
    val book2 = PrintedBook(
        "Война и мир", "Л. Толстой", 1869, Money(750.0), 2,
        pages = 1225, isbn = "9785170123469", genre = Genre.FICTION, tags = emptySet()
    )

    val lib = Library("...").apply {
        addBook(book1); addBook(book2)
    }

    println("Перед обращением — лениво ничего не вычислено")
    println(lib.authorIndex)
    println(lib.authorIndex)
}

private fun demoLateinit() {
    val lib = Library("Test")

    try {
        lib.lend(PrintedBook(title = "Чистый код", author = "Р. Мартин", year = 2008, price = Money(1290.0),
            copies = 1, pages = 464, isbn = "9785916719892", genre = Genre.PROGRAMMING, tags = emptySet()
        ))
    } catch (e: IllegalStateException) {
        println("До настройки: ${e.message}")
    }

    lib.defaultLoanPolicy = LoanPolicy(maxLoansPerUser = 5, maxDays = 14)
    println("Политика настроена: ${lib.defaultLoanPolicy}")

    val book = PrintedBook(title = "Война и мир", author = "Л. Толстой", year = 1869, price = Money(750.0),
        copies = 2, pages = 1225, isbn = "9785170123469", genre = Genre.FICTION, tags = emptySet()
    )
    lib.addBook(book)
    val result = lib.lend(book)
    println("Выдача: $result")
}

private fun demoMemo(library: Library) {
    println(library.genreCount)
    println(library.genreCount)
}

private fun demoIndexedLibrary() {
    val ilib = IndexedLibrary("Indexed")
    loadBooksFromYaml("src_files/printed_books.yaml").forEach { ilib.addBook(it) }

    println("Размер: ${ilib.size}")
    println("ISBN 9785916719892: ${ilib["9785916719892"]}")
    for ((isbn, book) in ilib) {
        println("$isbn -> ${book.title}")
    }

    println("Толстой: ${ilib.byAuthor("Л. Толстой")}")
}

private fun demoYamlLibrary() {
    val lib = library(
        "Городская №1 (из YAML)",
        "src_files/printed_books.yaml",
        "src_files/ebooks.yaml",
        "src_files/audiobooks.yaml"
    )
    println("Создано через YAML-DSL: ${lib.size} книг")
    lib.all().forEach { println("  - ${it.title} (${it.category})") }
}

private fun demoDslMarker() {
    val lib = library("Демо @DslMarker") {
        book {
            title = "Чистый код"
            author = "Р. Мартин"
            year = 2008
            pages = 464
        }
        book {
            title = "Война и мир"
            author = "Л. Толстой"
            year = 1869
            pages = 1225
        }
    }
    println("Создано через configure-DSL: ${lib.size} книг")
    lib.all().forEach { println("  - ${it.title} (${it.category})") }
    // @DslMarker в действии: вложенный book { } внутри book { } запрещён компилятором.
    // НЕ РАСКОММЕНТИРОВАТЬ — это демонстрирует ошибку @DslMarker (receiver ambiguity):
    // val bad = library("Bad") {
    //     book {
    //         title = "X"
    //         book { // КОМПИЛЯТОР ЗАПРЕЩАЕТ: 'book' ambiguous между LibraryBuilder и BookBuilder
    //             title = "Y"
    //         }
    //     }
    // }
}

private fun demoRepository() {
    data class Author(override val id: String, val name: String, val country: String) : Identifiable<String>

    class BookEntry(val book: Book) : Identifiable<String> {
        override val id: String = book.isbn ?: error("Book without ISBN cannot be in repository")
    }

    val authors: Repository<String, Author> = Repository()
    authors.add(Author("tolstoy", "Лев Толстой", "Россия"))
    authors.add(Author("martin", "Роберт Мартин", "США"))
    println("Авторы: ${authors.all().map { it.name }}")

    val bookRepo: Repository<String, BookEntry> = Repository()
    loadBooksFromYaml("src_files/printed_books.yaml").forEach { book ->
        bookRepo.add(BookEntry(book))
    }
    println("Книги в репозитории: ${bookRepo.all().map { it.book.title }}")
}

private fun demoBookSource() {
    class PrintedSource : BookSource<PrintedBook> {
        override fun all(): List<PrintedBook> = listOf(
            PrintedBook("Чистый код", "Р. Мартин", 2008, Money(1290.0), 3, pages = 464,
                isbn = "9785916719892", genre = Genre.PROGRAMMING, tags = emptySet())
        )
    }

    fun printAll(source: BookSource<Book>) {
        for (book in source.all()) println("  - ${book.title}")
    }

    printAll(PrintedSource())
    // PrintedSource (BookSource<PrintedBook>) передаётся туда, где ожидается BookSource<Book> — компилируется только благодаря out T (ковариация).
}

private fun demoOfType() {
    val lib = Library("ofType demo")
    lib.addBook(PrintedBook("Чистый код", "Р. Мартин", 2008, Money(1290.0), 3, pages = 464, isbn = "9785916719892", genre = Genre.PROGRAMMING, tags = emptySet()))
    lib.addBook(EBook("Война и мир", "Л. Толстой", 1869, Money(500.0), pages = 1300, sizeMb = 3.5, format = "EPUB", isbn = "9785000560332", genre = Genre.FICTION))
    lib.addBook(AudioBook("Мастер и Маргарита", "М. Булгаков", 1967, Money(800.0), durationMinutes = 720, narrator = "Иван Иванов"))
    println("EBook: ${lib.ofType<EBook>().map { it.title }}")
    println("PrintedBook: ${lib.ofType<PrintedBook>().map { it.title }}")
    println("AudioBook: ${lib.ofType<AudioBook>().map { it.title }}")
    println("Тип T: ${lib.ofType<EBook>()::class.simpleName}")
    // reified T — тип T доступен в runtime (без erasure). Без reified filterIsInstance<T>() не скомпилировалось бы.
}

private fun demoIsbnExtensions() {
    val raw = "978-0-13-468599-1"
    val clean = raw.cleanIsbn()
    println("raw: $raw")
    println("cleanIsbn(): $clean")
    println("isbnClean(raw): ${isbnClean(raw)}")  // сравнение с обычной функцией
    println("isValidIsbn13(): ${raw.isValidIsbn13()}")
    println("isbnPrefix: ${clean.isbnPrefix}")
    println("short.isbnPrefix: ${"12".isbnPrefix}")  // null (длина < 3)
    val bad = "978-5-17-118363-2"
    println("bad.isValidIsbn13(): ${bad.isValidIsbn13()}")  // false
    // Extension читается как метод объекта — короче и привычнее, особенно в IDE-автодополнении.
}

private fun demoOperators() {
    val lib = Library("Операторы")
    loadBooksFromYaml("src_files/printed_books.yaml").forEach { lib + it }
    loadBooksFromYaml("src_files/ebooks.yaml").forEach { lib + it }
    println("Размер: ${lib.size}")
    println("По ISBN: ${lib["9785916719892"]?.title}")
    println("book1 in lib: ${lib["9785916719892"]!! in lib}")
    println("Книги:")
    for (b in lib) println("  ${b.title}")
}

private fun demoByAuthor() {
    val lib = Library("byAuthor")
    loadBooksFromYaml("src_files/printed_books.yaml").forEach { lib.addBook(it) }
    loadBooksFromYaml("src_files/audiobooks.yaml").forEach { lib.addBook(it) }
    val tolstoy = lib byAuthor "Л. Толстой"
    println("Толстой: ${tolstoy.map { it.title }}")
    val martin = lib byAuthor "Р. Мартин"
    println("Мартин: ${martin.map { it.title }}")
}