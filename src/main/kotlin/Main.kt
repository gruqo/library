package main

import kotlin.ranges.contains

// ========== COMMON VARS ==========
const val titleSimbolMax = 30
const val yearStart: Int = 1450
const val yearCurrent: Int = 2026

const val pagesThicknessThin: Int = 50
const val pagesThicknessStandard: Int = 200
const val pagesThicknessBig: Int = 500
const val pagesThicknessVeryBig: Int = 1000

const val isbn13Quantity = 13
const val singleMarkerAccess = '✓'
const val singleMarkerFailed = '✗'

fun main() {
    println("Добро пожаловать в библиотеку!\n")

    print("Введите название книги: ")
    val titleInput = readln()
    val titleTrimm = if (titleInput.length > 30) titleInput.take(27) + "..." else titleInput
    val titleFinal = when {
        titleTrimm.isBlank() -> titleInput
        titleTrimm.any { it.isLetter() } && titleTrimm == titleTrimm.uppercase() -> {
            println("Название всё капсом, переведём в Title Case")
            titleTrimm.split(" ")
                .filter { it.isNotBlank() }
                .joinToString(" ") { word ->
                    word.lowercase().replaceFirstChar { it.uppercase() }
                }
        }
        else -> titleTrimm
    }

    print("Введите автора: ")
    val authorParts = readln().split(" ").filter { it.isNotBlank() }
    val author = when (authorParts.size) {
        0 -> "Автор неизвестен"
        1 -> authorParts[0].replaceFirstChar { it.uppercase() }
        2 -> "${authorParts[0].first().uppercase()}. ${authorParts[1].replaceFirstChar { it.uppercase() }}"
        else -> "${authorParts[0].first().uppercase()}. ${authorParts[1].first().uppercase()}. ${authorParts.last().replaceFirstChar { it.uppercase() }}"
    }

    print("Введите год издания: ")
    val yearInput = readln()
    val year = yearInput.toIntOrNull() ?: error("Год должен быть числом, а не «$yearInput»")
    val startYear = 1450
    val currentYear = 2026

    print("Введите количество страниц: ")
    val pages = readln().toUShort()
    val pagesMin: UShort = 1u
    val pagesMax: UShort = 10_000u
    val thicknessThin: UShort = 50u
    val thicknessStandard: UShort = 200u
    val thicknessBig: UShort = 500u
    val thicknessVeryBig: UShort = 1000u

    print("Введите цену (руб.): ")
    val price = readln().toDouble()
    val priceMin = 0

    print("Введите количество экземпляров: ")
    val copiesInStock = readln().toInt()
    val copiesInStockMin = 0

    val singleMarkerAccess = '✓'
    val singleMarkerFailed = '✗'
    var countError = 0

    println("\nПроверка карточки книги...")

    if (year !in startYear..currentYear) {
        println("$singleMarkerFailed Год $year вне диапазона $startYear..$currentYear")
        countError++
    } else {
        println("$singleMarkerAccess Год: $year")
    }
    val yearFormated = year.toUShort()

    if (pages !in pagesMin..pagesMax) {
        println("$singleMarkerFailed Страниц: $pages - должно быть от $pagesMin до $pagesMax")
        countError++
    } else {
        println("$singleMarkerAccess Страниц: $pages")
    }

    if (price < priceMin) {
        println("$singleMarkerFailed Цена $price должна быть положительной")
        countError++
    } else {
        println("$singleMarkerAccess Цена: $price")
    }

    if (copiesInStockMin >= copiesInStock) {
        println("$singleMarkerFailed Экземпляров: $copiesInStock - должно быть больше нуля")
        countError++
    } else {
        println("$singleMarkerAccess  Экземпляров: $copiesInStock")
    }

    var isbnClean: String
    while (true) {
        print("Введите ISBN: ")
        val isbn = readln()
        isbnClean = isbn.replace("-", "").replace(" ", "")

        if (isbnClean.length == 13) {
            val allDigits = isbnClean.all { it.isDigit() }
            if (!allDigits) {
                println("Ошибка: ISBN должен содержать только цифры")
                continue
            }

            var sumEven = 0
            var sumOdd = 0
            for (i in isbnClean.indices) {
                val digit = isbnClean[i].digitToInt()

                if (i % 2 == 0) {
                    sumEven += digit
                } else {
                    sumOdd += digit
                }
            }

            val total = sumEven + sumOdd * 3
            val isValid = total % 10 == 0
            val controlSummMarker = if (isValid) singleMarkerAccess else singleMarkerFailed
            val controlSummMess = if (isValid) "(делится на 10)" else "(не делится на 10)"
            val isbnValidateMess = if (isValid) "валиден" else "не валиден"

            println("""
                |======== ISBN =======
                |Очищенный:                 $isbnClean
                |Длина:                     $singleMarkerAccess   ${isbnClean.length}
                |Все цифры:                 $singleMarkerAccess
                |Контрольная сумма:         $controlSummMarker   $total $controlSummMess
                |ISBN $isbnValidateMess.
                |=====================
            """.trimMargin())

            if (!isValid) {
                countError++
            }
            break
        } else {
            println("Введенный ISBN не соответствует формату ISBN-13")
        }
    }

    fun thickness() {
        println("\nСтраниц Категория")
        when {
            pages < thicknessThin -> println("< 50 \"Брошюра\"")
            pages in thicknessThin until thicknessStandard -> println("50..199 \"Стандартная книга\"")
            pages in thicknessStandard until thicknessBig -> println("200..499 \"Толстая книга\"")
            pages in thicknessBig until thicknessVeryBig -> println("500..999 \"Очень толстая книга\"")
            pages >= thicknessVeryBig -> println("1000 \"Кирпич\"")
        }
    }

    fun printCart(author: String) {
        println("""

            |=== КАРТОЧКА КНИГИ ===
            |Название:                  $titleFinal
            |Автор:                     $author
            |Год издания:               $yearFormated
            |Кол-во страниц:            $pages
            |Цена:                      ${"%.2f".format(price)} руб.
            |В наличии:                 $copiesInStock шт.
            |ISBN:                      $isbnClean
            |Общая стоимость на складе: ${"%.2f".format(price * copiesInStock)} руб.
            |======================
        """.trimMargin())
    }

    if (countError == 0) {
        println("\nИтог: принято в каталог")
        thickness()
        printCart(author)
    } else {
        println("\nИтог: НЕ принято в каталог (количество проблем: $countError )")
    }

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

            |=== ФИНАЛЬНАЯ СТАТИСТИКА ВЫДАЧ КНИГИ "$titleTrimm" ===
            |Всего выдач:               $totalLoans
            |Сколько на руках:          $currentlyOnHand
            |Сколько свободно:          ${copiesInStock - currentlyOnHand}
            |======================
    """.trimMargin())

    var attempts = 0
    print("\nИнтерактивная игра \"Поиск года\"")
    while (true) {
        print("\nВведите число (или 0 для выхода): ")
        val userGuess = readln().toUShort()

        when (userGuess.toInt()) {
            0 -> {
                println("Выход из игры. Загаданный год: $yearFormated")
                break
            }
            yearFormated.toInt() -> {
                attempts++
                println("Угадали! Попыток: $attempts")
                break
            }
            else -> {
                attempts++
                if (userGuess < yearFormated) {
                    println("Слишком рано")
                } else {
                    println("Слишком поздно")
                }
            }
        }
    }
}
