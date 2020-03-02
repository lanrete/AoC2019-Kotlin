import kotlin.math.abs

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

    fun updateVelocityX(another: Moon) {
        if (another.x > x) vx += 1
        if (another.x < x) vx -= 1
    }

    fun updateVelocityY(another: Moon) {
        if (another.y > y) vy += 1
        if (another.y < y) vy -= 1
    }

    fun updateVelocityZ(another: Moon) {
        if (another.z > z) vz += 1
        if (another.z < z) vz -= 1
    }

    fun updateLocationX() {
        x += vx
    }

    fun updateLocationY() {
        y += vy
    }

    fun updateLocationZ() {
        z += vz
    }

    fun updateVelocity(another: Moon) {
        updateVelocityX(another)
        updateVelocityY(another)
        updateVelocityZ(another)

    }

    fun updateLocation() {
        updateLocationX()
        updateLocationY()
        updateLocationZ()
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

    private fun iterate() {
        moons.forEach { base ->
            moons
                .filter { it != base }
                .forEach { base.updateVelocity(it) }
        }
        moons.forEach { it.updateLocation() }
    }

    private fun iterateX() {
        moons.forEach { base ->
            moons
                .filter { it != base }
                .forEach { base.updateVelocityX(it) }
        }
        moons.forEach { it.updateLocationX() }

    }

    private fun iterateY() {
        moons.forEach { base ->
            moons
                .filter { it != base }
                .forEach { base.updateVelocityY(it) }
        }
        moons.forEach { it.updateLocationY() }

    }

    private fun iterateZ() {
        moons.forEach { base ->
            moons
                .filter { it != base }
                .forEach { base.updateVelocityX(it) }
        }
        moons.forEach { it.updateLocationX() }

    }

    override fun question1(): String {
        repeat(1000) { iterate() }
        return moons.map { it.kineticEnergy * it.potentialEnergy }.reduce { a, b -> a + b }.toString()
    }

    override fun question2(): String {
        moons = resetMoons()
        return ""
    }
}

fun main() {
    Day12.solveSecond()
}