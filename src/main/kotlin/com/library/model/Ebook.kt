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
    pages = 0,
    price = price,
    initialCopies = initialCopies,
    isbn = isbn
) {
    override val category = "Электронная книга"

    override fun lend(): Boolean {
        // «бесконечная лицензия»: экземпляры не расходуются, но выдача учитывается
        totalLoans++
        return true
    }

    override fun returnCopy() {
        // у электронной книги нечего возвращать
    }

    override fun printCard(withFancyFrame: Boolean) {
        super.printCard(withFancyFrame)
        println("Формат: $format, $sizeMb MB")
    }
}
