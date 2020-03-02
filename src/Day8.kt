object Day8 : Solver() {
    override val day: Int = 8
    override val inputs: List<String> = getInput()

    private const val width = 25
    private const val height = 6

    private val pixels = inputs[0].map { it - '0' }

    private val pixelLayer = pixels
        .withIndex()
        .groupBy { it.index / (width * height) }
        .map { eachLayer -> eachLayer.value.map { it.value } }

    override fun question1(): String {
        val targetLayer = pixelLayer
            .map { eachLayer -> eachLayer.count { it == 0 } }
            .withIndex()
            .reduce { acc, indexedValue -> if (indexedValue.value < acc.value) indexedValue else acc }
            .index

        return listOf(1, 2)
            .map { value -> pixelLayer[targetLayer].count { it == value } }
            .reduce { a, b -> a * b }
            .toString()
    }

    override fun question2(): String {
        val finalLayer = pixelLayer
            .reduce { last: List<Int>, pixels: List<Int> ->
                last
                    .zip(pixels)
                    .map {
                        val (final, new) = it
                        val output = if (final == 2) new else final
                        output
                    }
            }
        finalLayer
            .withIndex()
            .groupBy { it.index / width }
            .forEach { (_, list) ->
                list
                    .map {
                        when (it.value) {
                            1 -> '▉'
                            else -> ' '
                        }
                    }
                    .forEach { print(it) }
                println()
            }
        return ""
    }
}

fun main() {
    Day8.solve()
}