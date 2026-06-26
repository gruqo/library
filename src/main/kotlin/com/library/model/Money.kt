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

//Домашнее задание 3/6
//Задача 3. data class Money
//Цена пока — Double. Это удобно, но для денег — некорректно. Создайте:
//package com.library.model
//data class Money(val amount: Double, val currency: String = "RUB") {
//    init {
//        require(amount >= 0) { "Сумма не может быть отрицательной" }
//    }
//    operator fun plus(other: Money): Money {
//        require(currency == other.currency) { "Нельзя складывать $currency и ${other.currency}" }
//        return Money(amount + other.amount, currency)
//    }
//    override fun toString(): String = "%.2f %s".format(amount, currency)
//}
//Замените в Book val price: Double на val price: Money.
// Поправьте конструкторы и printCard. Обратите внимание: data class даёт нам бесплатно:
//•equals — две Money(100.0, "RUB") равны, даже если это разные объекты.
// •hashCode — можно класть в HashMap.
//•copy(amount = ...) — создание изменённой копии.
//•toString — мы переопределили, но иначе было бы Money(amount=100.0, currency=RUB).
// •componentN() — destructuring val (amount, currency) = book.price.