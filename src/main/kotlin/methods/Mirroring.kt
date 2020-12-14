package methods

import dataclasses.Complex
import dataclasses.Matrix
import java.math.BigDecimal

class Mirroring {

    // Q , R
    fun Solve(t: Matrix): Pair<Matrix, Matrix> {
        var A = t.copy()
        var orthogonal = Matrix.idMatrix(A.rows)

        for (i in 0 until A.cols - 1) {
            val v = Matrix(A.cols - i, 1)
            val e = Matrix(A.cols - i, 1)
            e[0][0] = Complex.one

            for (j in 0 until A.cols - i) {
                v[j][0] = A[j + i][i]
            }
            val u = v / v.abs
            if ((u - e).abs <= 0.000000001) continue
            val os = (u - e) / ((u - e).abs)
            val full = Matrix(A.rows, 1)
            for (j in i until A.rows) {
                full[j][0] = os[j - i][0]
            }

            A = mirrorBy(A, full)
            orthogonal = mirrorBy(orthogonal, full)

        }

        return Pair(orthogonal.transpose, A)
    }


    fun mirrorBy(A: Matrix, b: Matrix): Matrix {
        return A - (b * 2.0) * (b.transpose * A)
    }

    fun mirrorByFromRight(A: Matrix, b: Matrix): Matrix {
        return A - (A * b) * (b.transpose) * 2.0
    }
}