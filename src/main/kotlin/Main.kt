package main

import demo.playYearGuessingGame

// ========== COMMON VARS ==========
const val TITLE_SYMBOL_MAX = 30

const val YEAR_START: Int = 1450
const val YEAR_CURRENT: Int = 2026

const val PAGES_THICKNESS_THIN: Int = 50
const val PAGES_THICKNESS_STANDARD: Int = 200
const val PAGES_THICKNESS_BIG: Int = 500
const val PAGES_THICKNESS_BIG_VERY: Int = 1000

const val ISBN_13_QUANTITY = 13

const val SINGLE_MARKER_ACCESS = '✓'
const val SINGLE_MARKER_FAILED = '✗'

// ========== INPUT ==========
fun readTitle(): String {
    print("Введите название книги: ")
    return readln()
}

fun readAuthor(): String {
    print("Введите автора: ")
    return readln()
}

fun readIntOrNull(prompt: String, error: String): Int? {
    print(prompt)
    val value = readln().toIntOrNull()
    if (value == null) println("  Ошибка: $error\n")
    return value
}

fun readDoubleOrNull(prompt: String, error: String): Double? {
    print(prompt)
    val value = readln().toDoubleOrNull()
    if (value == null) println("  Ошибка: $error\n")
    return value
}

fun readYear(): Int? = readIntOrNull("Введите год издания: ", "год должен быть числом")
fun readPages(): Int? = readIntOrNull("Введите количество страниц: ", "количество страниц должно быть числом")
fun readPrice(): Double? = readDoubleOrNull("Введите цену (руб.): ", "цена должна быть числом")
fun readCopies(): Int? = readIntOrNull("Введите количество экземпляров: ", "количество экземпляров должно быть числом")
fun readRequestedLoans(): Int? = readIntOrNull("Сколько раз выдать книгу? ", "количество выдач должно быть числом")

fun readIsbn(): String {
    print("Введите ISBN: ")
    return readln()
}

// ========== TITLE ==========
fun makeShortTitle(title: String, maxLen: Int = TITLE_SYMBOL_MAX): String {
    val trimmed = if (title.length > maxLen) title.take(maxLen - 3) + "..." else title

    if (trimmed.isBlank()) return trimmed
    return if (trimmed.any { it.isLetter() } && trimmed == trimmed.uppercase()) {
        trimmed.split(" ").joinToString(" ") { word ->
            word.lowercase().replaceFirstChar { it.uppercase() }
        }
    } else trimmed
}

// ========== AUTHOR ==========
fun makeAuthorInitials(fullName: String): String {
    val parts = fullName.split(" ").filter { it.isNotBlank() }
    return when (parts.size) {
        0 -> "Автор не известен"
        1 -> parts[0].replaceFirstChar { it.uppercase() }
        2 -> "${parts[0].first().uppercase()}. ${parts[1].replaceFirstChar { it.uppercase() }}"
        else -> "${parts[0].first().uppercase()}. ${parts[1].first().uppercase()}. ${parts.last().replaceFirstChar { it.uppercase() }}"
    }
}

// ========== YEAR ==========
fun yearValidate(year: Int): Boolean = year in YEAR_START..YEAR_CURRENT

// ========== PAGES ==========
fun categorizePages(pages: Int): String = when {
    pages < PAGES_THICKNESS_THIN -> "Брошюра"
    pages < PAGES_THICKNESS_STANDARD -> "Стандартная книга"
    pages < PAGES_THICKNESS_BIG -> "Толстая книга"
    pages < PAGES_THICKNESS_BIG_VERY -> "Очень толстая книга"
    else -> "Кирпич"
}

// ========== PRICE ==========
fun pricePositive(price: Double): Boolean = price > 0.00
fun formatPrice(price: Double): String = "%.2f руб.".format(price)

// ========== COPIES ==========
fun simulateLoans(copiesInStock: Int, requestedLoans: Int): String {
    if (requestedLoans <= 0) return "Ошибка: количество запросов должно быть положительным"
    val lines = mutableListOf<String>()
    var onHand = 0
    var totalLoans = 0
    for (i in 1..requestedLoans) {
        if (onHand < copiesInStock) {
            onHand++
            totalLoans++
            lines.add("Выдача $i: на руках $onHand из $copiesInStock")
        } else {
            lines.add("Выдача $i: отказ — все экземпляры заняты")
            break
        }
    }
    lines.add("Всего выдач: $totalLoans")
    lines.add("Сколько на руках: $onHand")
    lines.add("Сколько свободно: ${copiesInStock - onHand}")
    return lines.joinToString("\n")
}

// ========== ISBN ==========
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

// ========== VALIDATION ==========
fun validateBook(
    title: String, year: Int, pages: Int, price: Double, copies: Int,
    author: String, isbn: String
): List<String> {
    val errors = mutableListOf<String>()
    if (title.isBlank()) errors.add("Название не может быть пустым")
    if (author.isBlank()) errors.add("Автор не указан")
    if (!yearValidate(year)) errors.add("Год издания ($year) некорректен ($YEAR_START–$YEAR_CURRENT)")
    if (pages <= 0) errors.add("Количество страниц должно быть больше 0")
    if (!pricePositive(price)) errors.add("Цена должна быть положительной")
    if (copies < 0) errors.add("Количество экземпляров не может быть отрицательным")
    if (!isValidIsbn(isbn)) {
        val cleaned = cleanIsbn(isbn)
        val lenOk = cleaned.length == ISBN_13_QUANTITY
        val digOk = cleaned.isNotEmpty() && cleaned.all { it.isDigit() }
        val lenM = if (lenOk) SINGLE_MARKER_ACCESS else SINGLE_MARKER_FAILED
        val digM = if (digOk) SINGLE_MARKER_ACCESS else SINGLE_MARKER_FAILED
        val digitSum = if (lenOk && digOk) digitsSum(cleaned.toLong()) else null
        errors.add(
            "ISBN не валиден:\n   Очищенный: $cleaned\n   Длина: $lenM (${cleaned.length}/$ISBN_13_QUANTITY)\n   Цифры: $digM\n   Сумма цифр: ${digitSum ?: "—"}"
        )
    }
    return errors
}

// ========== OUTPUT ==========
fun printBookCard(
    title: String, author: String, year: Int, pages: Int, price: Double,
    copies: Int, isbn: String, isbnDigitsSum: Int, withFancyFrame: Boolean = false
) {
    if (withFancyFrame) {
        val frameWidth = 67
        val border = "═".repeat(frameWidth - 2)
        val row: (String) -> String = { "║  $it".padEnd(frameWidth - 1) + "║" }

        println("╔$border╗")
        println(row("--- КАРТОЧКА КНИГИ  ---"))
        println("╠$border╣")
        println(row("Название:                  $title"))
        println(row("Автор:                     $author"))
        println(row("Год издания:               $year"))
        println(row("Кол-во страниц:            $pages (${categorizePages(pages)})"))
        println(row("Цена:                      ${formatPrice(price)}"))
        println(row("В наличии:                 $copies шт."))
        println(row("Общая стоимость на складе: ${formatPrice(price * copies)}"))
        println(row("ISBN:                      $isbn"))
        println(row("Сумма цифр ISBN:           $isbnDigitsSum"))
        println("╚$border╝")
    } else {
        println(
            """
            |=== КАРТОЧКА КНИГИ ===
            |Название:                  $title
            |Автор:                     $author
            |Год издания:               $year
            |Кол-во страниц:            $pages (${categorizePages(pages)})
            |Цена:                      ${formatPrice(price)}
            |В наличии:                 $copies шт.
            |Общая стоимость на складе: ${formatPrice(price * copies)}
            |ISBN:                      $isbn
            |Сумма цифр ISBN:           $isbnDigitsSum
            |======================
            """.trimMargin()
        )
    }
}

fun main() {
    println("Добро пожаловать в библиотеку!\n")

    while (true) {
        val title = readTitle()
        val author = readAuthor()
        val year = readYear() ?: continue
        val pages = readPages() ?: continue
        val price = readPrice() ?: continue
        val copies = readCopies() ?: continue
        val isbn = readIsbn()

        val errors = validateBook(title, year, pages, price, copies, author, isbn)
        if (errors.isNotEmpty()) {
            println("\n\u26a0\ufe0f ИНФОРМАЦИЯ О КНИГЕ СОДЕРЖИТ НЕВЕРНЫЕ ДАННЫЕ:")
            errors.forEach { println("   $it") }
            println("\n\ud83d\udd01 Пожалуйста, введите данные заново...\n")
            continue
        }

        val authorName = makeAuthorInitials(author)
        val shortTitle = makeShortTitle(title)
        val cleanedIsbn = cleanIsbn(isbn)
        val sumDigits = digitsSum(cleanedIsbn.toLong())
        printBookCard(shortTitle, authorName, year, pages, price, copies, cleanedIsbn, sumDigits)
        printBookCard(shortTitle, authorName, year, pages, price, copies, cleanedIsbn, sumDigits, withFancyFrame = true)
        println("$SINGLE_MARKER_ACCESS Книга успешно добавлена!")

        println("\n--- Симуляция выдачи ---")
        val requestedLoans = readRequestedLoans() ?: break
        println(simulateLoans(copies, requestedLoans))

        playYearGuessingGame(year)
        break
    }
}
