data class Interval(val start: Int, val end: Int) {
    override fun toString() = "$start ~ $end"
}

object Day16 : Solver() {

    data class IntervalTreeNode(
        val start: Int, val end: Int, var sum: Int = 0,
        var leftChild: IntervalTreeNode? = null, var rightChild: IntervalTreeNode? = null
    )

    data class FFTIntervals(val positiveInterval: List<Interval>, val negativeInterval: List<Interval>)

    private fun buildTree(start: Int, end: Int, sequence: List<Int>): IntervalTreeNode? {
        if (start > end) return null
        if (start == end) return IntervalTreeNode(start = start, end = end, sum = sequence[start])

        val root = IntervalTreeNode(start = start, end = end)
        val mid = start + (end - start) / 2

        val left = buildTree(start = start, end = mid, sequence = sequence)
        val right = buildTree(start = mid + 1, end = end, sequence = sequence)

        root.leftChild = left
        root.rightChild = right

        val leftSum = left?.sum ?: 0
        val rightSum = right?.sum ?: 0

        root.sum = leftSum + rightSum
        return root
    }

    override val day: Int = 16
    override val inputs: List<String> = getInput()

    private val startSequence = inputs[0].map { it - '0' }
    private val inputLength = startSequence.size
    private var root = buildTree(0, end = inputLength - 1, sequence = startSequence)!!

    private fun getIntervalSum(root: IntervalTreeNode?, start: Int, end: Int): Int {
        if (root == null) return 0
        if (end < root.start || start > root.end) return 0
        if (root.start == start && root.end == end) return root.sum

        val mid = (root.start + root.end) / 2
        var leftSum = 0
        var rightSum = 0
        if (start <= mid) {
            leftSum = if (mid < end) {
                getIntervalSum(root.leftChild, start = start, end = mid)
            } else {
                getIntervalSum(root.leftChild, start = start, end = end)
            }
        }
        if (mid < end) {
            rightSum = if (start <= mid) {
                getIntervalSum(root.rightChild, start = mid + 1, end = end)
            } else {
                getIntervalSum(root.rightChild, start = start, end = end)
            }
        }
        return leftSum + rightSum
    }

    private fun getIntervals(digit: Int, length: Int): FFTIntervals {
        val positiveIntervals =
            ((0 + digit - 1)..length step digit * 4).map {
                val end = it + digit - 1
                if (end < length) Interval(it, it + digit - 1)
                else Interval(it, length - 1)
            }
        val negativeInterval =
            ((0 + digit * 3 - 1)..length step digit * 4).map {
                val end = it + digit - 1
                if (end < length) Interval(it, it + digit - 1)
                else Interval(it, length - 1)
            }
        return FFTIntervals(positiveIntervals, negativeInterval)
    }

    private fun frequencyTransform(length: Int): List<Int> {
        val newSequence = (0 until length).map {
            val fftIntervals = getIntervals(it + 1, length)
            val positiveInterval = fftIntervals.positiveInterval
            val negativeInterval = fftIntervals.negativeInterval

            val positiveSum = positiveInterval
                .map { interval -> getIntervalSum(root, interval.start, interval.end) }
                .sum()

            val negativeSum = negativeInterval
                .map { interval -> getIntervalSum(root, interval.start, interval.end) }
                .sum()

            (positiveSum - negativeSum).toString().last() - '0'
        }
        root = IntervalTreeNode(0, 1)
        System.gc()
        root = buildTree(0, length - 1, newSequence)!!
        return newSequence
    }

    override fun question1(): String {
        var currentSequence = startSequence
        repeat(100) {
            currentSequence = frequencyTransform(inputLength)
        }
        return currentSequence.subList(0, 8).joinToString("")
    }

    override fun question2(): String {
        val refinedInput = List(10000) { startSequence }.flatten()
        val refinedInputLength = refinedInput.size
        val offset = refinedInput.subList(0, 7).joinToString("").toInt()
        root = buildTree(0, end = refinedInputLength - 1, sequence = refinedInput)!!
        var currentSequence = refinedInput
        repeat(100) {
            currentSequence = frequencyTransform(refinedInputLength)
        }
        return currentSequence.subList(offset, offset + 8).joinToString("")
    }
}

fun main() {
    Day16.solve()
}