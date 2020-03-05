import utility.Direction
import utility.Location


object Day15 : Solver() {

    enum class Status {
        EMPTY {
            override fun toString() = "."
        },
        WALL {
            override fun toString() = "▉"
        },
        OXYGEN {
            override fun toString() = "*"
        },
        START {
            override fun toString() = "O"
        }
    }

    data class Trace(val location: Location, val path: List<Direction>)

    override val day: Int = 15
    override val inputs: List<String> = getInput()

    private val baseCodes = inputs[0].split(",").map { it.toLong() }
    private val drone = IntcodeComputer(baseCodes)
    private val startPoint = Location(0, 0)
    private val allDirection = listOf(Direction.DOWN, Direction.UP, Direction.LEFT, Direction.RIGHT)

    private fun getDroneInput(direction: Direction): Int {
        return when (direction) {
            Direction.UP -> 1
            Direction.DOWN -> 2
            Direction.LEFT -> 3
            Direction.RIGHT -> 4
        }
    }

    private fun explore(range: IntRange): Map<Location, Status> {
        drone.resetCodes()
        val floor: MutableMap<Location, Status> = mutableMapOf()
        val seen: MutableSet<Location> = mutableSetOf()
        val stack: MutableList<Trace> = mutableListOf()

        seen.add(startPoint)
        stack.add(Trace(startPoint, emptyList()))
        while (stack.isNotEmpty()) {
            val currentTrace = stack.removeAt(stack.size - 1)
            val location = currentTrace.location
            val path = currentTrace.path

            // Rest drone to current location
            drone.resetCodes()
            if (path.isNotEmpty()) {
                for (it in path) drone.addInput(getDroneInput(it))
                drone.run()
            }

            // Try all four directions
            for (direction in allDirection) {
                val newLocation = location.move(direction)
                if (newLocation in seen) continue
                if ((newLocation.x !in range) || (newLocation.y !in range)) continue
                drone.addInput(getDroneInput(direction))
                drone.run()
                val rawOutput = drone.outputs.last()
                val status = when (rawOutput.toInt()) {
                    0 -> Status.WALL
                    1 -> Status.EMPTY
                    2 -> Status.OXYGEN
                    else -> throw Exception("Unknown output $rawOutput")
                }

                if (status != Status.WALL) {
                    // If find a empty tile, then reset to current location
                    drone.addInput(getDroneInput(direction.turnBack()))
                    drone.run()

                    // Add the new location to stack
                    val newPath = path.toMutableList()
                    newPath.add(direction)
                    stack.add(Trace(newLocation, newPath))
                }

                seen.add(newLocation)
                floor[newLocation] = status
            }
        }

        floor[startPoint] = Status.START
        return floor
    }

    private fun paintFloor(floor: Map<Location, Status>): String {
        val stringBuilder = StringBuilder()
        val minimalX = floor.map { it.key.x }.min()!!
        val maximalX = floor.map { it.key.x }.max()!!
        val minimalY = floor.map { it.key.y }.min()!!
        val maximalY = floor.map { it.key.y }.max()!!

        for (y in maximalY downTo minimalY) {
            for (x in (minimalX..maximalX)) {
                stringBuilder.append(floor.getOrDefault(Location(x, y), " "))
            }
            stringBuilder.appendln()
        }
        return stringBuilder.toString()
    }

    private fun floodFill(floor: Map<Location, Status>, origin: Location = startPoint): Map<Location, Int> {
        val distance = mutableMapOf(origin to 0)

        val queue = mutableListOf(origin)

        while (queue.isNotEmpty()) {
            val currentLocation = queue.removeAt(0)
            val currentDistance = distance.getValue(currentLocation)
            for (direction in allDirection) {
                val newLocation = currentLocation.move(direction)
                if (floor.getOrDefault(newLocation, Status.WALL) == Status.WALL) continue
                if (newLocation in distance.keys) continue

                distance[newLocation] = currentDistance + 1
                queue.add(newLocation)
            }
        }
        return distance
    }

    override fun question1(): String {
        val floor = explore(-500..500)
        val distance = floodFill(floor)
        val minimalDistance = floor
            .filter { (_, v) -> v == Status.OXYGEN }
            .map { distance.getValue(it.key) }
            .reduce { acc, i -> if (i < acc) i else acc }

        return "\n${paintFloor(floor)}\n$minimalDistance"
    }


    override fun question2(): String {
        val floor = explore(-500..500)
        val oxygenPoint = floor.filter { it.value == Status.OXYGEN }.keys.first()
        val distance = floodFill(floor, oxygenPoint)
        val maximum = floor
            .filter { (_, v) -> v != Status.WALL }
            .map { distance.getValue(it.key) }
            .reduce { acc, i -> if (i > acc) i else acc }
        return maximum.toString()
    }
}

fun main() {
    Day15.solve()
}