package com.library.api

import org.springframework.data.jpa.repository.JpaRepository

interface BookRepository : JpaRepository<BookEntity, Long> {
    fun findByIsbn(isbn: String): BookEntity?
    fun findByAuthorContainingIgnoreCase(author: String): List<BookEntity>
}