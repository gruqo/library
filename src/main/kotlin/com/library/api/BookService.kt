package com.library.api

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class BookService(private val repo: BookRepository) {

    fun all(): List<BookResponseDto> = repo.findAll().map { it.toDto() }

    fun byId(id: Long): BookResponseDto? = repo.findByIdOrNull(id)?.toDto()

    fun byIsbn(isbn: String): BookResponseDto? = repo.findByIsbn(isbn)?.toDto()

    fun searchByAuthor(author: String): List<BookResponseDto> =
        repo.findByAuthorContainingIgnoreCase(author).map { it.toDto() }

    fun create(dto: BookCreateDto): BookResponseDto {
        require(dto.title.isNotBlank()) { "Title required" }
        require(dto.year in 1450..2100) { "Year out of range" }
        require(dto.price >= 0) { "Price must be non-negative" }
        require(dto.copies >= 0) { "Copies must be non-negative" }
        return repo.save(dto.toEntity()).toDto()
    }

    fun update(id: Long, dto: BookCreateDto): BookResponseDto? {
        val existing = repo.findByIdOrNull(id) ?: return null
        existing.title = dto.title
        existing.author = dto.author
        existing.year = dto.year
        existing.price = dto.price
        existing.copies = dto.copies
        existing.isbn = dto.isbn
        return repo.save(existing).toDto()
    }

    fun delete(id: Long): Boolean {
        if (!repo.existsById(id)) return false
        repo.deleteById(id)
        return true
    }
}