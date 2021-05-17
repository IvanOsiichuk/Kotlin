package fraction
import kotlin.math.abs

/** Найбільний спільний дільник [алгоритм Евкліда]
 * @param n чисельник
 * @param d знаменник */
fun gcd(n: Int, d: Int): Int {
    var (a, b) = listOf<Int>(n, d)
    while (b != 0) {
        a = b.also { b = a % b }
    }

//    while (b != 0)

    return a + b
}

/** Найменший спільний знаменник
 * @param n чисельник
 * @param d знаменник */
fun lcm(n: Int, d: Int) = abs(n * d) / gcd(n, d)


/** Дріб */
class Fraction (var numerator: Int = 1, _denominator: Int = 1) {
    var denominator = 1 // знаменник
        set(value) {
            field = if (value != 0) value else field // заборона 0-значень
        }
        init {
            denominator = _denominator
        }

    /** Вторинний конструктор
     * @param exp Вираз вигляду "1/1" */
    constructor(exp: String) : this() {
        val args = exp.split('/').map { it.toIntOrNull() }
        if (args.size == 2) {
            numerator = args[0] ?: 1
            denominator = args[1] ?: 1
        }
    }

    /** Спільні найменше кратне і найбільший дільник відповідно
     * @param x інший дріб*/
    fun denominatorCommon(x: Fraction) =
        Pair<Int, Int>(lcm(denominator, x.denominator),
            gcd(denominator, x.denominator))

    /** Скорочення дробів
     * @param x інший дріб */
    fun reduce(x: Fraction): Pair<Fraction, Fraction> {
        val upCommon = gcd(numerator, x.denominator)
        val downCommon = gcd(denominator, x.numerator)
        return Pair(Fraction(numerator / upCommon, denominator / downCommon),
            Fraction(x.numerator / downCommon, x.denominator / upCommon))
    }

    /** Скорочення дробу */
    fun reduce(): Fraction {
        val commonBiggest = gcd(numerator, denominator)
        return Fraction(numerator / commonBiggest, denominator / commonBiggest)
    }

    /** Допоміжна функція для операцій додавання-віднімання
     * @param x інший дріб
     * @param sign функція, що +/- два операнди */
    private fun arithmetic(x: Fraction, sign: (a: Int, b: Int) -> Int):Fraction {
        if (denominator == x.denominator)
            return Fraction(numerator + x.numerator, denominator).reduce()

        val (f, s) = reduce(x) // обидва операнди
        val commonLeast = lcm(f.denominator, s.denominator)
        return Fraction(sign(f.numerator * (commonLeast / f.denominator),
                s.numerator * (commonLeast / s.denominator)), commonLeast).reduce()
    }

    // ПРИВЕДЕННЯ
    override fun toString() = "$numerator/$denominator"
    fun toFloat() = numerator / denominator.toFloat()

    // ОПЕРАТОРИ
    operator fun plus(x: Fraction) = arithmetic(x, { a, b -> a + b })
    operator fun minus(x: Fraction) = arithmetic(x, { a, b -> a - b })
    operator fun times(x: Fraction) = Fraction(
        numerator * x.numerator, denominator * x.denominator)
    operator fun div(x: Fraction) = this * Fraction(x.denominator, x.numerator)
    operator fun unaryPlus() = this.reduce() // +Fraction - скорочення !non logic
}
