import java.io.File

abstract class Solver(private val debug: Boolean = false) {
    abstract val day: Int
    abstract val inputs: List<String>
    fun getInput(): List<String> {
        val fileName = when (debug) {
            true -> "../input/${day}_test"
            false -> "../input/$day"
        }
        return File(fileName).readLines()
    }

    abstract fun question1(): String
    abstract fun question2(): String

    internal fun solveFirst() {
        val answer1 = question1()
        println("Answer to question 1 ==> $answer1")
    }

    internal fun solveSecond() {
        val answer2 = question2()
        println("Answer to question 2 ==> $answer2")
    }

    fun solve() {
        solveFirst()
        solveSecond()
    }

    fun solveWithTimer() {
        val startTime = System.currentTimeMillis()
        solveFirst()
        val solveFirst = System.currentTimeMillis()
        solveSecond()
        val solveSecond = System.currentTimeMillis()
        println("${(solveFirst - startTime) / 1000.0} seconds to solve Question 1")
        println("${(solveSecond - solveFirst) / 1000.0} seconds to solve Question 2")
    }

    fun validateInput() {
        this.inputs.forEach { input -> println(input) }
    }
}