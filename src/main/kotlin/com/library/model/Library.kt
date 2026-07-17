package com.library.model

import java.io.StringWriter
import com.library.error.*

class Library(val name: String, rows: Int = 5, cols: Int = 5) {
    internal val books: MutableList<Book> = mutableListOf()
    internal val byIsbn = mutableMapOf<String, Book>()

    private val reservationQueue: ArrayDeque<Pair<String, Book>> = ArrayDeque()
    fun reserve(userName: String, book: Book) {
        reservationQueue.addLast(userName to book)
        println("$userName поставлен в очередь на «${book.title}»")
    }
    fun nextReservation(): Pair<String, Book>? = reservationQueue.removeFirstOrNull()
    fun queueSize(): Int = reservationQueue.size


    fun addBook(book: Book) {
        book.isbn?.let { isbn ->
            if (isbn in byIsbn) throw BookAlreadyExistsException(isbn) }
        books.add(book)
        book.isbn?.let { byIsbn[it] = book }
    }

    fun findByIsbn(isbn: String): Book? = byIsbn[isbn]
    fun hasIsbn(isbn: String): Boolean = isbn in byIsbn

    fun getByIsbn(isbn: String): Book =
        byIsbn[isbn] ?: throw BookNotFoundException(isbn)

    fun lendOrThrow(book: Book) {
        when (val result = book.lend()) {
            is LoanResult.Success -> { /* ok */ }is LoanResult.NotAvailable -> throw NotAvailableException(book.title) else -> error("Unexpected result: $result")
        }
    }

    fun removeBook(book: Book): Boolean = books.remove(book)
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
            for (cell in row) print(if (cell == null) ".%-8s".format("") else "%-8s".format(cell.take(8)))
            println()
        }
    }

    fun totalCopies(): Int = books
        .filter { it.copiesInStock != Int.MAX_VALUE }
        .sumOf { it.copiesInStock }

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

    fun totalCatalogValue1(): Double =
        books.fold(0.0) { acc, book -> acc + book.price.amount * book.copiesInStock }

    fun totalCatalogValue2(): Double =
        books.sumOf { it.price.amount * it.copiesInStock }

    fun allPrintedBooks(): List<PrintedBook> = books.filterIsInstance<PrintedBook>()

    fun search(predicate: (Book) -> Boolean): List<Book> = books.filter(predicate)

    fun forEachBook(action: (Book) -> Unit) {
        books.forEach(action)
    }

    fun <R> mapBooks(transform: (Book) -> R): List<R> = books.map(transform)

    fun exportToString(): String = StringWriter().use { writer ->
        writer.write("# Каталог: $name\n")
        for (book in all()) {
            writer.write("${book.isbn ?: "no-isbn"}\t${book.title}\t${book.author}\t${book.year}\n")
        }
        writer.toString()
   }

}
