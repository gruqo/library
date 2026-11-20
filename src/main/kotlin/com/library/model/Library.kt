package com.library.model

class Library(val name: String, rows: Int = 5, cols: Int = 10) {
    private val books: MutableList<Book> = mutableListOf()
    private val byIsbn = mutableMapOf<String, Book>()

    private val reservationQueue: ArrayDeque<Pair<String, Book>> = ArrayDeque()

    fun reserve(userName: String, book: Book) {
        reservationQueue.addLast(userName to book)
        println("$userName поставлен в очередь на «${book.title}»")
    }
    fun nextReservation(): Pair<String, Book>? = reservationQueue.removeFirstOrNull()
    fun queueSize(): Int = reservationQueue.size

    fun addBook(book: Book) {
        book.isbn?.let { isbn ->
            require(isbn !in byIsbn) { "Книга с ISBN $isbn уже есть в каталоге" }
            byIsbn[isbn] = book
        }
        books.add(book)
    }

    fun findByIsbn(isbn: String): Book? = byIsbn[isbn]
    fun hasIsbn(isbn: String): Boolean = isbn in byIsbn

    fun removeBook(book: Book): Boolean {
        val removed = books.remove(book)
        if (removed) book.isbn?.let { byIsbn.remove(it) }
        return removed
    }
    val size: Int get() = books.size
    fun all(): List<Book> = books.toList()
    override fun toString(): String = "Библиотека «$name» ($size книг)"

    fun byGenre(): Map<Genre, List<Book>> = books.groupBy { it.genre }
    fun countByGenre(): Map<Genre, Int> = books.groupBy { it.genre }.mapValues { (_, list) -> list.size }
    fun countByGenre2(): Map<Genre, Int> = books.groupingBy { it.genre }.eachCount()

    private val shelves: Array<Array<String?>> = Array(rows) { arrayOfNulls(cols) }
    fun place(book: Book, row: Int, col: Int): Boolean {
        require(row in shelves.indices && col in shelves[row].indices)
        if (shelves[row][col] != null) return false
        shelves[row][col] = book.isbn ?: book.title
        return true
    }
    fun printShelves() {
        for ((rowIdx, row) in shelves.withIndex()) {
            print("Ряд $rowIdx: ")
            for (cell in row) print(if (cell == null) ". " else "□ ")
            println()
        }
    }

    fun totalCopies(): Int = books.sumOf { it.copiesInStock }

    fun averagePrice(): Double =
        if (books.isEmpty()) 0.0
        else books.map { it.price.amount }.average()

    fun hasAvailable(): Boolean = books.any { it.isAvailable }

    fun unavailableCount(): Int = books.count { !it.isAvailable }

    fun ebooks(): List<EBook> = books.filterIsInstance<EBook>()

    fun audioBooks(): List<AudioBook> = books.filterIsInstance<AudioBook>()

    fun totalAudioMinutes(): Int = audioBooks().sumOf { it.durationMinutes }

    fun topByLoans(n: Int): List<Book> =
        books.sortedByDescending { it.totalLoans }.take(n)

    fun topThickest(n: Int): List<PrintedBook> =
        books.filterIsInstance<PrintedBook>()
            .sortedByDescending { it.pages }
            .take(n)

    fun topAuthors(n: Int): List<Pair<String, Int>> =
        books.groupBy { it.author }
            .map { (author, list) -> author to list.size }
            .sortedByDescending { it.second }
            .take(n)

    fun totalCatalogValue(): Double =
        books.fold(0.0) { acc, book -> acc + book.price.amount * book.copiesInStock }

    fun totalCatalogValue2(): Double =
        books.sumOf { it.price.amount * it.copiesInStock }

    fun allPrintedBooks(): List<PrintedBook> = books.filterIsInstance<PrintedBook>()
}
