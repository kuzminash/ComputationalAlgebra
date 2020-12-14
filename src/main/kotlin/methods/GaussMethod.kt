package methods

import dataclasses.Complex
import dataclasses.Matrix

class GaussMethod {
    fun Solve(A: Matrix, b: Matrix, eps: Double, allowedTime: Int = Int.MAX_VALUE): Matrix {
        val beginTime = System.currentTimeMillis()

        val L = Matrix(A.rows, A.cols)
        val U = Matrix(A.rows, A.cols)
        var currentVector = Matrix(A.rows, 1)
        var badSteps = 0
        for (i in 0 until A.rows) currentVector[i][0] = Complex(Context.randomizer.nextDouble(), 0.0)

        for (i in 0 until A.rows) {
            for (j in 0 until A.cols) {
                if (A[i][i] == Complex.zero) return Matrix(A.rows, 1)
                if (i >= j) L[i][j] = A[i][j]
                else U[i][j] = A[i][j]
            }
        }

        while (true) {
            if (System.currentTimeMillis() - beginTime >= allowedTime) {
                return Matrix(A.rows, 1)
            }
            if ((A * currentVector - b).abs < eps) return currentVector
            val nextVector = solveGaussUnderTriangle(L, (-U * currentVector) + b)
            if (nextVector.abs >= currentVector.abs + 1) badSteps++
            else badSteps = 0
            if (badSteps >= 20) return Matrix(A.rows, 1)
            currentVector = nextVector
        }
    }

    //A[i][i] != 0
    private fun solveGaussUnderTriangle(A: Matrix, b: Matrix): Matrix {
        val currentB = b.copy()
        for (i in 0 until A.cols)
            for (j in i + 1 until A.rows) {
                val c = A[j][i] / A[i][i]
                currentB[j][0] += currentB[i][0] * c
            }
        val result = Matrix(A.rows, 1)
        for (i in 0 until A.cols) {
            result[i][0] = currentB[i][0] / A[i][i]
        }
        return result
    }
}