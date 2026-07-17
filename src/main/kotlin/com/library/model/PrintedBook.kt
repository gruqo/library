package com.library.model

class PrintedBook(
    title: String, author: String, year: UShort, pages: UShort, price: Money, copies: Int,
    isbn: String?, genre: Genre, tags: Set<String>
) : Book(title, author, year, pages, price, copies,
    isbn = isbn, genre = genre, tags = tags), Comparable<PrintedBook> {
    override fun compareTo(other: PrintedBook): Int = pages.compareTo(other.pages)

    constructor(
        title: String, author: String, year: Int, price: Money, copies: Int, pages: Int,
        isbn: String?, genre: Genre, tags: Set<String>
    ) : this(
        title = title,
        author = author,
        year = year.toUShort(),
        price = price,
        copies = copies,
        pages = pages.toUShort(),
        isbn = isbn,
        genre = genre,
        tags = tags)

    override val category = "Печатная книга"
    init { require(pages > 0u) { "Страниц должно быть положительно" } }
}
