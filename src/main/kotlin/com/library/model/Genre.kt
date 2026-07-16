package com.library.model

enum class Genre(val displayName: String, val emoji: String) {
    FICTION("Художественная", "✦"),
    SCIENCE("Научная", "⚛"),
    PROGRAMMING("Программирование", "⌨"),
    HISTORY("История", "⌛"),
    BIOGRAPHY("Биография", "✍"),
    OTHER("Прочее", "◈");
    companion object {
        fun fromString(value: String?): Genre =
            values().firstOrNull { it.name.equals(value, ignoreCase = true) } ?: OTHER
    }
}
