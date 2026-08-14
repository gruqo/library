package com.library.model

class IndexedLibrary(val name: String) :
    MutableMap<String, Book> by mutableMapOf() {
    fun addBook(book: Book) {
        val isbn = book.isbn ?: error("Book without ISBN cannot be added to indexed library")
        require(isbn !in this) { "Duplicate ISBN: $isbn" }
        this[isbn] = book
    }
    fun byAuthor(author: String): List<Book> =
        values.filter { it.author == author }
}