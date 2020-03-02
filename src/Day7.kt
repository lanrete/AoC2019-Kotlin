object Day7 : Solver() {
    override val day: Int = 7
    override val inputs: List<String> = getInput()

    private val baseCode = inputs[0]
        .split(",")
        .map { it.toLong() }

    private val amplifiers: List<IntcodeComputer> = listOf(
        IntcodeComputer(baseCode),
        IntcodeComputer(baseCode),
        IntcodeComputer(baseCode),
        IntcodeComputer(baseCode),
        IntcodeComputer(baseCode)
    )

    private fun runOnce(): Long {
        amplifiers.forEachIndexed { index, intcodeComputer ->
            run {
                if (index == 0) {
                    intcodeComputer.addInput(0)
                    intcodeComputer.run()
                } else {
                    val previousOutput = amplifiers[index - 1].outputs[0]
                    intcodeComputer.addInput(previousOutput)
                    intcodeComputer.run()
                }
            }
        }
        return amplifiers.last().outputs[0]
    }

    private fun keepRunning(lastOutput: List<Long>) {
        if (amplifiers.all { it.isHalt() }) {
            return
        }
        amplifiers
            .forEachIndexed { index, intcodeComputer ->
                run {
                    if (index == 0) {
                        lastOutput.forEach { intcodeComputer.addInput(it) }
                        amplifiers.last().outputs = mutableListOf()
                        intcodeComputer.run()
                    } else {
                        val previousOutput = amplifiers[index - 1].outputs
                        amplifiers[index - 1].outputs = mutableListOf()
                        previousOutput.forEach { intcodeComputer.addInput(it) }
                        intcodeComputer.run()
                    }
                }
            }
        val loopOutputs = amplifiers.last().outputs
        keepRunning(loopOutputs)
    }

    private fun <T> getPermutation(input: List<T>): List<List<T>> {
        if (input.size == 1) {
            return listOf(listOf(input[0]))
        }
        val firstElement = input[0]
        val remainingElements = input.drop(1)

        return getPermutation(remainingElements)
            .map {
                (0..it.size).map { ind ->
                    run {
                        val newPerm = it.toMutableList()
                        newPerm.add(ind, firstElement)
                        newPerm.toList()
                    }
                }
            }.flatten()
    }

    override fun question1(): String {
        var max = 0L
        getPermutation(listOf(0L, 1L, 2L, 3L, 4L)).forEach {
            run {
                it.forEachIndexed { index, i -> amplifiers[index].addInput(i) }
                val output = runOnce()
                if (output > max) {
                    max = output
                }
                amplifiers.forEach { it.resetCodes() }
            }
        }
        return max.toString()
    }

    override fun question2(): String {
        var max = 0L
        getPermutation(listOf(5L, 6L, 7L, 8L, 9L)).forEach {
            run {
                it.forEachIndexed { index, i -> amplifiers[index].addInput(i) }
                keepRunning(listOf(0))
                val output = amplifiers.last().outputs.last()
                if (output > max) {
                    max = output
                }
                amplifiers.forEach { it.resetCodes() }
            }
        }
        return max.toString()
    }
}

fun main() {
    Day7.solve()
}