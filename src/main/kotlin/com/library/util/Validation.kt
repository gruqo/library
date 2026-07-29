package com.library.util

import com.library.error.InvalidIsbnException

// ========== TITLE ==========
const val TITLE_SIMBOL_MAX = 30
fun titleShort(title: String): String {
    val titleTrim = if (title.length > TITLE_SIMBOL_MAX) title.take(27) + "..." else title

    if (titleTrim.isBlank()) return titleTrim
    return if (titleTrim == titleTrim.uppercase()) {
        titleTrim.split(" ").joinToString(" ") { word ->
            word.lowercase().replaceFirstChar { it.uppercase() }
        }
    } else titleTrim
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

fun extractLastName(author: String): String {
    // 1. "Фамилия, Имя" — берём до запятой
    Regex("""^([^,]+),""").find(author)?.let { return it.groupValues[1].trim() }
    // 2. "Имя Фамилия" или "Имя Отчество Фамилия" — последнее слово, если оно не инициал
    val tokens = author.trim().split(Regex("\\s+"))
    return tokens.last { !it.matches(Regex("""[A-ZА-ЯЁ]\.?""")) } // пропускаем "Л." как инициал
}
// ========== YEAR ==========
const val YEAR_START: UShort = 1450u
const val YEAR_CURRENT: UShort = 2026u

fun yearValidate(year: UShort): Boolean = year in YEAR_START..YEAR_CURRENT

// ========== PAGES ==========
const val PAGES_THICKNESS_THIN: UShort = 50u
const val PAGES_THICKNESS_STANDARD: UShort = 200u
const val PAGES_THICKNESS_BIG: UShort = 500u
const val PAGES_THICKNESS_VERY_BIG: UShort = 1000u

fun pageCategorize(pages: UShort): String = when {
    pages < PAGES_THICKNESS_THIN -> "Брошюра"
    pages in PAGES_THICKNESS_THIN until PAGES_THICKNESS_STANDARD -> "50..199 \"Стандартная книга\""
    pages in PAGES_THICKNESS_STANDARD until PAGES_THICKNESS_BIG -> "200..499 \"Толстая книга\""
    pages in PAGES_THICKNESS_BIG until PAGES_THICKNESS_VERY_BIG -> "500..999 \"Очень толстая книга\""
    pages >= PAGES_THICKNESS_VERY_BIG -> "1000 \"Кирпич\""
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
const val ISBN_13_QUANTITY = 13

fun isbnClean(rawIsbn: String): String = rawIsbn.replace("-", "").replace(" ", "")

fun digitsSum(n: Long): Int =
    if (n < 10L) n.toInt()
    else (n % 10L).toInt() + digitsSum(n / 10L)

fun isValidChecksum(isbn: String): Boolean {
    val cleaned = isbnClean(isbn)
    if (cleaned.length != ISBN_13_QUANTITY || !cleaned.all { it.isDigit() }) return false
    var sum = 0
    for (i in cleaned.indices) {
        sum += cleaned[i].digitToInt() * (if (i % 2 == 0) 1 else 3)
    }
    return sum % 10 == 0
}

fun  describeIsbn(isbn: String?): String{
    if (isbn == null) return "ISBN отсутствует"
    if (isbn.isBlank()) return "ISBN отсутствует"
    return "Длина ${isbn.length}, GS1 префикс ${isbn.substring(0, 3)}"
}

fun parseIsbn(raw: String): String {
    val cleaned = raw.replace("-", "").replace(" ", "")
    require(cleaned.length == 13) {
        throw InvalidIsbnException(raw, "ожидаемая длина 13, получено ${cleaned.length}")
    }

    require(cleaned.all { it.isDigit() }) {
        throw InvalidIsbnException(raw, "содержит нецифровые символы")}
    require(isValidChecksum(cleaned)) {
        throw InvalidIsbnException(raw, "контрольная сумма не сходится")}
    return cleaned
}

fun parseIsbnExplicit(raw: String): String {
    val cleaned = raw.replace("-", "").replace(" ", "")
    if (cleaned.length != 13) throw InvalidIsbnException(raw, "длина ${cleaned.length}, нужна 13")
    if (!cleaned.all { it.isDigit() }) throw InvalidIsbnException(raw, "не только цифры")
    if (!isValidChecksum(cleaned)) throw InvalidIsbnException(raw, "плохая контрольная сумма")
    return cleaned
}

// ========== VALIDATION ==========
//  978-0-13-468599-1         ISBN валиден
//  978-5-17-118363-2         ISBN невалиден

const val SINGLE_MARKER_ACCESS = '✓'
const val SINGLE_MARKER_FAILED = '✗'

fun bookValidate(
    title: String, author: List<String>, year: UShort, pages: UShort, price: Double, copies: Int,
    isbn: String?
): List<String> {
    val errors = mutableListOf<String>()
    if (title.isBlank()) errors.add("Название не может быть пустым")
    if (author.isEmpty()) errors.add("Автор не указан")
    if (!yearValidate(year)) errors.add("Год издания ($year) некорректен ($YEAR_START–$YEAR_CURRENT)")
    if (pages <= 0.toUShort()) errors.add("Количество страниц должно быть больше 0")
    if (!priceNonNegative(price)) errors.add("Цена должна быть положительной")
    if (copies < 0) errors.add("Количество экземпляров не может быть отрицательным")
    isbn?.takeIf { !isValidChecksum(it) }?.let { isbn ->
        val cleaned = isbnClean(isbn)
        val lenOk = cleaned.length == ISBN_13_QUANTITY
        val digOk = cleaned.all { it.isDigit() }
        val lenM = if (lenOk) SINGLE_MARKER_ACCESS else SINGLE_MARKER_FAILED
        val digM = if (digOk) SINGLE_MARKER_ACCESS else SINGLE_MARKER_FAILED
        val digitSum = digitsSum(cleaned.toLong())
        errors.add("ISBN не валиден:\n   Очищенный: $cleaned\n   Длина: $lenM (${cleaned.length}/$ISBN_13_QUANTITY)\n   Цифры: $digM\n   Сумма цифр: $digitSum")
    }
    return errors
}