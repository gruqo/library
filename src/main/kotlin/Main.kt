package com.prosoft.webinar02.homework

fun main() {
    println("Добро пожаловать в библиотеку!\n")

    print("Введите название книги: ")
    val title = readln()

    print("Введите автора: ")
    val author = readln()

    print("Введите год издания: ")
    val year = readln().toInt()

    print("Введите количество страниц: ")
    val pages = readln().toInt()

    print("Введите цену (руб.): ")
    val price = readln().toDouble()

    print("Введите количество экземпляров: ")
    val copiesInStock = readln().toInt()

    //  Учёт выдач
    var totalLoans: Int = 0
    var currentlyOnHand: Int = 0

    fun printCart() {
        println("""
        
            |=== КАРТОЧКА КНИГИ ===
            |Название:                  $title
            |Автор:                     $author
            |Год издания:               $year
            |Кол-во страниц:            $pages
            |Цена:                      ${"%.2f".format(price)} руб.
            |В наличии:                 $copiesInStock шт.
            |Общая стоимость на складе: ${"%.2f".format(price * copiesInStock)} руб.
            |
            |---------------------
            |
            | Выдача №1: всего выдач ${++totalLoans}, на руках ${++currentlyOnHand}, на полке ${copiesInStock - currentlyOnHand}
            | Выдача №2: всего выдач ${++totalLoans}, на руках ${++currentlyOnHand}, на полке ${copiesInStock - currentlyOnHand}
            | Выдача №3: всего выдач ${++totalLoans}, на руках ${++currentlyOnHand}, на полке ${copiesInStock - currentlyOnHand}
            | Возврат:   всего выдач ${totalLoans}, на руках ${--currentlyOnHand}, на полке ${copiesInStock - currentlyOnHand}
            |
            |=====================
        """.trimMargin())
    }

    printCart()
}
