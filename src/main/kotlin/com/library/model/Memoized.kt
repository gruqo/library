package com.library.model

import kotlin.reflect.KProperty

class Memoized<T>(private val compute: () -> T) {
    private var cached: T? = null
    private var initialized = false
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        if (!initialized) {
            cached = compute()
            initialized = true
        }
        @Suppress("UNCHECKED_CAST")
        return cached as T
    }
    fun reset() {
        initialized = false
        cached = null
    }
}