import utility.Location

object Day19 : Solver() {
    override val day: Int = 19
    override val inputs: List<String> = getInput()

    private val baseCodes = inputs[0].split(",").map { it.toLong() }
    private val droneDeploy = IntcodeComputer(baseCodes)
    private val locationStatus: MutableMap<Location, Boolean> = mutableMapOf()

    private fun isInRange(location: Location): Boolean {
        return locationStatus.getOrPut(location) {
            droneDeploy.resetCodes()
            droneDeploy.addInput(location.x)
            droneDeploy.addInput(location.y)
            droneDeploy.run()
            val output = droneDeploy.outputs.first()
            if (output == 1L) return true
            return false
        }
    }

    override fun question1(): String {
        var cnt = 0
        for (x in (0..49))
            for (y in (0..49)) {
                if (isInRange(Location(x, y))) cnt += 1
            }
        return cnt.toString()
    }

    override fun question2(): String {
        var distance = 1
        while (true) {
            println("Checking distance = $distance")
            for (y in (0..distance)) {
                val x = distance - y
                if (!isInRange(Location(x, y))) continue
                val check = (x..x + 100).flatMap { eachX -> (y..y + 100).map { eachY -> Location(eachX, eachY) } }
                    .asSequence()
                    .all { isInRange(it) }
                if (check) return (x * 10000 + y).toString()
            }
            distance += 1
            if (distance >= 9999 * 2) return "Something seems off"
        }
    }
}

fun main() {
    Day19.solve()
}
