object Day4 : Solver() {
    override val day: Int = 4
    override val inputs: List<String> = getInput()

    private fun getRange(): IntRange {
        val (start, end) = inputs[0]
            .split('-')
            .map { it.toInt() }
        return (start..end)
    }

    private val range = getRange()

    private fun isValidQ1(candidate: Int): Boolean {
        if (candidate < 100000) {
            return false
        }
        if (candidate > 999999) {
            return false
        }
        val digits = candidate.toString()
        if (digits
                .map { it - '0' }
                .reduce { acc, i -> if (acc <= i) i else 10 }
            == 10
        ) {
            return false
        }
        if (digits
                .mapIndexed { index, c -> if (index == 0) false else (c == digits[index - 1]) }
                .none { it }
        ) {
            return false
        }
        return true
    }

    private fun isValidQ2(candidate: Int): Boolean {
        if (!isValidQ1(candidate)) {
            return false
        }
        // Digits never decrease,  no need to check the case like 123232
        return candidate.toString()
            .groupBy { it }
            .mapValues { (_, v) -> v.size }
            .any { (_, v) -> v == 2 }
    }

    override fun question1(): String {
        return range
            .filter { isValidQ1(it) }
            .count()
            .toString()
    }

    override fun question2(): String {
        return range
            .filter { isValidQ2(it) }
            .count()
            .toString()
    }
}

fun main() {
    Day4.validateInput()
    Day4.solve()
}
