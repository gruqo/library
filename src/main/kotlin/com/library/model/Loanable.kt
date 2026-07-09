package com.library.model

interface Loanable {
    val isAvailable: Boolean
    fun lend(): LoanResult
    fun returnCopy()
    // Реализация по умолчанию:
    fun describeAvailability(): String =
        if (isAvailable) "Доступно для выдачи" else "Все экземпляры на руках"
}
