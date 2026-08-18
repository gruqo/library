package com.library.model

import com.library.error.*
import com.library.notify.Notifier
import java.io.FileOutputStream
import java.io.FileNotFoundException
import java.io.StringWriter
import java.nio.file.Files
import java.nio.file.Path
import java.util.zip.ZipFile
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import kotlin.io.path.*

class Library(val name: String, rows: Int = 5, cols: Int = 5, private val notifier: Notifier? = null) {
    internal val books: MutableList<Book> = mutableListOf()
    internal val byIsbn = mutableMapOf<String, Book>()

    private val reservationQueue: ArrayDeque<Pair<String, Book>> = ArrayDeque()
    fun reserve(userName: String, book: Book) {
        reservationQueue.addLast(userName to book)
        println("$userName поставлен в очередь на «${book.title}»")
    }
    fun nextReservation(): Pair<String, Book>? = reservationQueue.removeFirstOrNull()
    fun queueSize(): Int = reservationQueue.size

    //fun addBook(book: Book) { booksList.add(book) }
    fun addBook(book: Book) {
        book.isbn?.let { isbn ->
            if (isbn in byIsbn) throw BookAlreadyExistsException(isbn)
        }
        books.add(book)
        book.isbn?.let { byIsbn[it] = book }
        notifier?.bookAdded(book.title)
        genreCountDelegate.reset()
    }

    operator fun plus(book: Book): Library {
        addBook(book)
        return this
    }



    fun findByIsbn(isbn: String): Book? = byIsbn[isbn]
    operator fun get(isbn: String): Book? = byIsbn[isbn]
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
    operator fun contains(book: Book): Boolean = book in books
    operator fun iterator(): Iterator<Book> = books.iterator()
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

    fun searchHighlighted(query: String): List<String> {
        val regex = Regex(Regex.escape(query), RegexOption.IGNORE_CASE)
        return books
            .filter { regex.containsMatchIn(it.title) || regex.containsMatchIn(it.author) }
            .map { book ->
                val highlightedTitle = regex.replace(book.title) { match -> "[${match.value}]" }
                val highlightedAuthor = regex.replace(book.author) { match -> "[${match.value}]" }
                "$highlightedTitle — $highlightedAuthor"
            }
    }


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

    private val genreCountDelegate = Memoized {
        println("[Memo] Computing genre counts...")
        books.groupingBy { it.genre }.eachCount()
    }
    val genreCount: Map<Genre, Int> by genreCountDelegate

    val authorIndex: Map<String, List<Book>> by lazy {
        println("[Lazy] Computing authorIndex...")
        books.groupBy { it.author }
    }

    lateinit var defaultLoanPolicy: LoanPolicy
    fun lend(book: Book): LoanResult {
        check(::defaultLoanPolicy.isInitialized) { "Loan policy not configured" }
        return book.lend()
    }

}

fun Library.report(): String = buildString {
    val width = 36
    appendLine("╔" + "═".repeat(width + 2) + "╗")
    appendLine("║ " + "Каталог: ${name}".padEnd(width) + " ║")
    appendLine("╠" + "═".repeat(width + 2) + "╣")
    for (book in all().sortedBy { it.title }) {
        appendLine("║ " + book.title.take(30).padEnd(30) + " " + "%5d".format(book.year.toInt()) + " ║")
    }
    appendLine("╠" + "═".repeat(width + 2) + "╣")
    appendLine("║ " + "Всего книг: $size".padEnd(width) + " ║")
    appendLine("╚" + "═".repeat(width + 2) + "╝")
}

fun Library.saveToTsv(path: Path) {
    val header = listOf("type", "title", "author", "year", "price", "copies", "isbn", "genre")

    val rows = books.map { book ->
        listOf(
            book::class.simpleName ?: "Book",book.title,
            book.author,
            book.year.toString(),
            book.price.amount.toString(),
            book.copiesInStock.toString(),
            book.isbn ?: "",
            book.genre.name
        ).joinToString("\t")
    }
    path.writeLines(listOf(header.joinToString("\t")) + rows)
}

fun Library.loadLibraryFromTsv(name: String, path: Path): Library {
    if (!path.exists()) throw CatalogCorruptedException("Файл $path не найден", FileNotFoundException(path.toString()))
    val lines = path.readLines()
    if (lines.isEmpty()) throw CatalogCorruptedException("Файл пуст", Exception())
    val library = Library(name)
    // первая строка — заголовок, пропускаем
    for ((index, line) in lines.drop(1).withIndex()) {
        val cols = line.split("\t")
        try {
            require(cols.size == 8) { "Ожидалось 8 колонок, получено ${cols.size}" }
            val type = cols[0]
            val title = cols[1]
            val author = cols[2]
            val yearStr = cols[3]
            val priceStr = cols[4]
            val copiesStr = cols[5]
            val isbn = cols[6]
            val genreStr = cols[7]
            val book: Book = when (type) {
                "PrintedBook" -> PrintedBook(
                    title,
                    author,
                    yearStr.toInt(),
                    Money(priceStr.toDouble()),
                    copiesStr.toInt(),
                    pages = 100,
                    isbn = isbn.ifBlank { null },
                    genre = Genre.fromString(genreStr),
                    tags = emptySet()
                )
                "EBook" -> EBook(
                    title,
                    author,
                    yearStr.toInt(),
                    Money(priceStr.toDouble()),
                    pages = 100,
                    sizeMb = 0.0,
                    format = "неизвестный",
                    isbn = isbn.ifBlank { null },
                    genre = Genre.fromString(genreStr)
                )
                "AudioBook" -> AudioBook(
                    title,
                    author,
                    yearStr.toInt(),
                    Money(priceStr.toDouble()),
                    durationMinutes = 0,
                    narrator = "неизвестный",
                    initialCopies = copiesStr.toInt()
                )
                else -> error("Неизвестный тип: $type") }
            library.addBook(book) }
        catch (e: Exception) {
            throw CatalogCorruptedException("Ошибка в строке ${index + 2}: «$line»", e) }
    }
    return library
}

fun countLinesContaining(path: Path, substring: String): Int =
   path.useLines { lines -> lines.count {
       it.contains(substring, ignoreCase = true)
   }}


fun Library.saveWithBackup(path: Path) {
    if (path.exists()) {
        val backupDir = path.parent?.resolve("backups") ?: Path("backups")
        backupDir.createDirectories()
        val timestamp = System.currentTimeMillis()
        val backupPath = backupDir.resolve("${path.nameWithoutExtension}-$timestamp.tsv")
        path.copyTo(backupPath, overwrite = false)
        println("Backup: $backupPath")
    }
    saveToTsv(path)
}

fun  listBackups(dir: Path) {
    if (!dir.exists()) {
      println("Нет backup'ов")
      return
    }
    dir.toFile().walkTopDown()
        .filter { it.isFile && it.extension == "tsv" }
        .sortedByDescending { it.lastModified() }
        .forEach { f -> println("${f.name}\t${f.length()} bytes") }
}



fun Library.exportZip(zipPath: Path) {
    ZipOutputStream(FileOutputStream(zipPath.toFile())).use { zip ->
        zip.putNextEntry(ZipEntry("README.txt"))
        zip.write("Каталог: $name\nВсего книг: $size\n".toByteArray())
        zip.closeEntry()
        zip.putNextEntry(ZipEntry("library.tsv"))
        val tempTsv = Files.createTempFile("library", ".tsv")
        saveToTsv(tempTsv)
        zip.write(tempTsv.readBytes())
        zip.closeEntry()
        tempTsv.deleteIfExists()
    }
    println("Архив: ${zipPath.toAbsolutePath()}")
}


fun listZipContents(zipPath: Path) {
    ZipFile(zipPath.toFile()).use { zip ->
        for (entry in zip.entries()) {
            println("${entry.name}\t${entry.size} bytes")
        }
    }
}

inline fun <reified T : Book> Library.ofType(): List<T> =
    all().filterIsInstance<T>()

infix fun Library.byAuthor(author: String): List<Book> =
    search { it.author == author }
