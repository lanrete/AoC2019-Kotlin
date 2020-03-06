package mathUti

fun gcd(a: Int, b: Int): Int {
    if (a < b) return gcd(b, a)
    if (b != 0) return gcd(b, a % b)
    return a
}

fun gcd(a: Long, b: Long): Long {
    if (a < b) return gcd(b, a)
    if (b != 0L) return gcd(b, a % b)
    return a
}