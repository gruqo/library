package com.library.dsl

import com.library.model.*

@DslMarker
annotation class LibraryDsl

@LibraryDsl
class BookBuilder {
    var title: String = ""
    var author: String = ""
    var year: Int = 0
    var pages: Int = 100
    var price: Double = 0.0
    var copies: Int = 1
    var isbn: String? = null
    var genre: Genre = Genre.OTHER

    fun build(): PrintedBook {
        require(title.isNotBlank()) { "Book must have title" }
        require(author.isNotBlank()) { "Book must have author" }
        return PrintedBook(title, author, year, Money(price), copies, pages = pages, isbn = isbn, genre = genre, tags = emptySet())
    }
}

@LibraryDsl
class LibraryBuilder(private val name: String) {
    private val books = mutableListOf<Book>()

    fun book(configure: BookBuilder.() -> Unit) {
        val builder = BookBuilder()
        builder.configure()
        books.add(builder.build())
    }

    fun build(): Library = Library(name).apply {
        this@LibraryBuilder.books.forEach { addBook(it) }
    }
}

fun library(name: String, configure: LibraryBuilder.() -> Unit): Library {
    val builder = LibraryBuilder(name)
    builder.configure()
    return builder.build()
}

fun library(name: String, vararg yamlPaths: String): Library {
    val library = Library(name)
    for (path in yamlPaths) {
        loadBooksFromYaml(path).forEach { library.addBook(it) }
    }
    return library
}