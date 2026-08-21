package com.library.api

data class BookCreateDto(
    val title: String,
    val author: String,
    val year: Int,
    val price: Double,
    val copies: Int,
    val isbn: String? = null,
)

data class BookResponseDto(
    val id: Long,
    val title: String,
    val author: String,
    val year: Int,
    val price: Double,
    val copies: Int,
    val isbn: String?,
)

fun BookEntity.toDto(): BookResponseDto = BookResponseDto(
    id = id ?: error("Entity not persisted"),
    title = title, author = author, year = year,
    price = price, copies = copies, isbn = isbn,
)

fun BookCreateDto.toEntity(): BookEntity = BookEntity(
    title = title, author = author, year = year,
    price = price, copies = copies, isbn = isbn,
)