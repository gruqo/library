package main

fun main() {
    println("Добро пожаловать в библиотеку!\n")

    print("Введите название книги: ")
    val titleInput = readln()
    val titleTrimm = if (titleInput.length > 30) titleInput.take(27) + "..." else titleInput

    val titleFinal = when {
        titleTrimm.isBlank() -> titleTrimm
        titleTrimm == titleTrimm.uppercase() -> {
            println("Название всё капсом, переведём в Title Case")
            titleTrimm.split(" ").joinToString(" ") { word ->
                word.lowercase().replaceFirstChar { it.uppercase() }
            }
        }
        else -> titleTrimm
    }

    print("Введите автора: ")
    val authorInput = readln().split(" ")

    val autorName = authorInput.getOrNull(0)
    val autorSirName = authorInput.getOrNull(1)
    val autorSurname = authorInput.getOrNull(2)

    val author = when {
        autorSurname != null && autorSirName != null && autorName != null ->
            "${autorName.first().uppercase()}. ${autorSirName.first().uppercase()}. ${autorSurname.replaceFirstChar { it.uppercase() }}"
        autorSurname != null && autorName != null ->
            "${autorName.first().uppercase()}. ${autorSurname.replaceFirstChar { it.uppercase() }}"
        !autorName.isNullOrBlank() ->
            autorName.replaceFirstChar { it.uppercase() }
        else -> {
            if (autorName.isNullOrBlank()) "Автор не известен" else "Ошибка"
        }
    }

    print("Введите год издания: ")
    val yearInput = readln()
    val year = yearInput.toIntOrNull()?: error("Год должен быть числом, а не «$yearInput»")
    val yearFormated = year.toUShort()
    val startYear: UShort = 1450u
    val currentYear: UShort = 2026u

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

    if (yearFormated !in startYear..currentYear) {
        println("$singleMarkerFailed Год $yearFormated вне диапазона $startYear..$currentYear")
        countError++
    } else {
        println("$singleMarkerAccess Год: $yearFormated")
    }

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

    if (copiesInStockMin >= copiesInStock ) {
        println("$singleMarkerFailed Экземпляров: $copiesInStock - должно быть больше нуля")
        countError++
    } else {
        println("$singleMarkerAccess  Экземпляров: $copiesInStock")
    }


    while (true) {
        print("Введите ISBN: ")
        val isbn = readln()
        val isbnClean: String = isbn.replace("-", "").replace(" ", "")

        var lengthMarker: Char
        var allDigitsMarker: Char
        var controlSummMarker: Char
        var controlSummMess: String
        var isbnValidateMess: String

        if (isbnClean.length == 13 ) {
            val allDigits = isbnClean.all { it.isDigit() }
            lengthMarker = singleMarkerAccess


            if (!allDigits) {
                println("Ошибка: ISBN должен содержать только цифры")
                continue
            }else{
                allDigitsMarker = singleMarkerAccess
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

            if (total % 10 == 0) {
                controlSummMarker = singleMarkerAccess
                controlSummMess = "(делится на 10)"
                isbnValidateMess = "валиден"
            }else{
                controlSummMarker = singleMarkerFailed
                controlSummMess = "(не делится на 10)"
                isbnValidateMess = "не валиден"
            }

            println("""
                
                |======== ISBN =======
                |Очищенный:                 $isbnClean
                |Длина:                     $lengthMarker   ${isbnClean.length} 
                |Все цифры:                 $allDigitsMarker
                |Контрольная сумма:         $controlSummMarker   $total $controlSummMess
                |ISBN $isbnValidateMess.
                |=====================
            """.trimMargin())
            break
        } else {
            println("Введенный ISBN не соответствует формату ISBN-13")
        }
    }

    fun thickness() {
        println("\nСтраниц Категория")
        when {pages < thicknessThin -> println("< 50 \"Брошюра\"")
            pages in thicknessThin until thicknessStandard -> println("50..199 \"Стандартная книга\"")
            pages in thicknessStandard until  thicknessBig -> println("200..499 \"Толстая книга\"")
            pages in thicknessBig until thicknessVeryBig-> println("500..999 \"Очень толстая книга\"")
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


    /* Задача 4-3. Float vs Double — ловушка
    //    Запустите, объясните результат в комментарии в коде (одно предложение).
    //    В реальном коде — для денег используйте BigDecimal, для научных вычислений — Double с epsilon-сравнением.
    //    println("\nВнимание, плавающая точка")
    //    val a = 0.1 + 0.2
    //    val b = 0.3
    //    println("a = $a")
    //    println("b = $b")
    //    println("a == b ? ${a == b}") // что выведет?
    //    println("|a - b| < 1e-9 ? ${kotlin.math.abs(a - b) < 1e-9}")

    // Часть вывода a == b ? false
    // println вызывает Double.toString, который печатает минимальную строку
    // т.е. вместо реальных
    // 0.1000000000000000055511151231257827021181583404541015625
    // 0.200000000000000011102230246251565404236316680908203125
    // сумма которых не равна 3, в выводе отображается 0.1 и 0.2
    */

    /* Задача 3-3.
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

            |=== ФИНАЛЬНАЯ СТАТИСТИКА ВЫДАЧ КНИГИ "$titleFinal" ===
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