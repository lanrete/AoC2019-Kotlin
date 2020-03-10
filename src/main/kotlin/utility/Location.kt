package utility

import kotlin.math.abs

data class Location(val x: Int, val y: Int) {
    fun move(d: Direction): Location {
        return Location(x + d.modifier.first, y + d.modifier.second)
    }

    fun manhattanDistance(other: Location): Int {
        return abs(x - other.x) + abs(y - other.y)
    }
}