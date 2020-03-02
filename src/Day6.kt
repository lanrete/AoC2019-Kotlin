data class Star(val name: String) {
    override fun toString(): String {
        return name
    }

    val orbitedBy: MutableSet<Star> = mutableSetOf()
    lateinit var parent: Star
}

object Day6 : Solver() {
    override val day: Int = 6
    override val inputs: List<String> = getInput()


    private fun processInput(): Map<String, Star> {
        val stars: MutableMap<String, Star> = mutableMapOf()
        inputs.forEach { rule ->
            run {
                val (center, moon) = rule.split(")")
                val centerStar = stars.getOrPut(center, { Star(center) })
                val moonStar = stars.getOrPut(moon, { Star(moon) })

                centerStar.orbitedBy.add(moonStar)
                moonStar.parent = centerStar
            }
        }
        return stars
    }

    private val stars = processInput()
    private var starDepth: MutableMap<Star, Int> = mutableMapOf()
    private var starParentList: MutableMap<Star, List<Star>> = mutableMapOf()

    override fun question1(): String {
        fun getDepth(s: Star): Int {
            if (s == Star("COM")) {
                return 0
            }
            val parent = s.parent
            val parentDepth = starDepth.getOrPut(parent, { getDepth(parent) })
            return parentDepth + 1
        }
        return stars.values.map { getDepth(it) }.reduce { a, b -> a + b }.toString()
    }

    override fun question2(): String {
        fun getParentList(s: Star): List<Star> {
            if (s == Star("COM")) {
                return mutableListOf()
            }
            val parent = s.parent
            val grandParent = starParentList.getOrPut(parent, { getParentList(parent) })
            val parentList = grandParent.toMutableList()
            parentList.add(parent)
            return parentList
        }

        val parentSet = listOf("YOU", "SAN")
            .map { stars.getValue(it) }
            .map { getParentList(it) }
            .map { it.toSet() }
        val commonParent = parentSet.reduce { a, b -> a.intersect(b) }
        return parentSet
            .map { it.subtract(commonParent) }
            .map { it.size }
            .reduce { a, b -> a + b }
            .toString()
    }
}

fun main() {
    Day6.solveFirst()
    Day6.solveSecond()
}