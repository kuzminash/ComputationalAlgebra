package methods

import dataclasses.Complex
import dataclasses.Matrix

class ThreeDiags {

    // A' , Q
    fun Solve(t: Matrix): Pair<Matrix, Matrix> {
        var A = t.copy()
        var orthogonal = Matrix.idMatrix(A.rows)
        val mirrorer = Mirroring()

        for (i in 0 until A.cols - 2) {
            val underDiag = Matrix(A.rows - i - 1, 1)
            val e = Matrix(A.rows - i - 1, 1)
            e[0][0] = Complex.one
            for (j in i + 1 until A.rows) {
                underDiag[j - i - 1][0] = A[j][i]
            }
            if (underDiag.abs <= 0.00000001) continue

            val u = underDiag / underDiag.abs
            val delta = u - e

            if (delta.abs <= 0.00000001) continue

            val os = delta / delta.abs
            val full = Matrix(A.rows, 1)
            for (j in i + 1 until A.rows) {
                full[j][0] = os[j - i - 1][0]
            }
            A = mirrorer.mirrorByFromRight(mirrorer.mirrorBy(A, full), full)
            orthogonal = mirrorer.mirrorBy(orthogonal, full)
        }

        return Pair(A, orthogonal.transpose)
    }
}