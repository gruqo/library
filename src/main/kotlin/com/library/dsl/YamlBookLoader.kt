package com.library.dsl

import com.library.model.*
import org.yaml.snakeyaml.Yaml
import java.io.File

fun loadBooksFromYaml(path: String): List<Book> {
    val data = Yaml().load<Map<String, Any>>(File(path).readText())
    val books = data["books"] as? List<*> ?: emptyList<Any>()
    return books.mapNotNull { item ->
        val map = item as? Map<*, *> ?: return@mapNotNull null
        when (map["type"] as? String ?: "printed") {
            "ebook" -> buildEBook(map)
            "audiobook" -> buildAudioBook(map)
            else -> buildPrintedBook(map)
        }
    }
}

private fun buildPrintedBook(map: Map<*, *>): PrintedBook = PrintedBook(
    title = map["title"] as? String ?: "",
    author = map["author"] as? String ?: "",
    year = (map["year"] as? Number)?.toInt() ?: 0,
    price = Money((map["price"] as? Number)?.toDouble() ?: 0.0),
    copies = (map["copies"] as? Number)?.toInt() ?: 1,
    pages = (map["pages"] as? Number)?.toInt() ?: 100,
    isbn = map["isbn"] as? String,
    genre = Genre.fromString(map["genre"] as? String),
    tags = (map["tags"] as? List<*>)?.map { it.toString() }?.toSet() ?: emptySet()
)

private fun buildEBook(map: Map<*, *>): EBook = EBook(
    title = map["title"] as? String ?: "",
    author = map["author"] as? String ?: "",
    year = (map["year"] as? Number)?.toInt() ?: 0,
    price = Money((map["price"] as? Number)?.toDouble() ?: 0.0),
    pages = (map["pages"] as? Number)?.toInt() ?: 0,
    sizeMb = (map["sizeMb"] as? Number)?.toDouble() ?: 0.0,
    format = map["format"] as? String ?: "PDF",
    isbn = map["isbn"] as? String,
    genre = Genre.fromString(map["genre"] as? String)
)

private fun buildAudioBook(map: Map<*, *>): AudioBook = AudioBook(
    title = map["title"] as? String ?: "",
    author = map["author"] as? String ?: "",
    year = (map["year"] as? Number)?.toInt() ?: 0,
    price = Money((map["price"] as? Number)?.toDouble() ?: 0.0),
    durationMinutes = (map["durationMinutes"] as? Number)?.toInt() ?: 0,
    narrator = map["narrator"] as? String ?: ""
)
