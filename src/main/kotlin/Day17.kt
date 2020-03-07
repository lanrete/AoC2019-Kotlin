import utility.Direction
import utility.Location

object Day17 : Solver() {

    enum class MapElement {
        EMPTY {
            override fun toString() = "."
        },
        SCAFFOLD {
            override fun toString() = "#"
        },
    }

    override val day: Int = 17
    override val inputs: List<String> = getInput()

    private val baseCode = inputs[0].split(",").map { it.toLong() }
    private val floor: MutableMap<Location, MapElement> = mutableMapOf()

    private val robot = IntcodeComputer(baseCode = baseCode)
    private var robotLocation = Location(0, 0)
    private var robotDirection = Direction.UP

    private fun isIntersection(location: Location): Boolean {
        val allScaffolds = floor.filter { it.value == MapElement.SCAFFOLD }.keys
        if (location !in allScaffolds) return false
        // A location is an intersection when all four connected locations have scaffold
        return listOf(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT).map {
            location.move(it) in allScaffolds
        }.all { it }
    }

    override fun question1(): String {
        robot.resetCodes()
        robot.run()
        val outputs = robot.outputs
        val stringBuilder = StringBuilder()
        stringBuilder.append('\n')
        var y = 0
        var x = 0
        for (output in outputs) {
            val char = output.toInt().toChar()
            val mapElement = when (char) {
                '.' -> MapElement.EMPTY
                '#' -> MapElement.SCAFFOLD
                'v' -> {
                    robotLocation = Location(x, y)
                    robotDirection = Direction.DOWN
                    MapElement.SCAFFOLD
                }
                '^' -> {
                    robotLocation = Location(x, y)
                    robotDirection = Direction.UP
                    MapElement.SCAFFOLD
                }
                '<' -> {
                    robotLocation = Location(x, y)
                    robotDirection = Direction.LEFT
                    MapElement.SCAFFOLD
                }
                '>' -> {
                    robotLocation = Location(x, y)
                    robotDirection = Direction.RIGHT
                    MapElement.SCAFFOLD
                }
                '\n' -> {
                    y -= 1
                    x = -1
                    null
                }
                else -> throw Exception("Unknown element $char")
            }
            if (mapElement != null) floor[Location(x, y)] = mapElement
            x += 1
            stringBuilder.append(char)
        }

        val intersections = floor
            .filter { it.value == MapElement.SCAFFOLD }
            .keys
            .filter { isIntersection(it) }
        val intersectionAlignParameters = intersections.map { it.x * it.y }

        println(stringBuilder.toString())
        return intersectionAlignParameters.sum().toString()
    }

    override fun question2(): String {
        val allScaffolds = floor.filter { it.value == MapElement.SCAFFOLD }.keys
        val commandSequence = StringBuilder()

        commandSequence.append("\n")
        while (true) {
            if (robotLocation.move(robotDirection) in allScaffolds) { // Move forward, need to find steps
                var step = 1
                var newLocation = robotLocation.move(robotDirection)
                while (newLocation in allScaffolds) {
                    step += 1
                    newLocation = newLocation.move(robotDirection)
                }
                step -= 1
                robotLocation = newLocation.move(robotDirection.turnBack())
                commandSequence.append("$step,")
            } else {
                // Decide the direction to Turn
                if (robotLocation.move(robotDirection.turnLeft()) in allScaffolds) { // can turn Left
                    robotDirection = robotDirection.turnLeft()
                    commandSequence.append("L,")
                } else if (robotLocation.move(robotDirection.turnRight()) in allScaffolds) { // can turn right
                    robotDirection = robotDirection.turnRight()
                    commandSequence.append("R,")
                } else { // No where to go
                    commandSequence.deleteCharAt(commandSequence.length - 1)
                    break
                }
            }
        }

        println(commandSequence.toString())

        val mainRoute = "A,A,B,C,B,C,B,C,C,A\n"
        val functionA = "L,10,R,8,R,8\n"
        val functionB = "L,10,L,12,R,8,R,10\n"
        val functionC = "R,10,L,12,R,10\n"

        val inputs = listOf(mainRoute, functionA, functionB, functionC).flatMap {
            it.map { char -> char.toInt() }
        }
        val newCodes = baseCode.toMutableList()
        newCodes[0] = 2L
        val collectionBot = IntcodeComputer(newCodes)
        for (input in inputs) {
            collectionBot.addInput(input)
        }
        collectionBot.addInput('n'.toInt())
        collectionBot.addInput(10)
        collectionBot.run()

        println(collectionBot.outputs)

        return collectionBot.outputs.last().toString()
    }
}

fun main() {
    Day17.solve()
}