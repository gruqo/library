package com.library.model

sealed class LoanResult {
    data object Success : LoanResult() // выдано
    data class NotAvailable(val available: Int) : LoanResult() // нет в наличии
    data class TooManyOnHand(val limit: Int) : LoanResult() // у читателя слишком много книг
    data class BookNotInLibrary(val isbn: String?) : LoanResult() // книга не в каталоге
}
