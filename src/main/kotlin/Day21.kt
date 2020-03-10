object Day21 : Solver() {
    override val day: Int = 21
    override val inputs: List<String> = getInput()

    private val baseCodes = inputs[0].split(",").map { it.toLong() }
    private val springBot = IntcodeComputer(baseCodes)

    private fun String.toAscii(): List<Int> {
        return this.map { it.toInt() }
    }

    private fun List<Long>.asciiToString(): String {
        return this.map { it.toChar() }.joinToString("")
    }

    override fun question1(): String {
        springBot.run()
        var outputs = springBot.outputs
        springBot.outputs = mutableListOf()
        print(outputs.asciiToString())

        /*
        @==>@
        #.??#
         ABCD
        */
//        springBot.addInput("NOT A J\n".toAscii())
//        springBot.addInput("NOT D T\n".toAscii())
//        springBot.addInput("NOT T T\n".toAscii())
//        springBot.addInput("AND T J\n".toAscii())

        /*
        @==>@
        #?.?#
         ABCD
        */
        springBot.addInput("NOT B J\n".toAscii())
        springBot.addInput("NOT D T\n".toAscii())
        springBot.addInput("NOT T T\n".toAscii())
        springBot.addInput("AND T J\n".toAscii())

        /*
        @==>@
        #??.#
         ABCD
        */
//        springBot.addInput("NOT C J\n".toAscii())
//        springBot.addInput("NOT D T\n".toAscii())
//        springBot.addInput("NOT T T\n".toAscii())
//        springBot.addInput("AND T J\n".toAscii())

        springBot.addInput("WALK\n".toAscii())
        springBot.run()
        outputs = springBot.outputs
        print(outputs.asciiToString())
        return "That's it"
    }

    override fun question2(): String {
        TODO("not implemented")
    }
}

fun main() {
    Day21.solveFirst()
}