package com.library.model

sealed class LoanResult {
    data object Success : LoanResult()
    data class NotAvailable(val available: Int) : LoanResult()
    data class TooManyOnHand(val limit: Int) : LoanResult()
    data class BookNotInLibrary(val isbn: String?) : LoanResult()
}
