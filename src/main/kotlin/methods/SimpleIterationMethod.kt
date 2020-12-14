package methods

import dataclasses.Complex
import dataclasses.Matrix

class SimpleIterationMethod {
    fun Solve(A: Matrix, b: Matrix, eps: Double, allowedTime: Int = Int.MAX_VALUE): Matrix {
        val beginTime = System.currentTimeMillis()

        val good = A.data.all { it.sumByDouble { it.abs } <= 1 }
        var badSteps = 0
        var currentVector = Matrix(b.rows, 1)
        for (i in 0 until b.rows)
            currentVector[i][0] = Complex(Context.randomizer.nextDouble(), 0.0)

        while (true) {
            if (System.currentTimeMillis() - beginTime >= allowedTime) {
                return Matrix(b.rows, 1)
            }
            if ((currentVector - A * currentVector - b).abs < eps) return currentVector
            val nextVector = (A * currentVector) + b
            if (nextVector.abs >= currentVector.abs + 1) badSteps++
            else badSteps = 0
            if (badSteps >= 20 && !good) return Matrix(b.rows, 1)
            currentVector = nextVector
        }

    }
}