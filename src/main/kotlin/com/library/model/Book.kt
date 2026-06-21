package com.library.model

import com.library.util.pageCategorize
import com.library.util.priceFormat

class Book(
    val title: String,
    val author: String,
    val year: UShort,
    val pages: UShort,
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
        require(year in 1450u..2100u) { "Год $year вне допустимого диапазона" }
        require(pages > 0u) { "Страниц должно быть положительно, а не $pages" }
        require(price >= 0) { "Цена не может быть отрицательной" }
        require(initialCopies >= 0) { "Количество экземпляров не может быть отрицательным" }
    }

    constructor(
        title: String, author: String, year: Int, pages: Int, price: Double, copies: Int
    ) : this(
        title, author, year.toUShort(), pages.toUShort(), price, copies,
        isbn = null, edition = null, originalLanguage = null, translator = null
    )

    val isAvailable: Boolean
        get() = copiesInStock > 0

    val shortTitle: String
        get() = if (title.length > 30) title.take(27) + "..." else title

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
        println(
                """
            |
            |=== КАРТОЧКА КНИГИ ===
            |Название:                  $title
            |Автор:                     $author
            |Год издания:               $year
            |Кол-во страниц:            $pages (${pageCategorize(pages)})
            |Цена:                      ${priceFormat(price)}
            |В наличии:                 $copiesInStock шт.
            |Общая стоимость на складе: ${priceFormat(price * copiesInStock)}
        """.trimMargin()
        )
    }
}


//Домашнее задание 2/5
//Задача 2. Методы класса
//Перенесите в класс методы, которые работают с книгой:class Book(...) {
//    // ... поля и init ...
//    val isAvailable: Boolean
//        get() = copiesInStock > 0
//    val shortTitle: String
//        get() = if (title.length > 30) title.take(27) + "..." else title
//    fun lend(): Boolean {
//        if (copiesInStock <= 0) return false
//        copiesInStock--
//        totalLoans++
//        return true
//    }
//    fun returnCopy() {
//        copiesInStock++
//    }
//    fun printCard(withFancyFrame: Boolean = false) {
//// тот же вывод, что и раньше, но теперь использует поля this
//    }
//}
//Обратите внимание:
//❑ isAvailable и shortTitle — это свойства с кастомным геттером, не методы. Считаются «на лету».
//❑ lend()/returnCopy() — команды, меняющие состояние. Они могут это делать,
// потому что находятся внутри класса (где private set не запрет).
// ❑ printCard() — печать, использует поля через this (или просто по имени, this опционален).

