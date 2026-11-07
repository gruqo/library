package com.library.util

import com.library.common.ISBN_13_QUANTITY
import com.library.common.PAGES_THICKNESS_BIG
import com.library.common.PAGES_THICKNESS_STANDARD
import com.library.common.PAGES_THICKNESS_THIN
import com.library.common.PAGES_THICKNESS_VERY_BIG

// ========== AUTHOR ==========
fun authorSplit(fullName: String): String {
    val parts = fullName.split(" ").filter { it.isNotBlank() }
    return when (parts.size) {
        0 -> "Автор не известен"
        1 -> parts[0].replaceFirstChar { it.uppercase() }
        2 -> "${parts[0].first().uppercase()}. ${parts[1].replaceFirstChar { it.uppercase() }}"
        else -> "${parts[0].first().uppercase()}. ${parts[1].first().uppercase()}. ${parts.last().replaceFirstChar { it.uppercase() }}"
    }
}

// ========== PAGES ==========
fun pageCategorize(pages: Int): String = when {
    pages < PAGES_THICKNESS_THIN -> "Брошюра"
    pages in PAGES_THICKNESS_THIN until PAGES_THICKNESS_STANDARD -> "${PAGES_THICKNESS_THIN}..${PAGES_THICKNESS_STANDARD - 1} \"Стандартная книга\""
    pages in PAGES_THICKNESS_STANDARD until PAGES_THICKNESS_BIG -> "${PAGES_THICKNESS_STANDARD}..${PAGES_THICKNESS_BIG - 1} \"Толстая книга\""
    pages in PAGES_THICKNESS_BIG until PAGES_THICKNESS_VERY_BIG -> "${PAGES_THICKNESS_BIG}..${PAGES_THICKNESS_VERY_BIG - 1} \"Очень толстая книга\""
    pages >= PAGES_THICKNESS_VERY_BIG -> "$PAGES_THICKNESS_VERY_BIG \"Кирпич\""
    else -> "Толщина не определена"
}

// ========== PRICE ==========
fun priceFormat(price: Double): String = "%.2f руб.".format(price)

fun cleanIsbn(rawIsbn: String): String = rawIsbn.replace("-", "").replace(" ", "")

fun digitsSum(n: Long): Int =
    if (n < 10L) n.toInt()
    else (n % 10L).toInt() + digitsSum(n / 10L)

fun isValidIsbn(isbn: String): Boolean {
    val cleaned = cleanIsbn(isbn)
    if (cleaned.length != ISBN_13_QUANTITY || !cleaned.all { it.isDigit() }) return false
    var sum = 0
    for (i in cleaned.indices) {
        sum += cleaned[i].digitToInt() * (if (i % 2 == 0) 1 else 3)
    }
    return sum % 10 == 0
}

fun describeIsbn(isbn: String?): String {
    if (isbn == null) return "ISBN отсутствует"
    if (isbn.isBlank()) return "ISBN отсутствует"
    return "Длина ${isbn.length}, GS1 префикс ${isbn.substring(0, 3)}"
}
