object Day16 : Solver() {

    data class IntervalTreeNode(val start: Int, val end: Int)

    override val day: Int = 16
    override val inputs: List<String> = getInput()

    private val startSequence = inputs[0].map { it - '0' }
    private val basePattern = listOf(0, 1, 0, -1)
    private val patterns: MutableMap<Int, List<Int>> = mutableMapOf()

    private fun getPattern(digit: Int): List<Int> {
        return patterns.getOrPut(digit, {
            basePattern.flatMap {
                List(digit) { _ -> it }
            }
        })
    }

    private fun flawedFrequencyTransform(sequence: List<Int>): List<Int> {
        return sequence
            .mapIndexed { index, _ ->
                val pattern = getPattern(index + 1)
                val patternSize = pattern.size
                val newDigit = sequence
                    .mapIndexed { ind, i ->
                        i * pattern[(ind + 1) % patternSize]
                    }
                    .reduce { a, b -> (a + b) }.toString().map { it - '0' }.last()
                newDigit
            }
    }

    override fun question1(): String {
        var currentSequence = startSequence
        repeat(100) {
            currentSequence = flawedFrequencyTransform(currentSequence)
        }
        return currentSequence.subList(0, 8).toString()
    }

    override fun question2(): String {
        TODO("Not Yet")
    }

}

fun main() {
    Day16.validateInput()
}