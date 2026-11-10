package com.library.io

import com.library.model.PrintedBook
import com.library.util.cleanIsbn
import com.library.util.describeIsbn
import com.library.util.isValidIsbn

private fun readOptionalString(prompt: String): String? {
    print(prompt)
    return readln().ifBlank { null }
}

private fun readIntOrNull(prompt: String, error: String): Int? {
    print(prompt)
    return readln().toIntOrNull() ?: null.also { println("  Ошибка: $error\n") }
}

private fun readDoubleOrNull(prompt: String, error: String): Double? {
    print(prompt)
    return readln().toDoubleOrNull() ?: null.also { println("  Ошибка: $error\n") }
}

private fun readEdition(): Int? {
    print("Введите номер издания: ")
    return readln().toIntOrNull()?.takeIf { it > 0 }
}

private fun readIsbn(): String? {
    print("Введите ISBN (Enter если нет ISBN): ")
    val cleaned = cleanIsbn(readln())
    val isbn = if (cleaned.isNotBlank() && isValidIsbn(cleaned)) cleaned else null
    println(describeIsbn(isbn))
    return isbn
}

fun readBookData(): PrintedBook? {
    println("Добро пожаловать в библиотеку!\n")

    print("Введите название книги: ")
    val title = readln()

    print("Введите автора: ")
    val author = readln()

    val originalLanguage = readOptionalString("Введите язык оригинала (Enter если оригинал на русском): ")
    val translator = readOptionalString("Введите переводчика: ")
    val edition = readEdition()

    val year = readIntOrNull("Введите год издания: ", "Год должен быть числом") ?: return null
    val pages = readIntOrNull("Введите количество страниц: ", "Количество страниц должно быть числом") ?: return null
    val price = readDoubleOrNull("Введите цену (руб.): ", "Цена должна быть числом") ?: return null
    val copies = readIntOrNull("Введите количество экземпляров: ", "Количество экземпляров должно быть числом") ?: return null
    val isbn = readIsbn()

    return PrintedBook(
        title = title,
        author = author,
        year = year,
        price = price,
        initialCopies = copies,
        pages = pages,
        isbn = isbn,
        edition = edition,
        originalLanguage = originalLanguage,
        translator = translator
    )
}
