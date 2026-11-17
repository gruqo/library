package com.library.model

import com.library.common.TITLE_SYMBOL_MAX
import com.library.common.YEAR_END
import com.library.common.YEAR_START
import com.library.util.authorSplit
import com.library.util.digitsSum
import com.library.util.priceFormat

abstract class Book(
    val title: String,
    val author: String,
    val year: Int,
    val price: Money,
    initialCopies: Int,
    val isbn: String? = null,
    val edition: Int? = null,
    val originalLanguage: String? = null,
    val translator: String? = null,
    val genre: Genre = Genre.OTHER
) : Loanable {
    var copiesInStock: Int = initialCopies
        protected set // protected: менять можно внутри Book и в наследниках (нужно EBook.lend())
    var totalLoans: Int = 0
        protected set

    companion object {
        const val MIN_YEAR: Int = YEAR_START
        const val MAX_YEAR: Int = YEAR_END

        fun isValidYear(year: Int): Boolean = year in MIN_YEAR..MAX_YEAR
    }

    abstract val category: String // каждый подкласс задаёт свою

    init {
        require(title.isNotBlank()) { "Название не может быть пустым" }
        require(Book.isValidYear(year)) { "Год $year вне допустимого диапазона" }
        require(price.amount >= 0) { "Цена не может быть отрицательной" }
        require(initialCopies >= 0) { "Количество экземпляров не может быть отрицательным" }
    }

    override val isAvailable: Boolean
        get() = copiesInStock > 0

    val shortTitle: String
        get() {
            val trimmed =
                if (title.length > TITLE_SYMBOL_MAX) title.take(TITLE_SYMBOL_MAX - 3) + "..." else title
            if (trimmed.isBlank()) return trimmed
            return if (trimmed == trimmed.uppercase()) {
                trimmed.split(" ").joinToString(" ") { word ->
                    word.lowercase().replaceFirstChar { it.uppercase() }
                }
            } else trimmed
        }

    override fun lend(): LoanResult {
        if (copiesInStock <= 0) return LoanResult.NotAvailable(0)
        copiesInStock--
        totalLoans++
        return LoanResult.Success
    }

    override fun returnCopy() {
        copiesInStock++
    }

    /**
     * Строки о наличии на складе. Вынесены отдельно, потому что у EBook
     * «Int.MAX_VALUE экземпляров» — это на самом деле лицензия без ограничений.
     * emit — куда складывать строки, чтобы они попали и в обычную карточку, и в рамку.
     */
    protected open fun printStock(emit: (String) -> Unit) {
        emit("В наличии:                 $copiesInStock шт.")
        emit("Общая стоимость на складе: ${priceFormat(price.amount * copiesInStock)}")
    }

    open fun printCard(withFancyFrame: Boolean = false) {
        val header = "$category: $shortTitle"
        val body = mutableListOf(
            "Название:                  $shortTitle",
            "Автор:                     ${authorSplit(author)}",
            "Год издания:               $year",
            "Жанр:                      ${genre.emoji} ${genre.displayName}",
            "Цена:                      ${priceFormat(price.amount)}",
        )
        printStock { body.add(it) }
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
            val frameHeader = "--- $header ---"
            println("╔$border╗")
            println("║  ${frameHeader.take(frameWidth - 4)}".padEnd(frameWidth - 1) + "║")
            println("╠$border╣")
            for (line in body) {
                println("║  ${line.take(frameWidth - 4)}".padEnd(frameWidth - 1) + "║")
            }
            println("╚$border╝")
        } else {
            println("=== $header ===")
            body.forEach { println(it) }
            println("=".repeat(header.length + 8))
        }
    }
}
