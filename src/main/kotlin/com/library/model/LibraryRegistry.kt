package com.library.model

object LibraryRegistry {
    private val books = mutableListOf<Book>() // мы используем List, который ещё не проходили
    fun register(book: Book) {
        books.add(book)
        println("Добавлено в каталог: ${book.title}")
    }
    fun count(): Int = books.size
    fun summary(): String =
        "Каталог: ${books.size} книг, всего выдач ${books.sumOf { it.totalLoans }}"
}
