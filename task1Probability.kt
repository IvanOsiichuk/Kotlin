/** Частота появи символів у тексті */
fun charProbability(text: String): Map<Char, Float> {
    val probs = mutableMapOf<Char, Float>() // вірогідності
    text.forEach { c -> probs[c] = probs.getOrDefault(c, 0.0F) + 1 }
    return probs.mapValues { (_, v) -> v / text.length}
}
