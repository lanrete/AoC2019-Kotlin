data class Material(val name: String) {
    lateinit var ingredients: List<String>
}

data class Reaction(val inputs: List<Pair<String, Int>>, val output: Pair<String, Int>)

object Day14 : Solver() {
    override val day: Int = 14
    override val inputs: List<String> = getInput()

    private const val ORE = "ORE"
    private const val FUEL = "FUEL"

    val materials: MutableMap<String, Material> = mutableMapOf(ORE to Material(ORE))
    val reactions = inputs.map {
        val reactionInput = it
            .split("=").first().split(",").map { ingredient ->
                val tokens = ingredient.trim().split(" ")
                val count = tokens.first().toInt()
                val material = tokens.last()
                Pair(material, count)
            }
        val outputTokens = it.split('>').last().trim().split(" ")
        val outputName = outputTokens.last()
        val outputMaterial = Material(outputName)
        outputMaterial.ingredients = reactionInput.map { input -> input.first }
        materials[outputTokens.last()] = outputMaterial
        val reactionOutput = Pair(outputName, outputTokens.first().toInt())
        Reaction(reactionInput, reactionOutput)
    }

    private val materialDepth: MutableMap<Material, Int> = mutableMapOf(materials[ORE]!! to 1)

    private fun getDepth(material: Material): Int {
        return materialDepth.getOrPut(
            material,
            { material.ingredients.map { getDepth(materials.getValue(it)) }.reduce { a, b -> if (a < b) b else a } + 1 })
    }


    override fun question1(): String {
        val depths = getDepth(materials.getValue(FUEL))
        materials
            .map { it.key to materialDepth.getValue(it.value) }
            .sortedByDescending { it.second }
            .forEach {
                println("${it.first} is a level ${it.second} material")
            }
        return ""
    }

    override fun question2(): String {
        TODO("not implemented")
    }
}

fun main() {
    Day14.solveFirst()
}