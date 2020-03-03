enum class Tile {
    EMPTY {
        override fun toString(): String = " "
    },
    WALL {
        override fun toString(): String = "*"
    },
    BALL {
        override fun toString(): String = "o"
    },
    BLOCK {
        override fun toString(): String = "="
    },
    PADDLE {
        override fun toString(): String = "-"
    }
}


object Day13 : Solver() {
    override val day: Int = 13
    override val inputs: List<String> = getInput()

    private val baseCode = inputs[0].split(",").map { it.toLong() }
    private val intcodeComputer = IntcodeComputer(baseCode)

    var floor = mapOf<Location, Tile>()

    private fun updateFloor(instructions: List<Int>) {
        floor = instructions
            .withIndex()
            .groupBy { it.index / 3 }
            .mapValues { it.value.map { inner -> inner.value } }
            .values
            .associate { Location(it[0], it[1]) to toTile(it[2]) }
    }

    override fun question1(): String {
        intcodeComputer.resetCodes()
        intcodeComputer.run()
        updateFloor(intcodeComputer.outputs.map { it.toInt() })

        return floor.count { it.value == Tile.BLOCK }.toString()
    }

    private fun toTile(i: Int): Tile {
        return when (i) {
            0 -> Tile.EMPTY
            1 -> Tile.WALL
            2 -> Tile.BLOCK
            3 -> Tile.PADDLE
            4 -> Tile.BALL
            else -> throw Exception("Something went wrong")
        }

    }

    override fun question2(): String {
        val freeArcade = IntcodeComputer(
            baseCode.toMutableList().mapIndexed { index, l -> if (index == 0) 2 else l }
        )
        freeArcade.resetCodes()
        freeArcade.run()
        updateFloor(freeArcade.outputs.map { it.toInt() })
        TODO("Not Yet")
    }
}

fun main() {
    Day13.solveFirst()
}