package com.prosoft.webinar01.homework

fun main() {
    // --- ОСНОВНАЯ ЗАДАЧА ----

    // Вывод строки приветствия пользователя библиотеки
    println("")
    println("Добро пожаловать в библиотеку!")

    // Вывод карточки первой книги
    val author: String = "Фёдор Достоевский"
    val title: String = "Преступление и наказание"
    val year: Int = 1866
    val pages: Int = 672
    val price: Double = 1234.56
    val quantity: Int = 20

    println("")
    println("""
        |=== КАРТОЧКА КНИГИ ===
        |Название:     $title
        |Автор:        $author
        |Год издания:  $year
        |Кол-во стран: $pages
        |Цена:         $price руб.
        |В наличии:    $quantity шт.
        |=====================
    """.trimMargin())

    // --- БОНУСНАЯ ЗАДАЧА ---
    showLiterals()
}

fun showLiterals() {

    val million = 1_000_000
    val hex = 0xCAFE
    val binary = 0b1010_1010
    val scientific = 1.5e3

    println("")
    println("""
    БОНУСНАЯ ЗАДАЧА:
    Десятичная с подчёркиваниями: 1_000_000 = $million
    Шестнадцатеричная: 0xCAFE = $hex
    Двоичная: 0b1010_1010 = $binary
    Научная нотация: 1.5e3 = $scientific
""".trimIndent())
}
