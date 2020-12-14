package methods

import dataclasses.Complex
import dataclasses.Matrix
import dataclasses.Rotation
import java.lang.StrictMath.abs
import java.lang.StrictMath.min

class QRAlgoForThreeDiag {

    // lambdas, Q
    fun Solve(A: Matrix, eps: Double, allowedTime: Int = Int.MAX_VALUE): Pair<MutableList<Complex>, Matrix> {
        val beginTime = System.currentTimeMillis()

        var currentMatrix = A
        val eigenvectors = Matrix.idMatrix(A.rows)
        val checker = QRAlgoToGainSpecter()
        val rotator = Rotations()
        while (true) {
            if (System.currentTimeMillis() - beginTime >= allowedTime){
                return Pair(MutableList(A.rows) { Complex.zero }, Matrix(A.rows, A.cols))
            }
            val rotations: MutableList<Rotation> = mutableListOf()
            val (Q, R) = rotator.Solve(currentMatrix, rotations)
            for (r in rotations) {
                rotator.rotateBy(eigenvectors, r.i, r.j, r.c, r.s)
            }
            currentMatrix = specialTimes(R, Q)
            for (i in 0 until currentMatrix.rows) {
                for (j in 0 until currentMatrix.cols) {
                    if (abs(i - j) >= 2) currentMatrix[i][j] = Complex.zero
                }
            }
            if (checker.checkCircles(currentMatrix, eps)) return Pair(
                (currentMatrix).data.fold(mutableListOf(),
                    { acc: MutableList<Complex>, next: MutableList<Complex> ->
                        acc.add(next[acc.size])
                        acc
                    }), eigenvectors.transpose
            )
        }
    }

    private fun specialTimes(R: Matrix, Q: Matrix): Matrix {
        val result = Matrix(R.rows, R.cols)
        for (i in 0 until R.rows) {
            for (j in 0 until R.cols) {
                for (k in i until min(R.rows, i + 3)) {
                    result[i][j] = result[i][j] + R[i][k] * Q[k][j]
                }
            }
        }
        return result
    }
}