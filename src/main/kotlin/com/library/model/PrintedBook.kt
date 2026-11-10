package com.library.model

import com.library.util.pageCategorize

class PrintedBook(
    title: String,
    author: String,
    year: Int,
    price: Double,
    initialCopies: Int,
    pages: Int,
    isbn: String? = null,
    edition: Int? = null,
    originalLanguage: String? = null,
    translator: String? = null
) : Book(
    title = title,
    author = author,
    year = year,
    pages = pages,
    price = price,
    initialCopies = initialCopies,
    isbn = isbn,
    edition = edition,
    originalLanguage = originalLanguage,
    translator = translator
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
