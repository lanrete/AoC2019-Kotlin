package mathUti

fun lcm(a: Long, b: Long): Long {
    return a * b / gcd(b, a)
}