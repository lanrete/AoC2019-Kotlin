object Day9 : Solver() {
    override val day: Int = 9
    override val inputs: List<String> = getInput()

    private val baseCodes = inputs[0].split(",").map { it.toLong() }
    private val intcodeComputer = IntcodeComputer(baseCode = baseCodes)

    override fun question1(): String {
        intcodeComputer.resetCodes()
        intcodeComputer.addInput(1)
        intcodeComputer.run()
        return intcodeComputer.outputs.joinToString(",")
    }

    override fun question2(): String {
        intcodeComputer.resetCodes()
        intcodeComputer.addInput(2)
        intcodeComputer.run()
        return intcodeComputer.outputs.joinToString(",")
    }
}

fun main() {
    Day9.solve()
}
