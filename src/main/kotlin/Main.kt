package main

// ========== INPUT ==========
fun bookInput(): List<Any?>? {

    print("Введите название книги: ")
    val titleInput = readln()

    print("Введите автора: ")
    val authorInput = readln().split(" ")

    print("Введите язык оригинала (Enter если оригинал на русском): ")
    val originalLanguage = readln().ifBlank { null }

    print("Введите переводчика: ")
    val translator = readln().ifBlank { null }

    print("Введите номер издания: ")
    val edition = readln().toIntOrNull()

    print("Введите год издания: ")
    val yearInput = readln().toIntOrNull()
        ?: return null.also { println("Ошибка: год должен быть числом\n") }
    val year = yearInput.toUShort()

    print("Введите количество страниц: ")
    val pagesInput = readln().toUShortOrNull()
        ?: return null.also { println("Ошибка: количество страниц должно быть числом\n") }

    print("Введите цену (руб.): ")
    val priceInput = readln().toDoubleOrNull()
        ?: return null.also { println("Ошибка: цена должна быть числом\n") }

    print("Введите количество экземпляров: ")
    val copiesInStockInput = readln().toIntOrNull()
    if (copiesInStockInput == null) { println("Ошибка: количество экземпляров должно быть числом\n"); return null }

    print("Введите ISBN: ")
    val rawIsbn = readln()
    val cleaned = isbnClean(rawIsbn)

    println( describeIsbn(cleaned))
    val isbnInput = if (cleaned.isNotBlank() && isbnValidate(cleaned)) rawIsbn else null

    return listOf(titleInput, authorInput, originalLanguage, translator, edition, year, pagesInput, priceInput, copiesInStockInput, isbnInput)
}

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

fun isbnValidate(isbn: String): Boolean {
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
    isbn?.takeIf { !isbnValidate(it) }?.let { isbn ->
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

// ========== OUTPUT ==========
fun bookPrint(
    title: String, author: String, language: String?, translator: String?, edition: Int?, year: UShort, pages: UShort, price: Double,
    copies: Int, isbn: String?
) {
    println(
        """
        |
        |=== КАРТОЧКА КНИГИ ===
        |Название:                  $title
        |Автор:                     $author
        |Год издания:               $year
        |Кол-во страниц:            $pages (${pageCategorize(pages)})
        |Цена:                      ${priceFormat(price)}
        |В наличии:                 $copies шт.
        |Общая стоимость на складе: ${priceFormat(price * copies)}
    """.trimMargin()
    )

    language?.let { println("Язык оригинала:            $it") }
    translator?.let { println("Переводчик:                $it") }
    edition?.let { println("Издание:                   $it") }
    isbn?.let { println("ISBN:                      $it") }

    println(
        """
        |
        |$SINGLE_MARKER_ACCESS Книга успешно добавлена!
        |======================
    """.trimMargin()
    )
}

fun main() {
    println("Добро пожаловать в библиотеку!\n")
    var bookValid = false
    while (!bookValid) {
        val book = bookInput() ?: continue
        val title = book[0] as String
        @Suppress("UNCHECKED_CAST")
        val authorParts = book[1] as List<String>
        val language = book[2] as String?
        val translator = book[3] as String?
        val edition = book[4] as Int?
        val year = book[5] as UShort
        val pages = book[6] as UShort
        val price = book[7] as Double
        val copies = book[8] as Int
        val isbn = book[9] as String?
        val errors = bookValidate(title, authorParts, year, pages, price, copies,  isbn)
        if (errors.isNotEmpty()) {
            println("\n\u26a0\ufe0f ИНФОРМАЦИЯ О КНИГЕ СОДЕРЖИТ НЕВЕРНЫЕ ДАННЫЕ:")
            errors.forEach { println("   $it") }
            println("\n\ud83d\udd01 Пожалуйста, введите данные заново...\n")
            continue
        }
        val authorName = authorSplit(authorParts.joinToString(" "))
        val shortTitle = titleShort(title)

        bookPrint(shortTitle, authorName, language, translator, edition, year, pages, price, copies, isbn)

        println("\n--- Симуляция выдачи ---")
        println("\n ${simulateLoans(copies, 3)}\n")
        bookValid = true
    }
}
