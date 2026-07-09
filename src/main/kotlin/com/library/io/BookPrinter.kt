package com.library.io

import com.library.util.SINGLE_MARKER_ACCESS
import com.library.util.pageCategorize
import com.library.util.priceFormat

fun printCard(
    title: String, author: String, language: String?, translator: String?, edition: Int?, year: UShort, pages: UShort, price: Double,
    copies: Int, isbn: String?
) {
    println(
        """
        |
        |=== КАРТОЧКА КНИГИ ===
        |Название:                  $title
        |Автор:                     $author
        |Год издания:               $year
        |Кол-во страниц:            $pages (${pageCategorize(pages)})
        |Цена:                      ${priceFormat(price)}
        |В наличии:                 $copies шт.
        |Общая стоимость на складе: ${priceFormat(price * copies)}
    """.trimMargin()
    )

    language?.let { println("Язык оригинала:            $it") }
    translator?.let { println("Переводчик:                $it") }
    edition?.let { println("Издание:                   $it") }
    isbn?.let { println("ISBN:                      $it") }

    println(
        """
        |
        |${SINGLE_MARKER_ACCESS} Книга успешно добавлена!
        |======================
    """.trimMargin()
    )
}