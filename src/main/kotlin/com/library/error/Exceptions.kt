package com.library.error

sealed class LibraryException(message: String, cause: Throwable? = null) :
    RuntimeException(message, cause)

class BookNotFoundException(val isbn: String) :
    LibraryException("Книга с ISBN $isbn не найдена в каталоге")

class BookAlreadyExistsException(val isbn: String) :
    LibraryException("Книга с ISBN $isbn уже есть в каталоге")

class InvalidIsbnException(val rawIsbn: String, reason: String) :
    LibraryException("Невалидный ISBN «$rawIsbn»: $reason")

class NotAvailableException(val title: String) :
    LibraryException("«$title» нет в наличии")

class CatalogCorruptedException(message: String, cause: Throwable) :
    LibraryException("Каталог повреждён: $message", cause)
