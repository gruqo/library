package com.library.model

import com.library.util.pageCategorize

class PrintedBook(
    title: String,
    author: String,
    year: Int,
    price: Money,
    initialCopies: Int,
    val pages: Int,
    isbn: String? = null,
    edition: Int? = null,
    originalLanguage: String? = null,
    translator: String? = null,
    genre: Genre = Genre.OTHER,
    tags: Set<String> = emptySet()
) : Book(
    title = title,
    author = author,
    year = year,
    price = price,
    initialCopies = initialCopies,
    isbn = isbn,
    edition = edition,
    originalLanguage = originalLanguage,
    translator = translator,
    genre = genre,
    tags = tags
) {
    override val category = "Печатная книга"

    init {
        require(pages > 0) { "Страниц должно быть положительно" }
    }

    override fun printCard(withFancyFrame: Boolean) {
        super.printCard(withFancyFrame)
        println("Страниц: $pages (${pageCategorize(pages)})")
    }
}
