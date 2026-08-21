package com.library.api

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/books")
class BookController(private val service: BookService) {

    @GetMapping
    fun list(@RequestParam(required = false) author: String?): List<BookResponseDto> =
        if (author != null) service.searchByAuthor(author) else service.all()

    @GetMapping("/{id}")
    fun byId(@PathVariable id: Long): ResponseEntity<BookResponseDto> =
        service.byId(id)?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()

    @GetMapping("/isbn/{isbn}")
    fun byIsbn(@PathVariable isbn: String): ResponseEntity<BookResponseDto> =
        service.byIsbn(isbn)?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody dto: BookCreateDto): BookResponseDto = service.create(dto)

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody dto: BookCreateDto): ResponseEntity<BookResponseDto> =
        service.update(id, dto)?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Unit> =
        if (service.delete(id)) ResponseEntity.noContent().build()
        else ResponseEntity.notFound().build()
}