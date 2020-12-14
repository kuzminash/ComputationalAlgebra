import dataclasses.Matrix
import tasks.FindAlphaForFirstGraph
import tasks.FindAlphaForSecondGraph
import java.math.MathContext
import kotlin.random.Random

object Context {
    val mathContext = MathContext(30)
    val randomizer = Random(System.currentTimeMillis())
    const val SCALE = 30
}


fun main() {
    println(FindAlphaForFirstGraph().invokeTask(2, 4))
    println(FindAlphaForSecondGraph().invokeTask(8, 15))
}