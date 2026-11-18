package com.library.model

class EBook(
    title: String,
    author: String,
    year: Int,
    price: Money,
    val format: String,
    val sizeMb: Double,
    isbn: String? = null,
    initialCopies: Int = Int.MAX_VALUE,
    genre: Genre = Genre.OTHER
) : Book(
    title = title,
    author = author,
    year = year,
    price = price,
    initialCopies = initialCopies,
    isbn = isbn,
    genre = genre
) {
    override val category = "Электронная книга"

    override fun lend(): LoanResult {
        totalLoans++
        return LoanResult.Success
    }

    override fun returnCopy() {
    }

    override fun printStock(emit: (String) -> Unit) {
        emit("В наличии:                 без ограничений")
    }

    override fun printCard(withFancyFrame: Boolean) {
        super.printCard(withFancyFrame)
        println("Формат: $format, $sizeMb MB")
    }
}
