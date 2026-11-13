package com.library.model

class EBook(
    title: String,
    author: String,
    year: Int,
    price: Double,
    val format: String,
    val sizeMb: Double,
    isbn: String? = null,
    initialCopies: Int = Int.MAX_VALUE
) : Book(
    title = title,
    author = author,
    year = year,
    price = price,
    initialCopies = initialCopies,
    isbn = isbn
) {
    override val category = "Электронная книга"

    override fun lend(): Boolean {
        totalLoans++
        return true
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
