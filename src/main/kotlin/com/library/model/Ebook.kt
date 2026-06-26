package com.library.model

class EBook(
    title: String, author: String, year: UShort, pages: UShort, price: Double, isbn: String? = null, val format: String, val sizeMb: Double
) : Book(title, author, year, pages, price, initialCopies = Int.MAX_VALUE, isbn = isbn) {
    constructor(
        title: String, author: String, year: Int, price: Double, format: String, sizeMb: Double
    ) : this(title, author, year.toUShort(), 0u.toUShort(), price, format = format, sizeMb = sizeMb)

    override val category = "Электронная книга"

    override fun lend(): Boolean {
        totalLoans ++
        return true
    }

    override fun returnCopy() {
    }

    override fun printCard() {
        super.printCard()
        println("Формат: $format, $sizeMb MB")
    }
}
