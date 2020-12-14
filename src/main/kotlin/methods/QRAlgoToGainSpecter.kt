package methods

import dataclasses.Complex
import dataclasses.Matrix

class QRAlgoToGainSpecter {
    fun Solve(A: Matrix, eps: Double, allowedTime: Int = Int.MAX_VALUE): Pair<MutableList<Complex>, Matrix> {
        val beginTime = System.currentTimeMillis()
        var currentMatrix = A
        var eigenvectors = Matrix.idMatrix(A.rows)
        while (true) {
            if (System.currentTimeMillis() - beginTime >= allowedTime) {
                return Pair(MutableList(A.rows) { Complex.zero }, Matrix(A.rows, A.cols))
            }
            val (Q, R) = Rotations().Solve(currentMatrix)
            eigenvectors = eigenvectors * Q
            currentMatrix = R * Q
            if (checkCircles(currentMatrix, eps)) return Pair(
                (currentMatrix).data.fold(mutableListOf(),
                    { acc: MutableList<Complex>, next: MutableList<Complex> ->
                        acc.add(next[acc.size])
                        acc
                    }), eigenvectors
            )
        }
    }

    fun checkCircles(A: Matrix, eps: Double): Boolean {
        for (i in 0 until A.rows) {
            var currentSum = 0.0
            for (j in 0 until A.cols) {
                if (i == j) continue
                currentSum += A[i][j].abs
            }
            if (currentSum >= eps) return false
        }
        return true
    }
}