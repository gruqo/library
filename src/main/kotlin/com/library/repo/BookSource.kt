package com.library.repo

import com.library.model.Book

interface BookSource<out T : Book> {
    fun all(): List<T>
}