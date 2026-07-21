package com.library.model

class AudioBook(
    title: String,
    author: String,
    year: UShort,
    price: Money,
    isbn: String? = null,
    val durationMinutes: Int,
    val narrator: String,
    initialCopies: Int = Int.MAX_VALUE
) : Book(title, author, year, 0u.toUShort(), price, initialCopies, isbn = isbn) {

    constructor(
        title: String, author: String, year: Int, price: Money,
        durationMinutes: Int, narrator: String, initialCopies: Int = Int.MAX_VALUE
    ) : this(title, author, year.toUShort(), price,
        durationMinutes = durationMinutes, narrator = narrator, initialCopies = initialCopies)

    override val category = "Аудиокнига"
}
