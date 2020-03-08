import utility.Location

object Day19 : Solver() {
    override val day: Int = 19
    override val inputs: List<String> = getInput()

    private val baseCodes = inputs[0].split(",").map { it.toLong() }
    private val droneDeploy = IntcodeComputer(baseCodes)
    private val locationStatus: MutableMap<Location, Boolean> = mutableMapOf()

    private fun isInRange(location: Location): Boolean {
        if (location in locationStatus) return locationStatus.getValue(location)
        droneDeploy.resetCodes()
        droneDeploy.addInput(location.x)
        droneDeploy.addInput(location.y)
        droneDeploy.run()
        val output = droneDeploy.outputs.first()
        val result = output == 1L
        locationStatus[location] = result
        return result
    }

    override fun question1(): String {
        var cnt = 0
        for (y in (0..49))
            for (x in (0..49))
                if (isInRange(Location(x, y))) cnt += 1
        return cnt.toString()
    }

    override fun question2(): String {
        var startPosition = 0
        for (y in (0..9999)) {
            var firstPositive: Int? = null
            for (x in (startPosition..9999)) {
                if (!isInRange(Location(x, y)) && firstPositive == null) continue
                if (!isInRange(Location(x, y)) && firstPositive != null) break
                if (firstPositive == null) firstPositive = x
                val check = (x..x + 99).flatMap { eachX -> (y..y + 99).map { eachY -> Location(eachX, eachY) } }
                    .asSequence()
                    .all { isInRange(it) }
                if (check) return (x * 10000 + y).toString()
            }
            if (firstPositive != null) startPosition = firstPositive
            if (y % 100 == 0) println("$y lines processed")
        }
        return "Nothing found"
    }
}

fun main() {
    Day19.solve()
}
