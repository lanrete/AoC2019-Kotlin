object Day5 : Solver() {
    override val day: Int = 5
    override val inputs: List<String> = getInput()

    private val baseCode = inputs[0]
        .split(",")
        .map { it.toLong() }

    private val computer = IntcodeComputer(baseCode = baseCode)

    override fun question1(): String {
        computer.resetCodes()
        computer.addInput(1)
        computer.run()
        return computer.outputs.joinToString(",")
    }

    override fun question2(): String {
        computer.resetCodes()
        computer.addInput(5)
        computer.run()
        return computer.outputs.joinToString(",")
    }
}

fun main() {
    Day5.solve()
}
