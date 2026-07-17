package com.library.model

abstract class Book(
    val title: String,
    val author: String,
    val year: UShort,
    val pages: UShort,
    val price: Money,
    initialCopies: Int,
    val isbn: String? = null,
    val edition: Int? = null,
    val originalLanguage: String? = null,
    val translator: String? = null,
    val genre: Genre = Genre.OTHER,
    val tags: Set<String> = emptySet()
) : Loanable {
    var copiesInStock: Int = initialCopies
        protected set
    var totalLoans: Int = 0
        protected set

    companion object {
        const val MIN_YEAR = 1450
        const val MAX_YEAR = 2100
        fun isValidYear(year: Int): Boolean = year in MIN_YEAR..MAX_YEAR
    }

    abstract val category: String

    init {
        require(title.isNotBlank()) { "Название не может быть пустым" }
        require(isValidYear(year.toInt())) { "Год $year вне диапазона" }
        require(pages >= 0u.toUShort()) { "Страниц должно быть положительно, а не $pages" }
        require(price.amount >= 0) { "Цена не может быть отрицательной" }
        require(initialCopies >= 0) { "Количество экземпляров не может быть отрицательным" }
    }

    override val isAvailable: Boolean
        get() = copiesInStock > 0

    override fun lend(): LoanResult {
        if (copiesInStock <= 0) return LoanResult.NotAvailable(0)
        copiesInStock--
        totalLoans++
        return LoanResult.Success
    }

    override fun returnCopy() {
        copiesInStock++
    }

    override fun toString(): String = "$title — $author, $year, $pages стр."

}

