package utility

data class Location(val x: Int, val y: Int) {
    fun move(d: Direction): Location {
        return Location(x + d.modifier.first, y + d.modifier.second)
    }
}