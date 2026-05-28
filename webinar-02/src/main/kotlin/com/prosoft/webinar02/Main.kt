package com.prosoft.webinar02

const val MAX_COUNT = 100

/**
 * Это точка входа
 * @author Автор это я
 * @param - входящие параметры
 * @return - тип результата
 */
fun main() {

    val language = "Kotlin" // read-only
    // language = "Java" // Kotlin: 'val' cannot be reassigned.
    println(language)

    var dayOfWeek = "Monday"
    println(dayOfWeek)
    dayOfWeek = "Tuesday"
    println(dayOfWeek)

    println("Максимальное количество: $MAX_COUNT")

    val message = "Hello World" // String
    val number = 42 // тип Int
    val pi = 3.14   // Double

    println("$message, число $number, pi = $pi")

    var age = 30 //  тип Int
    // age = "30" // Kotlin: Assignment type mismatch: actual type is 'String', but 'Int' was expected.
    age = 31
    println(age)

    // L
    val smallNumber = 1000 // Int
    val bigNumber = 3_000_000_000 // Long
    val explicitLong = 100L // явно Long
    println("small: ${smallNumber.javaClass}")

    val floatVal = 3.14F // Float
    val grade: Char = 'A'
    val fullNumber: String = "Kotlin"

    val isKotlinFun = true

    if (isKotlinFun) {
        println("Kotlin is Kotlin")
    } else {
        println("Kotlin is Java")
    }

    val a = 10
    val b = 20

    print("Сумма: ")
    println("${a + b}")

}