package com.library

import com.library.common.SINGLE_MARKER_ACCESS
import com.library.io.readBookData

fun main() {
    val book = readBookData() ?: return

    println("$SINGLE_MARKER_ACCESS Книга успешно добавлена!")

    book.printCard()
    book.printCard(withFancyFrame = true)

    repeat(5) { i ->
        val ok = book.lend()
        println("Выдача ${i + 1}: ${if (ok) "ок, осталось ${book.copiesInStock}" else "отказ"}")
    }

    book.returnCopy()
    println("После возврата: ${book.copiesInStock}, всего выдач: ${book.totalLoans}")
}
