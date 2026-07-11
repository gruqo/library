package com.library.model

class Library(val name: String, rows: Int = 5, cols: Int = 5) {
    private val books: MutableList<Book> = mutableListOf()
    private val byIsbn = mutableMapOf<String, Book>()

    fun addBook(book: Book) {
        books.add(book)
        book.isbn?.let { isbn ->
            require(isbn !in byIsbn) { "Книга с ISBN $isbn уже есть в каталоге" }
            byIsbn[isbn] = book
        }
    }

    fun findByIsbn(isbn: String): Book? = byIsbn[isbn]
    fun hasIsbn(isbn: String): Boolean = isbn in byIsbn

    fun removeBook(book: Book): Boolean = books.remove(book)
    val size: Int get() = books.size
    fun all(): List<Book> = books.toList() // возвращаем КОПИЮ как read-only
    override fun toString(): String = "Библиотека «$name» ($size книг)"

    fun byGenre(): Map<Genre, List<Book>> {
        val result = mutableMapOf<Genre, MutableList<Book>>()
        for (book in books) {
            val list = result.getOrPut(book.genre) { mutableListOf() }
            list.add(book)
        }
        return result // вернётся как Map<Genre, List<Book>> (List — супертип MutableList)
    }

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
}
