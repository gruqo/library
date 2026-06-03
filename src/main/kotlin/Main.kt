package main

fun main() {
    println("Добро пожаловать в библиотеку!\n")

    print("Введите название книги: ")
    val title = readln()

    print("Введите автора: ")
    val author = readln()

    print("Введите год издания: ")
    val year = readln().toUShort()
    val startYear: UShort = 1450u
    val currentYear: UShort = 2026u

    print("Введите количество страниц: ")
    val pages = readln().toUShort()
    val pagesMin: UShort = 1u
    val pagesMax: UShort = 10_000u
    val thicknessThin: UShort = 50u
    val thicknessStandart: UShort = 200u
    val thicknessBig: UShort = 500u
    val thicknessVeryBig: UShort = 1000u

    print("Введите цену (руб.): ")
    val price = readln().toDouble()
    val priceMin = 0

    print("Введите количество экземпляров: ")
    val copiesInStock = readln().toInt()
    val copiesInStockMin = 0

    val singleMarkerFaled = '✗'
    val singleMarkerAccess = '✓'
    var countError = 0

    println("\nПроверка карточки книги...")

    if (year !in startYear..currentYear) {
        println("$singleMarkerFaled Год $year вне диапазона $startYear..$currentYear")
        countError++
    } else {
        println("$singleMarkerAccess Год: $year")
    }

    if (pages !in pagesMin..pagesMax) {
        println("$singleMarkerFaled Страниц: $pages - должно быть от $pagesMin до $pagesMax")
        countError++
    } else {
        println("$singleMarkerAccess Страниц: $pages")
    }

    if (price < priceMin) {
        println("$singleMarkerFaled Цена $price должна быть положительной")
        countError++
    } else {
        println("$singleMarkerAccess Цена: $price")
    }

    if (copiesInStockMin >= copiesInStock ) {
        println("$singleMarkerFaled Экземпляров: $copiesInStock - должно быть больше нуля")
        countError++
    } else {
        println("$singleMarkerAccess  Экземпляров: $copiesInStock")
    }


    fun thickness() {
        println("\nСтраниц Категория")
        when {pages < thicknessThin -> println("\nСтраниц Категория\n< 50 \"Брошюра\"")
            pages in thicknessThin until thicknessStandart -> println("50..199 \"Стандартная книга\"")
            pages in thicknessStandart until  thicknessBig -> println("200..499 \"Толстая книга\"")
            pages in thicknessBig until thicknessVeryBig-> println("500..999 \"Очень толстая книга\"")
            pages >= thicknessVeryBig -> println("1000 \"Кирпич\"")
        }
    }

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
            |======================
        """.trimMargin())
    }

    println()
    if (countError == 0) {
        println("Итог: принято в каталог")
        thickness()
        printCart()
    } else {
        println("Итог: НЕ принято в каталог (количество проблем: $countError )")
    }

    /* Задача 3.
    // используется for, т.к. известно число итераций и работает break
    */
    print("\nСколько раз выдать книгу? ")
    val requestsCount = readln().toInt()
    var currentlyOnHand = 0
    var totalLoans = 0

    for (i in 1..requestsCount) {
        if (currentlyOnHand < copiesInStock) {
            currentlyOnHand++
            totalLoans++
            println("Выдача $i: на руках $currentlyOnHand из $copiesInStock")
        } else {
            println("Выдача $i: отказ — все экземпляры заняты")
            break
        }
    }

    println("""

            |=== ФИНАЛЬНАЯ СТАТИСТИКА ВЫДАЧ КНИГИ "$title" ===
            |Всего выдач:               $totalLoans
            |Сколько на руках:          $currentlyOnHand
            |Сколько свободно:          ${copiesInStock - currentlyOnHand}
            |======================
    """.trimMargin())

    // Задача 4 (★ бонус). Поиск года
    var attempts = 0
    print("\nИнтерактивная игра \"Поиск года\"")
    while (true) {
        print("\nВведите число (или 0 для выхода): ")
        val userGuess = readln().toUShort()

        when (userGuess.toInt()) {
            0 -> {
                println("Выход из игры. Загаданный год: $year")
                break
            }
            year.toInt() -> {
                attempts++
                println("Угадали! Попыток: $attempts")
                break
            }
            else -> {
                attempts++
                if (userGuess < year) {
                    println("Слишком рано")
                } else {
                    println("Слишком поздно")
                }
            }
        }
    }
}