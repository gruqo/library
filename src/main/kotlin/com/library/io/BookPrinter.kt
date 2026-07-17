package com.library.io

import com.library.model.*
import com.library.util.SINGLE_MARKER_ACCESS
import com.library.util.pageCategorize
import com.library.util.priceFormat

fun printCard(book: Book) {
    println(
        """
        |
        |=== КАРТОЧКА КНИГИ ===
        |Название:                  ${book.title}
        |Автор:                     ${book.author}
        |Год издания:               ${book.year}
        |Кол-во страниц:            ${book.pages} (${pageCategorize(book.pages)})
        |Цена:                      ${priceFormat(book.price.amount)}
        |В наличии:                 ${book.copiesInStock} шт.
        |Общая стоимость на складе: ${priceFormat(book.price.amount * book.copiesInStock)}
    """.trimMargin()
    )

    println("Жанр:                      ${book.genre.emoji} ${book.genre.displayName}")
    book.originalLanguage?.let { println("Язык оригинала:            $it") }
    book.translator?.let { println("Переводчик:                $it") }
    book.edition?.let { println("Издание:                   $it") }
    book.isbn?.let { println("ISBN:                      $it") }
    if (book.tags.isNotEmpty()) println("Тэги:                      ${book.tags.joinToString(", ")}")

    when (book) {
        is PrintedBook -> println("Страниц: ${book.pages}")
        is EBook -> println("Формат: ${book.format}, ${book.sizeMb} MB")
        is AudioBook -> {
            val hours = book.durationMinutes / 60
            val mins = book.durationMinutes % 60
            println("Длительность: ${hours}ч ${mins}мин, читает ${book.narrator}")
        }
    }

    println(
        """
        |$SINGLE_MARKER_ACCESS Книга успешно добавлена!
        |======================
    """.trimMargin()
    )
}