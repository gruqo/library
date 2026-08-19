package concurrent.thread

import kotlin.concurrent.thread
import java.util.concurrent.atomic.AtomicInteger

fun unSynchronize() {
    var counter = 0
    val threads = List(10) {
        thread {
            repeat(10_000) {
                counter++
            }
        }
    }
    threads.forEach { it.join() }
    println("Без synchronized: Ожидали: 100000, получили: $counter")
}

fun synchronize() {
    var counter = 0
    val lock = Any()
    val threads = List(10) {
        thread {
            repeat(10_000) {
                synchronized(lock) {
                    counter++
                }
            }
        }
    }
    threads.forEach { it.join() }
    println("С synchronized: $counter") // всегда 100000
}


fun atomicInteger() {
    val counter = AtomicInteger(0)
    val threads = List(10) {
        thread {
            repeat(10_000) {
                counter.incrementAndGet()
            }
        }
    }
    threads.forEach { it.join() }
    println("С AtomicInteger: ${counter.get()}")
}

fun stringBuilderVsStringBuffer() {
    val sb = StringBuilder()
    val threadsSb = List(10) {
        thread {
            repeat(1000) {
                sb.append("x")
            }
        }
    }
    threadsSb.forEach { it.join() }
    println("StringBuilder: ожидали 10000, получили ${sb.length}")

    val sbf = StringBuffer()
    val threadsSbf = List(10) {
        thread {
            repeat(1000) {
                sbf.append("x")
            }
        }
    }
    threadsSbf.forEach { it.join() }
    println("StringBuffer: ожидали 10000, получили ${sbf.length}")
}

fun volatileVisibility() {
    class Worker {
        var stop: Boolean = false
        fun run() {
            thread {
                while (!stop) { /* busy work */ }
                println("Stopped")
            }
        }
    }
    val worker = Worker()
    worker.run()
    Thread.sleep(100)
    worker.stop = true
    Thread.sleep(500)
    println("Main: stop = ${worker.stop}, but worker may still loop")

    class VolatileWorker {
        @Volatile var stop: Boolean = false
        fun run() {
            thread {
                while (!stop) { /* busy work */ }
                println("Stopped")
            }
        }
    }
    val vworker = VolatileWorker()
    vworker.run()
    Thread.sleep(100)
    vworker.stop = true
    Thread.sleep(500)
    println("Main (volatile): stop = ${vworker.stop}, worker stopped")
}

fun main() {
    unSynchronize()
    println()
    synchronize()
    println()
    atomicInteger()
    println()
    stringBuilderVsStringBuffer()
    println()
    volatileVisibility()
}
