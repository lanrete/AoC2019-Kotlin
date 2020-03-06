import java.io.File

abstract class Solver {
    abstract val day: Int
    abstract val inputs: List<String>
    fun getInput(): List<String> {
        val fileName = "../input/$day"
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

    fun validateInput() {
        this.inputs.forEach { input -> println(input) }
    }
}