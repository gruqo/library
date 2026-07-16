package com.library.model

class EBook(
    title: String, author: String, year: UShort, pages: UShort, price: Money,
    isbn: String? = null, val format: String, val sizeMb: Double, genre: Genre = Genre.OTHER
) : Book(title, author, year, pages, price, initialCopies = Int.MAX_VALUE, isbn = isbn, genre = genre) {

    constructor(
        title: String, author: String, year: Int, price: Money, pages: Int, sizeMb: Double,
        format: String, isbn: String?, genre: Genre
    ) : this(
        title = title,
        author = author,
        year = year.toUShort(),
        price = price,
        pages = pages.toUShort(),
        sizeMb = sizeMb,
        format = format,
        isbn = isbn,
        genre = genre)

    override val category = "Электронная книга"

    override fun lend(): LoanResult {
        totalLoans ++
        return LoanResult.Success
    }

    override fun returnCopy() {
    }

    override fun printCard() {
        super.printCard()
        println("Формат: $format, $sizeMb MB")
    }
}
