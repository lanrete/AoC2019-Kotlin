# What is Advent of Code

AoC (Advent of Code) is annual event hosted by 
[Eric Wastl](http://was.tl/). It provides a set of 
programming puzzles that can be solved within any language of choice.

The actual puzzle of AoC 2019 can be [here](https://adventofcode.com/2019).

# What is this Repo

I am learning Kotlin as my next programming language, AoC is just a perfect
use case for puzzle-cracking as well as learning the awesome features in Kotlin.

I will try to solve all 25 puzzles with Kotlin here and perhaps share my solution.

## Input data

Although the puzzles are same for everyone, each user will get different puzzle
inputs, for that reason I didn't include the input in this repo.

By default, input data should be available in below directory with the day count
as the file name like below.

```
|-Repository Root
|  |-src
|     |-Day1.kt
|     |-Day2.kt
|     |-....kt
|
|-input
   |-1
   |-2
   |-..
```

The `input` directory is being put at this location cause I am using the same location
for the solution in other languages like `python` or `ruby`.

If you would like to change the relative path where inputs are located, please modify
`fun getInput()` in `Solver.kt`

