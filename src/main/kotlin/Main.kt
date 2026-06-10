package main

import kotlin.ranges.contains

// ========== COMMON VARS ==========
const val titleSimbolMax = 30
const val yearStart: UShort = 1450u
const val yearCurrent: UShort = 2026u

const val pagesThicknessThin: UShort = 50u
const val pagesThicknessStandard: UShort = 200u
const val pagesThicknessBig: UShort = 500u
const val pagesThicknessVeryBig: UShort = 1000u

const val isbn13Quantity = 13
const val singleMarkerAccess = '✓'
const val singleMarkerFailed = '✗'

// ========== INPUT ==========
fun bookInput(): List<Any>? {
    print("Введите название книги: ")
    val titleInput = readln()
    print("Введите автора: ")
    val authorInput = readln().split(" ")
    print("Введите год издания: ")
    val yearInput = readln().toIntOrNull()
    if (yearInput == null) { println("  Ошибка: год должен быть числом\n"); return null }
    val year = yearInput.toUShort()
    print("Введите количество страниц: ")
    val pagesInput = readln().toUShortOrNull()
    if (pagesInput == null) { println("  Ошибка: количество страниц должно быть числом\n"); return null }
    print("Введите цену (руб.): ")
    val priceInput = readln().toDoubleOrNull()
    if (priceInput == null) { println("  Ошибка: цена должна быть числом\n"); return null }
    print("Введите количество экземпляров: ")
    val copiesInStockInput = readln().toIntOrNull()
    if (copiesInStockInput == null) { println("  Ошибка: количество экземпляров должно быть числом\n"); return null }
    print("Введите ISBN: ")
    val isbnInput = readln()
    return listOf(titleInput, authorInput, year, pagesInput, priceInput, copiesInStockInput, isbnInput)
}

// ========== TITLE ==========
fun titleShort(title: String): String {
    val titleTrimm = if (title.length > titleSimbolMax) title.take(27) + "..." else title

    if (titleTrimm.isBlank()) return titleTrimm
    return if (titleTrimm == titleTrimm.uppercase()) {
        titleTrimm.split(" ").joinToString(" ") { word ->
            word.lowercase().replaceFirstChar { it.uppercase() }
        }
    } else titleTrimm
}

// ========== AUTHOR ==========
fun authorSplit(fullName: String): String {
    val parts = fullName.split(" ")
    return when (parts.size) {
        0 -> "Автор не известен"
        1 -> parts[0].replaceFirstChar { it.uppercase() }
        2 -> "${parts[0].first().uppercase()}. ${parts[1].replaceFirstChar { it.uppercase() }}"
        else -> "${parts[0].first().uppercase()}. ${parts[1].first().uppercase()}. ${parts[2].replaceFirstChar { it.uppercase() }}"
    }
}
// ========== YEAR ==========
fun yearValidate(year: UShort): Boolean = year in yearStart..yearCurrent

// ========== PAGES ==========
fun pageCategorize(pages: UShort): String = when {
    pages < pagesThicknessThin -> "Брошюра"
    pages in pagesThicknessThin until pagesThicknessStandard -> "50..199 \"Стандартная книга\""
    pages in pagesThicknessStandard until pagesThicknessBig -> "200..499 \"Толстая книга\""
    pages in pagesThicknessBig until pagesThicknessVeryBig -> "500..999 \"Очень толстая книга\""
    pages >= pagesThicknessVeryBig -> "1000 \"Кирпич\""
    else -> "Толщина не определена"
}

// ========== PRICE ==========
fun priceNonNegative(price: Double): Boolean = price > 0.00
fun priceFormat(price: Double): String = "%.2f руб.".format(price)

// ========== COPIES ==========
fun simulateLoans(copiesInStock: Int, requestedLoans: Int): String {
    val actualLoans = minOf(copiesInStock, requestedLoans)
    val remaining = copiesInStock - actualLoans
    return when {
        requestedLoans <= 0 -> "Ошибка: количество запросов должно быть положительным"
        actualLoans < requestedLoans -> "Выдано $actualLoans из $requestedLoans. Осталось $remaining шт."
        else -> "Выдано $actualLoans экземпляров. Осталось $remaining шт."
    }
}

// ========== ISBN ==========
fun isbnClean(rawIsbn: String): String = rawIsbn.replace("-", "").replace(" ", "")

fun digitsSum(n: Long): Int =
    if (n < 10L) n.toInt()
    else (n % 10L).toInt() + digitsSum(n / 10L)

fun isbnValidate(isbn: String): Boolean {
    val cleaned = isbnClean(isbn)
    if (cleaned.length != isbn13Quantity || !cleaned.all { it.isDigit() }) return false
    var sum = 0
    for (i in cleaned.indices) {
        sum += cleaned[i].digitToInt() * (if (i % 2 == 0) 1 else 3)
    }
    return sum % 10 == 0
}

// ========== VALIDATION ==========
fun bookValidate(
    title: String, year: UShort, pages: UShort, price: Double, copies: Int,
    author: List<String>, isbn: String
): List<String> {
    val errors = mutableListOf<String>()
    if (title.isBlank()) errors.add("Название не может быть пустым")
    if (author.isEmpty()) errors.add("Автор не указан")
    if (!yearValidate(year)) errors.add("Год издания ($year) некорректен ($yearStart–$yearCurrent)")
    if (pages <= 0.toUShort()) errors.add("Количество страниц должно быть больше 0")
    if (!priceNonNegative(price)) errors.add("Цена должна быть положительной")
    if (copies < 0) errors.add("Количество экземпляров не может быть отрицательным")
    if (!isbnValidate(isbn)) {
        val cleaned = isbnClean(isbn)
        val lenOk = cleaned.length == isbn13Quantity
        val digOk = cleaned.all { it.isDigit() }
        val lenM = if (lenOk) singleMarkerAccess else singleMarkerFailed
        val digM = if (digOk) singleMarkerAccess else singleMarkerFailed
        val digitSum = digitsSum(cleaned.toLong())
        errors.add("ISBN не валиден:\n   Очищенный: $cleaned\n   Длина: $lenM (${cleaned.length}/$isbn13Quantity)\n   Цифры: $digM\n   Сумма цифр: $digitSum")
    }
    return errors
}

// ========== OUTPUT ==========
fun bookPrint(
    title: String, author: String, year: UShort, pages: UShort, price: Double,
    copies: Int, isbn: String, isbnDigitsSum: Int, withFancyFrame: Boolean = false
) {
    if (withFancyFrame) {
        println("""
         
        ╔═════════════════════════════════════════════════════════════════╗
        ║ --- КАРТОЧКА КНИГИ  ---                                         ║
        ╠═════════════════════════════════════════════════════════════════╣
        ║  Название:                  $title
        ║  Автор:                     $author
        ║  Год издания:               $year
        ║  Кол-во страниц:            $pages (${pageCategorize(pages)})
        ║  Цена:                      ${priceFormat(price)}
        ║  В наличии:                 $copies шт.
        ║  Общая стоимость на складе: ${priceFormat(price * copies)}
        ║  ISBN:                      $isbn
        ║  Сумма цифр ISBN:           $isbnDigitsSum
        ╚═════════════════════════════════════════════════════════════════╝
        """.trimIndent())
    } else {
        println(
            """

            |=== КАРТОЧКА КНИГИ ===
            |Название:                  $title
            |Автор:                     $author
            |Год издания:               $year
            |Кол-во страниц:            $pages (${pageCategorize(pages)})
            |Цена:                      ${priceFormat(price)}
            |В наличии:                 $copies шт.
            |Общая стоимость на складе: ${priceFormat(price * copies)}
            |ISBN:                      $isbn
            |======================

            |$singleMarkerAccess Книга успешно добавлена!
    """.trimMargin()
        )
    }
}

fun main() {
    println("Добро пожаловать в библиотеку!\n")
    var bookValid = false
    while (!bookValid) {
        val book = bookInput() ?: continue
        val title = book[0] as String
        val authorParts = book[1] as List<String>
        val year = book[2] as UShort
        val pages = book[3] as UShort
        val price = book[4] as Double
        val copies = book[5] as Int
        val isbn = book[6] as String

        val errors = bookValidate(title, year, pages, price, copies, authorParts, isbn)
        if (errors.isNotEmpty()) {
            println("\n\u26a0\ufe0f ИНФОРМАЦИЯ О КНИГЕ СОДЕРЖИТ НЕВЕРНЫЕ ДАННЫЕ:")
            errors.forEach { println("   $it") }
            println("\n\ud83d\udd01 Пожалуйста, введите данные заново...\n")
            continue
        }
        val authorName = authorSplit(authorParts.joinToString(" "))
        val shortTitle = titleShort(title)
        val sumDigits = digitsSum(isbnClean(isbn).toLong())
        bookPrint(shortTitle, authorName, year, pages, price, copies, isbn, sumDigits)
        bookPrint(shortTitle, authorName, year, pages, price, copies, isbn, sumDigits, withFancyFrame = true)

        println("\n--- Симуляция выдачи ---")
        println(simulateLoans(copies, 3))
        bookValid = true
    }
}
