import kotlin.math.abs

fun lcm(a: Long, b: Long): Long {
    return a * b / gcd(b, a)
}

enum class Axis {
    X,
    Y,
    Z,
    ALL
}

data class Moon(var x: Int, var y: Int, var z: Int) {
    var vx = 0
    var vy = 0
    var vz = 0

    val potentialEnergy: Int
        get() {
            return abs(x) + abs(y) + abs(z)
        }

    val kineticEnergy: Int
        get() {
            return abs(vx) + abs(vy) + abs(vz)
        }

    override fun toString(): String {
        return "($x, $y, $z) with velocity of ($vx, $vy, $vz)"
    }

    fun updateVelocity(another: Moon, axis: Axis) {
        when (axis) {
            Axis.X -> {
                if (another.x > x) vx += 1
                if (another.x < x) vx -= 1
            }
            Axis.Y -> {
                if (another.y > y) vy += 1
                if (another.y < y) vy -= 1
            }
            Axis.Z -> {
                if (another.z > z) vz += 1
                if (another.z < z) vz -= 1
            }
            Axis.ALL -> {
                for (it in listOf(Axis.X, Axis.Y, Axis.Z)) updateVelocity(another, it)
            }
        }
    }

    fun updateLocation(axis: Axis) {
        when (axis) {
            Axis.X -> x += vx
            Axis.Y -> y += vy
            Axis.Z -> z += vz
            Axis.ALL -> {
                for (it in listOf(Axis.X, Axis.Y, Axis.Z)) updateLocation(it)
            }
        }
    }
}

object Day12 : Solver() {
    override val day: Int = 12
    override val inputs: List<String> = getInput()

    private fun resetMoons(): List<Moon> {
        return inputs.map {
            val x = it.split(",").first().split("=").last().toInt()
            val y = it.split(",")[1].split("=").last().toInt()
            val z = it.split("=").last().split(">").first().toInt()
            Moon(x, y, z)
        }
    }

    private var moons = resetMoons()

    private fun iterate(axis: Axis) {
        moons.forEach { base ->
            moons
                .filter { it != base }
                .forEach { base.updateVelocity(it, axis) }
        }
        moons.forEach { it.updateLocation(axis) }
    }


    override fun question1(): String {
        repeat(1000) { iterate(Axis.ALL) }
        return moons.map { it.kineticEnergy * it.potentialEnergy }.reduce { a, b -> a + b }.toString()
    }


    private fun findLoop(axis: Axis): Int {
        moons = resetMoons()
        val initialStage = resetMoons()
        var cnt = 0
        do {
            iterate(axis)
            cnt += 1
        } while (moons.zip(initialStage).any { it.first != it.second })
        return cnt + 1
    }

    override fun question2(): String {
        return listOf(Axis.X, Axis.Y, Axis.Z)
            .map {
                val cycle = findLoop(it)
                println("$cycle steps needed on Axis $it")
                cycle.toLong()
            }
            .reduce { acc, i -> lcm(acc, i) }
            .toString()
    }
}

fun main() {
    Day12.solveFirst()
    Day12.solveSecond()
}