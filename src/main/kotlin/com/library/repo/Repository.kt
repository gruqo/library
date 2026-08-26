package com.library.repo

interface Identifiable<ID> {
    val id: ID
}
class Repository<ID, T : Identifiable<ID>> {
    private val items: MutableMap<ID, T> = mutableMapOf()
    val size: Int get() = items.size
    fun add(item: T) {
        require(item.id !in items) { "Duplicate ID: ${item.id}" }
        items[item.id] = item
    }
    fun get(id: ID): T? = items[id]
    fun getOrThrow(id: ID): T = items[id] ?: throw NoSuchElementException("No item with id $id")
    fun remove(id: ID): T? = items.remove(id)
    fun all(): List<T> = items.values.toList()
    fun findAll(predicate: (T) -> Boolean): List<T> = items.values.filter(predicate)
}