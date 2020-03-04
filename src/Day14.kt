data class Component(val material: Material, val count: Long) {
    override fun toString(): String {
        return "$count $material"
    }
}

data class Material(val name: String) {
    override fun toString(): String {
        return name
    }
}


object Day14 : Solver() {
    override val day: Int = 14
    override val inputs: List<String> = getInput()

    private val ORE = Material("ORE")
    private val FUEL = Material("FUEL")

    private val components: MutableMap<Material, List<Component>> = mutableMapOf()
    private val reactionOutputs: MutableMap<Material, Int> = mutableMapOf()
    private val materials: MutableSet<Material> = mutableSetOf(ORE)
    private val materialLevels: MutableMap<Material, Int> = mutableMapOf(ORE to 0)

    private fun processInput() {
        for (reactionString in inputs) {
            val inputs = reactionString.split("=>").first().trim()
            val output = reactionString.split("=>").last().trim()

            val outputCount = output.split(" ").first().toInt()
            val outputMaterial = Material(output.split(" ").last())

            val ingredient = inputs.split(",").map {
                val tokens = it.trim().split(" ")
                val cnt = tokens.first().toLong()
                val m = Material(tokens.last())
                Component(material = m, count = cnt)
            }
            materials.add(outputMaterial)
            components[outputMaterial] = ingredient
            reactionOutputs[outputMaterial] = outputCount
        }
    }

    private fun getLevel(material: Material): Int {
        return materialLevels.getOrPut(material,
            {
                components
                    .getValue(material)
                    .map { getLevel(it.material) }
                    .reduce { acc, i -> if (i > acc) i else acc } + 1
            }
        )
    }

    private fun getOre(c: Component): Long {
        var currentFormula = components
            .getValue(c.material)
            .map { Component(it.material, it.count * c.count) }
            .toMutableList()
        var currentLevel = currentFormula.filter { it.count > 0 }.map { getLevel(it.material) }.max()!!
        while (currentLevel > 0) {
            val neededComponents = currentFormula
                .filter { getLevel(it.material) == currentLevel }
                .filter { it.material != ORE }
                .flatMap {
                    currentFormula.remove(it)
                    val needed = it.count
                    val unitOutput = reactionOutputs.getValue(it.material)
                    val reactionCount = (needed / unitOutput) + if (needed % unitOutput == 0L) 0 else 1
                    val rawMaterials = components.getValue(it.material).map { comp ->
                        Component(comp.material, comp.count * reactionCount)
                    }.toMutableList()
                    val wasteOutput = unitOutput * reactionCount - needed
                    if (wasteOutput > 0) rawMaterials.add(Component(it.material, 0 - wasteOutput))
                    rawMaterials.toList()
                }
            currentFormula.addAll(neededComponents)
            currentFormula = currentFormula
                .groupBy { it.material }
                .map { Component(it.key, it.value.map { sub -> sub.count }.sum()) }
                .toMutableList()
            currentLevel = currentFormula.filter { it.count > 0 }.map { getLevel(it.material) }.max()!!
        }
        return currentFormula.first { it.material == ORE }.count
    }

    override fun question1(): String {
        processInput()
        return getOre(Component(FUEL, 1)).toString()
    }

    override fun question2(): String {
        for (i in (3568900 downTo 1)) {
            val oreNeeded = getOre(Component(FUEL, i.toLong()))
            println("$oreNeeded ORE to generate $i FUEL")
            if (oreNeeded < 1000000000000) {
                return i.toString()
            }
        }
        return "NotFound"
    }
}

fun main() {
    Day14.solveFirst()
    Day14.solveSecond()
}