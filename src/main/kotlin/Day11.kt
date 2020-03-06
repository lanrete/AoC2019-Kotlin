import utility.Direction
import utility.Location

enum class Color {
    BLACK,
    WHITE
}

object Day11 : Solver() {
    override val day: Int = 11
    override val inputs: List<String> = getInput()

    private val baseCode = inputs[0].split(",").map { it.toLong() }
    private val intcodeComputer = IntcodeComputer(baseCode)

    private fun draw(initialColor: Color): MutableMap<Location, Color> {
        intcodeComputer.resetCodes()
        val floor: MutableMap<Location, Color> = mutableMapOf()
        floor[Location(0, 0)] = initialColor
        var current = Location(0, 0)
        var direction = Direction.UP
        while (!intcodeComputer.isHalt()) {
            val input = when (floor.getOrDefault(current, Color.BLACK)) {
                Color.BLACK -> 0L
                Color.WHITE -> 1L
            }
            intcodeComputer.addInput(input)
            intcodeComputer.run()

            val colorToPaint = when (intcodeComputer.outputs.removeAt(0)) {
                0L -> Color.BLACK
                1L -> Color.WHITE
                else -> throw Exception("Something went wrong")
            }
            floor[current] = colorToPaint
            val directionChange = intcodeComputer.outputs.removeAt(0)
            direction = when (directionChange) {
                0L -> direction.turnLeft()
                1L -> direction.turnRight()
                else -> throw Exception("Something went wrong")
            }
            current = current.move(direction)
        }
        return floor
    }

    override fun question1(): String {
        return draw(Color.BLACK).size.toString()
    }

    override fun question2(): String {
        val sb = StringBuilder()
        val floor = draw(Color.WHITE)
        val minimalX = floor.keys.minBy { it.x }!!.x
        val maximalX = floor.keys.maxBy { it.x }!!.x
        val minimalY = floor.keys.minBy { it.y }!!.y
        val maximalY = floor.keys.maxBy { it.y }!!.y

        sb.append("\n")
        for (y in (maximalY.downTo(minimalY))) {
            for (x in (minimalX..maximalX)) {
                val color = floor.getOrDefault(Location(x, y), Color.BLACK)
                sb.append(
                    when (color) {
                        Color.BLACK -> " "
                        Color.WHITE -> "▉"
                    }
                )
            }
            sb.append("\n")
        }
        return sb.toString()
    }
}

fun main() {
    Day11.solve()
}