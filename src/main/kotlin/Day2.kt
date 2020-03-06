object Day2 : Solver() {
    override val day: Int = 2
    override val inputs: List<String> = getInput()

    private val baseCode: List<Int> = inputs[0]
        .split(",")
        .map { it.toInt() }
    private var codes: MutableList<Int> = baseCode.toMutableList()

    private fun run(pointer: Int) {
        val op = codes[pointer]
        val a = codes[pointer + 1]
        val b = codes[pointer + 2]
        val c = codes[pointer + 3]

        when (op) {
            1 -> codes[c] = codes[a] + codes[b]
            2 -> codes[c] = codes[a] * codes[b]
            99 -> return
        }
        run(pointer + 4)
    }

    override fun question1(): String {
        codes[1] = 12
        codes[2] = 2
        run(pointer = 0)
        return codes[0].toString()
    }

    override fun question2(): String {
        (0..99).forEach { noun ->
            (0..99).forEach { verb ->
                codes = baseCode.toMutableList()
                codes[1] = noun
                codes[2] = verb
                run(pointer = 0)
                if (codes[0] == 19690720) {
                    return "$noun$verb"
                }
            }
        }
        return "Found Nothing"
    }
}

fun main() {
    Day2.solve()
}