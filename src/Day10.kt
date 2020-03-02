import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.sqrt

fun gcd(a: Int, b: Int): Int {
    if (a < b) return gcd(b, a)
    if (b != 0) return gcd(b, a % b)
    return a
}

data class Coordinate(val x: Int, val y: Int) {
    override fun toString() = "($x, $y)"
    fun between(other: Coordinate): List<Coordinate> {
        val xGap = other.x - x
        val yGap = other.y - y

        val gcd = gcd(abs(xGap), abs(yGap))
        val xUnitGap = xGap / gcd
        val yUnitGap = yGap / gcd

        return if (gcd > 0) (1 until gcd).map {
            Coordinate(x + it * xUnitGap, y + it * yUnitGap)
        } else ((gcd + 1)..-1).map {
            Coordinate(x + it * xUnitGap, y + it * yUnitGap)
        }
    }
}

typealias Stars = MutableSet<Coordinate>

object Day10 : Solver() {
    override val day: Int = 10
    override val inputs: List<String> = getInput()

    private fun getStars(): Stars {
        val stars = mutableSetOf<Coordinate>()
        inputs.forEachIndexed { y, s ->
            s.forEachIndexed { x, c ->
                if (c == '#') stars.add(Coordinate(x, y))
            }
        }
        return stars
    }

    private var stars = getStars()
    private lateinit var base: Coordinate

    override fun question1(): String {
        val visibleCounts = stars
            .associateWith { base ->
                stars
                    .filterNot { it == base }
                    .count { base.between(it).none { between -> between in stars } }
            }
        base = visibleCounts
            .maxBy { it.value }?.key!!
        println("Base is selected at $base")
        return visibleCounts[base].toString()
    }

    private fun updateVisible(): MutableSet<Coordinate> {
        return stars
            .filterNot { it == base }
            .filter { base.between(it).none { between -> between in stars } }
            .toMutableSet()
    }


    fun angel(other: Coordinate): Double {
        val v1x = 0
        val v1y = 1

        val v2x = (other.x - base.x)
        val v2y = -(other.y - base.y)

        val n1 = sqrt((v1x * v1x + v1y * v1y).toDouble())
        val n2 = sqrt((v2x * v2x + v2y * v2y).toDouble())

        val angel = acos((v1x * v2x + v1y * v2y) / (n1 * n2)) * 180 / PI

        return if (other.x < base.x) 360 - angel
        else angel

    }

    override fun question2(): String {
        var visibleStars = updateVisible()
        var destroyCnt = 1
        while (visibleStars.isNotEmpty()) {
            visibleStars
                .associateWith { angel(it) }
                .toList()
                .sortedBy { it.second }
                .forEach {
                    val destroyedStar = it.first
                    println("$destroyCnt star to destroy is: $destroyedStar")
                    stars.remove(destroyedStar)
                    destroyCnt += 1
                }
            visibleStars = updateVisible()
        }
        return ""
    }
}

fun main() {
    Day10.solve()
}