package com.library.model

data class Money(val amount: Double, val currency: String = "RUB") {
    init {
        require(amount >= 0) { "Сумма не может быть отрицательной" }
    }
    operator fun plus(other: Money): Money {
        require(currency == other.currency) { "Нельзя складывать $currency и ${other.currency}" }
        return Money(amount + other.amount, currency)
    }
    override fun toString(): String = "%.2f %s".format(amount, currency)
}
