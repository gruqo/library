package com.library.model

import com.library.common.TITLE_SIMBOL_MAX
import com.library.common.YEAR_END
import com.library.common.YEAR_START
import com.library.util.authorSplit
import com.library.util.digitsSum
import com.library.util.pageCategorize
import com.library.util.priceFormat

class Book(
    val title: String,
    val author: String,
    val year: Int,
    val pages: Int,
    val price: Double,
    initialCopies: Int,
    val isbn: String? = null,
    val edition: Int? = null,
    val originalLanguage: String? = null,
    val translator: String? = null
) {
    var copiesInStock: Int = initialCopies
        private set // менять можно только изнутри класса
    var totalLoans: Int = 0
        private set
    init {
        require(title.isNotBlank()) { "Название не может быть пустым" }
        require(year in YEAR_START..YEAR_END) { "Год $year вне допустимого диапазона" }
        require(pages > 0) { "Страниц должно быть положительно, а не $pages" }
        require(price >= 0) { "Цена не может быть отрицательной" }
        require(initialCopies >= 0) { "Количество экземпляров не может быть отрицательным" }
    }

    constructor(
        title: String, author: String, year: Int, pages: Int, price: Double, copies: Int
    ) : this(
        title, author, year, pages, price, copies,
        isbn = null, edition = null, originalLanguage = null, translator = null
    )

    val isAvailable: Boolean
        get() = copiesInStock > 0

    val shortTitle: String
        get() {
            val trimmed =
                if (title.length > TITLE_SIMBOL_MAX) title.take(TITLE_SIMBOL_MAX - 3) + "..." else title
            if (trimmed.isBlank()) return trimmed
            return if (trimmed == trimmed.uppercase()) {
                trimmed.split(" ").joinToString(" ") { word ->
                    word.lowercase().replaceFirstChar { it.uppercase() }
                }
            } else trimmed
        }

    fun lend(): Boolean {
        if (copiesInStock <= 0) return false
        copiesInStock--
        totalLoans++
        return true
    }

    fun returnCopy() {
        copiesInStock++
    }

    fun printCard(withFancyFrame: Boolean = false) {
        val body = mutableListOf(
            "Название:                  $shortTitle",
            "Автор:                     ${authorSplit(author)}",
            "Год издания:               $year",
            "Кол-во страниц:            $pages (${pageCategorize(pages)})",
            "Цена:                      ${priceFormat(price)}",
            "В наличии:                 $copiesInStock шт.",
            "Общая стоимость на складе: ${priceFormat(price * copiesInStock)}",
        )
        originalLanguage?.let { body.add("Язык оригинала:            $it") }
        translator?.let { body.add("Переводчик:                $it") }
        body.add("Издание:                   ${edition ?: "не указано"}")
        isbn?.let {
            body.add("ISBN:                      $it")
            body.add("Сумма цифр ISBN:           ${digitsSum(it.toLong())}")
        }

        if (withFancyFrame) {
            val frameWidth = 69
            val border = "═".repeat(frameWidth - 2)
            println("╔$border╗")
            println("║  --- КАРТОЧКА КНИГИ  ---".padEnd(frameWidth - 1) + "║")
            println("╠$border╣")
            for (line in body) {
                println("║  $line".padEnd(frameWidth - 1) + "║")
            }
            println("╚$border╝")
        } else {
            println("=== КАРТОЧКА КНИГИ ===")
            body.forEach { println(it) }
            println("======================")
        }
    }
}
