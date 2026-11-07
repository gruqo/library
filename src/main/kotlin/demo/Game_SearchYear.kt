package demo

// Игра «Поиск года» из ДЗ-03
fun playYearGuessingGame(year: Int) {
    println("\nИнтерактивная игра \"Поиск года\"")
    var attempts = 0
    while (true) {
        print("\nВведите число (или 0 для выхода): ")
        val guess = readln().toIntOrNull()
        if (guess == null) {
            println("Ошибка: год должен быть числом")
            continue
        }
        when {
            guess == 0 -> {
                println("Выход из игры. Загаданный год: $year")
                break
            }
            guess == year -> {
                attempts++
                println("Угадали! Попыток: $attempts")
                break
            }
            guess < year -> {
                attempts++
                println("Слишком рано")
            }
            else -> {
                attempts++
                println("Слишком поздно")
            }
        }
    }
}

fun main() {
    print("Введите загаданный год: ")
    val year = readln().toIntOrNull() ?: return
    playYearGuessingGame(year)
}
