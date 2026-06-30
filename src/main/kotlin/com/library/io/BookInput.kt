package com.library.io

import com.library.model.Book
import com.library.util.describeIsbn
import com.library.util.isbnClean
import com.library.util.isbnValidate

fun readBookData(): Book? {
    println("Добро пожаловать в библиотеку!\n")

    print("Введите название книги: ")
    val titleInput = readln()

    print("Введите автора: ")
    val authorInput = readln()

    print("Введите язык оригинала (Enter если оригинал на русском): ")
    val originalLanguage = readln().ifBlank { null }

    print("Введите переводчика: ")
    val translator = readln().ifBlank { null }

    print("Введите номер издания: ")
    val edition = readln().toIntOrNull()

    print("Введите год издания: ")
    val yearInput = readln().toIntOrNull()
        ?: return null.also { println("Ошибка: год должен быть числом\n") }
    val year = yearInput.toUShort()

    print("Введите количество страниц: ")
    val pagesInput = readln().toUShortOrNull()
        ?: return null.also { println("Ошибка: количество страниц должно быть числом\n") }

    print("Введите цену (руб.): ")
    val priceInput = readln().toDoubleOrNull()
        ?: return null.also { println("Ошибка: цена должна быть числом\n") }

    print("Введите количество экземпляров: ")
    val copiesInStockInput = readln().toIntOrNull()
    if (copiesInStockInput == null) { println("Ошибка: количество экземпляров должно быть числом\n"); return null }

    print("Введите ISBN: ")
    val rawIsbn = readln()
    val cleaned = isbnClean(rawIsbn)

    println( describeIsbn(cleaned))
    val isbnInput = if (cleaned.isNotBlank() && isbnValidate(cleaned)) rawIsbn else null

    return Book(
        title = titleInput,
        author = authorInput,
        year = year,
        pages = pagesInput,
        price = priceInput,
        initialCopies = copiesInStockInput,
        isbn = isbnInput,
        edition = edition,
        originalLanguage = originalLanguage,
        translator = translator
    )
}