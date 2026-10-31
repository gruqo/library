package demo

fun Double.exactString(): String = java.math.BigDecimal(this).toPlainString()

fun main() {
    println("\nВнимание, плавающая точка")
    println(0.1.exactString())
    println(0.2.exactString())

    val a = 0.1 + 0.2
    val b = 0.3
    println("a = $a")
    println("b = $b")
    // 0.1 и 0.2 не представимы точно в двоичной системе, поэтому их сумма равна 0.30000000000000004, и == возвращает false.
    println("a == b ? ${a == b}")
    println("|a - b| < 1e-9 ? ${kotlin.math.abs(a - b) < 1e-9}")
}
