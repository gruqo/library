package com.library.model

class AudioBook(
    title: String,
    author: String,
    year: Int,
    price: Money,
    initialCopies: Int,
    val durationMinutes: Int,
    val narrator: String,
    isbn: String? = null,
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
    override val category = "Аудиокнига"

    override fun printCard(withFancyFrame: Boolean) {
        super.printCard(withFancyFrame)
        val hours = durationMinutes / 60
        val mins = durationMinutes % 60
        println("Длительность: ${hours}ч ${mins}мин, читает $narrator")
    }
}
