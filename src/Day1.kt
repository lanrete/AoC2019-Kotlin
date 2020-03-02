import kotlin.math.floor

object Day1 : Solver() {
    override val day = 1
    override val inputs: List<String> = getInput()
    private fun getFuel(mass: Int): Int {
        return (floor(mass / 3.0) - 2).toInt()
    }

    private fun getFuelRecursive(mass: Int): Int {
        val fuel = getFuel(mass)
        if (fuel <= 0) {
            return 0
        }
        return fuel + getFuelRecursive(fuel)
    }

    override fun question1(): String {
        return inputs
            .map { it.toInt() }
            .map { getFuel(it) }
            .reduce { acc, i -> acc + i }
            .toString()
    }

    override fun question2(): String {
        return inputs
            .map { it.toInt() }
            .map { getFuelRecursive(it) }
            .reduce { acc, i -> acc + i }
            .toString()
    }
}

fun main() {
    Day1.solve()
}