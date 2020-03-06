package utility

enum class Direction {
    UP {
        override val modifier: Pair<Int, Int> = Pair(0, 1)
        override fun turnLeft() = LEFT
        override fun turnRight() = RIGHT
        override fun turnBack() = DOWN
    },
    DOWN {
        override val modifier: Pair<Int, Int> = Pair(0, -1)
        override fun turnLeft() = RIGHT
        override fun turnRight() = LEFT
        override fun turnBack() = UP
    },
    LEFT {
        override val modifier: Pair<Int, Int> = Pair(-1, 0)
        override fun turnLeft() = DOWN
        override fun turnRight() = UP
        override fun turnBack() = RIGHT
    },
    RIGHT {
        override val modifier: Pair<Int, Int> = Pair(1, 0)
        override fun turnLeft() = UP
        override fun turnRight() = DOWN
        override fun turnBack() = LEFT
    },
    ;

    abstract val modifier: Pair<Int, Int>
    abstract fun turnLeft(): Direction
    abstract fun turnRight(): Direction
    abstract fun turnBack(): Direction
}