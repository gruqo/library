package com.library.api

import jakarta.persistence.*

@Entity
@Table(name = "books")
class BookEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    var title: String = "",

    @Column(nullable = false)
    var author: String = "",

    @Column(nullable = false)
    var year: Int = 0,

    @Column(nullable = false)
    var price: Double = 0.0,

    @Column(nullable = false)
    var copies: Int = 0,

    @Column(unique = true)
    var isbn: String? = null,
)
