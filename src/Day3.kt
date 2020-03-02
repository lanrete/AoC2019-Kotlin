import kotlin.math.abs

data class Point(val x: Int, val y: Int) {
    override fun toString(): String {
        return "($x, $y)"
    }
}

data class Line(val start: Point, val end: Point) {
    override fun toString(): String {
        return "[$start -> $end]"
    }
}
typealias Wire = List<Line>


fun Line.points(): List<Point> {
    return if (start.x == end.x) {
        val range = if (start.y < end.y) (start.y..end.y) else start.y.downTo(end.y)
        range.map {
            Point(start.x, it)
        }
    } else {
        val range = if (start.x < end.x) (start.x..end.x) else start.x.downTo(end.x)
        range.map {
            Point(it, start.y)
        }
    }
}

fun Line.overPoint(p: Point): Boolean {
    if (p == start) {
        return true
    }
    if (p == end) {
        return true
    }
    if ((p.x != start.x) && (p.x != end.x) &&
        (p.y != start.y) && (p.y != end.y)
    ) {
        return false
    }
    if ((start.x == end.x) && (p.x == start.x)) {
        return ((p.y - start.y) * (p.y - end.y) < 0)
    }
    if ((start.y == end.y) && (p.y == start.y)) {
        return ((p.x - start.x) * (p.x - end.x) < 0)
    }
    return false
}

fun Line.intersect(another: Line): Point? {
    return this
        .points()
        .associateWith { another.overPoint(it) }
        .filterValues { it }
        .keys
        .firstOrNull()
}

fun Wire.intersect(l: Line): List<Point> {
    return this.mapNotNull { it.intersect(l) }
}

fun Wire.intersect(another: Wire): List<Point> {
    return another
        .map { this.intersect(it) }
        .flatten()
        .filterNot { it == Point(0, 0) }
}

fun Wire.steps(): Map<Point, Long> {
    var step: Long = 0
    return this
        .map { line ->
            val tempMap = line
                .points().withIndex()
                .associate { Pair(it.value, (it.index + step)) }
            step += line.points().size
            step -= 1
            tempMap.toMutableMap()
        }
        .reduce { cumMap, newMap ->
            newMap.forEach { (k, v) ->
                run {
                    if (!cumMap.containsKey(k)) {
                        cumMap[k] = v
                    }
                }
            }
            cumMap
        }
}


object Day3 : Solver() {
    override val day: Int = 3
    override val inputs: List<String> = getInput()

    private fun generateWire(raw: String): Wire {
        var point = Point(0, 0)
        return raw.split(',').map {
            val dir = it[0]
            val step = it.drop(1).toInt()
            var xm = 0
            var ym = 0
            when (dir) {
                'R' -> xm = 1
                'L' -> xm = -1
                'U' -> ym = 1
                'D' -> ym = -1
            }
            val start = point
            val end = Point(point.x + step * xm, point.y + step * ym)
            point = end
//            println("Adding line from $start to $end")
            Line(start, end)
        }
    }

    private val wires: List<Wire> = inputs.map { generateWire(it) }

    private val intersections = wires[0].intersect(wires[1])

    override fun question1(): String {
        return intersections
            .map { abs(it.x) + abs(it.y) }
            .reduce { a, b -> if (a < b) a else b }
            .toString()
    }

    override fun question2(): String {
        val steps = wires.map { it.steps() }
        return intersections
            .map { intersect ->
                steps
                    .map { map -> map.getValue(intersect) }
                    .reduce { a, b -> a + b }
            }
            .reduce { a, b -> if (a < b) a else b }
            .toString()
    }
}

fun main() {
    Day3.solve()
}
