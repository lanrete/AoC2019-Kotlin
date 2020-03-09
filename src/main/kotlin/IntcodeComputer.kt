enum class Status {
    Idle,
    Halt,
    Running,
    Pending
}

enum class ParameterMode {
    Position,
    Immediate,
    Relative
    ;
}

enum class Opcode {
    Add {
        override val size: Int = 4
    },
    Multiply {
        override val size: Int = 4
    },
    Input {
        override val size: Int = 2
    },
    Output {
        override val size: Int = 2
    },
    JumpIfTrue {
        override val size: Int = 3
    },
    JumpIfFalse {
        override val size: Int = 3
    },
    LessThen {
        override val size: Int = 4
    },
    Equal {
        override val size: Int = 4
    },
    Terminate {
        override val size: Int = 1
    },
    AdjustBase {
        override val size: Int = 2
    }
    ;

    abstract val size: Int
}

data class Command(val modes: List<ParameterMode>, val opcode: Opcode)

class IntcodeComputer(private val baseCode: List<Long>) {
    var outputs: MutableList<Long> = mutableListOf()
    private var status = Status.Idle
    private var pointer: Long = 0
    private var relativeBase: Long = 0
    private var inputs: MutableList<Long> = mutableListOf()
    private var codes = baseCode
        .withIndex()
        .associate { (ind, value) -> Pair(ind.toLong(), value) }
        .toMutableMap()

    fun addInput(it: List<Int>) {
        for (int in it) addInput(int)
    }

    fun addInput(it: Long) {
        inputs.add(it)
        return
    }

    fun addInput(it: Int) {
        addInput(it.toLong())
    }

    fun resetCodes() {
        codes = baseCode
            .withIndex()
            .associate { (ind, value) -> Pair(ind.toLong(), value) }
            .toMutableMap()
        pointer = 0L
        inputs = mutableListOf()
        outputs = mutableListOf()
        status = Status.Idle
        relativeBase = 0
    }

    private fun parseCommand(): Command {
        val raw = codes.getValue(pointer)
        val opcode = when ((raw % 100).toInt()) {
            1 -> Opcode.Add
            2 -> Opcode.Multiply
            3 -> Opcode.Input
            4 -> Opcode.Output
            5 -> Opcode.JumpIfTrue
            6 -> Opcode.JumpIfFalse
            7 -> Opcode.LessThen
            8 -> Opcode.Equal
            9 -> Opcode.AdjustBase
            99 -> Opcode.Terminate
            else -> throw Exception("Unknown opcode")
        }

        fun parseMode(it: Int): ParameterMode {
            return when (it) {
                0 -> ParameterMode.Position
                1 -> ParameterMode.Immediate
                2 -> ParameterMode.Relative
                else -> throw Exception("Unknown mode")
            }
        }

        val modes = listOf(100, 1000, 10000)
            .map { raw / it % 10 }
            .map { parseMode(it.toInt()) }
        return Command(modes, opcode)
    }

    private fun getValue(v: Long, m: ParameterMode): Long {
        return when (m) {
            ParameterMode.Position -> codes.getOrDefault(v, 0L)
            ParameterMode.Immediate -> v
            ParameterMode.Relative -> codes.getOrDefault(relativeBase + v, 0L)
        }
    }

    private fun getIndex(v: Long, m: ParameterMode): Long {
        return when (m) {
            ParameterMode.Relative -> relativeBase + v
            ParameterMode.Position -> v
            ParameterMode.Immediate -> throw Exception("This shouldn't happen ")
        }
    }

    fun run() {
        if (status == Status.Halt) {
            return
        }
        status = Status.Running
        var instruction = parseCommand()
        while (instruction.opcode != Opcode.Terminate) {
            val opcode = instruction.opcode
            val modes = instruction.modes
            when (opcode) {
                Opcode.Add -> {
                    val v1 = getValue(codes.getValue(pointer + 1), modes[0])
                    val v2 = getValue(codes.getValue(pointer + 2), modes[1])
                    val v3 = getIndex(codes.getValue(pointer + 3), modes[2])
                    codes[v3] = v1 + v2
                }
                Opcode.Multiply -> {
                    val v1 = getValue(codes.getValue(pointer + 1), modes[0])
                    val v2 = getValue(codes.getValue(pointer + 2), modes[1])
                    val v3 = getIndex(codes.getValue(pointer + 3), modes[2])
                    codes[v3] = v1 * v2
                }
                Opcode.Input -> {
                    val v1 = getIndex(codes.getValue(pointer + 1), modes[0])
                    if (inputs.isEmpty()) {
                        status = Status.Pending
                        return
                    }
                    codes[v1] = inputs.removeAt(0)
                }
                Opcode.Output -> {
                    val v1 = getValue(codes.getValue(pointer + 1), modes[0])
                    outputs.add(v1)
                }
                Opcode.JumpIfTrue -> {
                    val v1 = getValue(codes.getValue(pointer + 1), modes[0])
                    val v2 = getValue(codes.getValue(pointer + 2), modes[1])
                    if (v1 != 0L) {
                        pointer = v2
                        pointer -= opcode.size
                    }
                }
                Opcode.JumpIfFalse -> {
                    val v1 = getValue(codes.getValue(pointer + 1), modes[0])
                    val v2 = getValue(codes.getValue(pointer + 2), modes[1])
                    if (v1 == 0L) {
                        pointer = v2
                        pointer -= opcode.size
                    }
                }
                Opcode.LessThen -> {
                    val v1 = getValue(codes.getValue(pointer + 1), modes[0])
                    val v2 = getValue(codes.getValue(pointer + 2), modes[1])
                    val v3 = getIndex(codes.getValue(pointer + 3), modes[2])
                    if (v1 < v2) {
                        codes[v3] = 1
                    } else {
                        codes[v3] = 0
                    }
                }
                Opcode.Equal -> {
                    val v1 = getValue(codes.getValue(pointer + 1), modes[0])
                    val v2 = getValue(codes.getValue(pointer + 2), modes[1])
                    val v3 = getIndex(codes.getValue(pointer + 3), modes[2])
                    if (v1 == v2) {
                        codes[v3] = 1
                    } else {
                        codes[v3] = 0
                    }
                }
                Opcode.AdjustBase -> {
                    val v1 = getValue(codes.getValue(pointer + 1), modes[0])
                    relativeBase += v1
                }
                Opcode.Terminate -> {
                    status = Status.Halt
                    return
                }
            }
            pointer += opcode.size
            instruction = parseCommand()
        }
        status = Status.Halt
        return
    }

    fun isHalt(): Boolean {
        return when (status) {
            Status.Idle -> false
            Status.Halt -> true
            Status.Running -> false
            Status.Pending -> false
        }
    }
}