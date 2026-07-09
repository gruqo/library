package com.library.model

class PrintedBook(
    title: String, author: String, year: UShort, pages: UShort, price: Money, copies: Int
) : Book(title, author, year, pages, price, copies) {

    constructor(
        title: String, author: String, year: Int, price: Money, copies: Int, pages: Int
    ) : this(title, author, year.toUShort(), pages.toUShort(), price, copies)

    override val category = "Печатная книга"
    init { require(pages > 0u) { "Страниц должно быть положительно" } }
    override fun printCard() {
        super.printCard()
        println("Страниц: $pages")
    }
}
