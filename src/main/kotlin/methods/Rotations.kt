package methods

import dataclasses.Complex
import dataclasses.Matrix
import dataclasses.Rotation
import java.math.BigDecimal
import java.math.RoundingMode

class Rotations {
    // Q , R
    fun Solve(t: Matrix, rotations: MutableList<Rotation> = mutableListOf()): Pair<Matrix, Matrix> {
        val A = t.copy()
        val orthogonal = Matrix.idMatrix(A.rows)
        for (i in 0 until A.cols) {
            var index = -1
            for (j in i until A.rows) {
                if (A[j][i].abs >= 0.0000001) {
                    index = j
                    break
                }
            }
            if (index == -1) continue
            if (index != i) {
                rotateBy(A, i, index, Complex.zero, Complex.one)
                rotateBy(orthogonal, i, index, Complex.zero, Complex.one)
                rotations.add(Rotation(i, index, Complex.zero, Complex.one))
            }

            for (j in i + 1 until A.rows) {
                if (A[j][i].abs >= 0.0000001) {
                    val d = (A[j][i] / A[i][i])
                    val c =
                        Complex(
                            (Complex.one / (Complex.one + d * d)).real.sqrt(Context.mathContext),
                            BigDecimal(0.0, Context.mathContext)
                        )
                    var s = (Complex(
                        BigDecimal(1.0, Context.mathContext).setScale(Context.SCALE, RoundingMode.HALF_UP),
                        BigDecimal(0.0, Context.mathContext).setScale(Context.SCALE, RoundingMode.HALF_UP)
                    ) - c * c).real.sqrt(Context.mathContext)
                    if (d.real.toDouble() < 0) s = s.negate(Context.mathContext)

                    val st = Complex(s, BigDecimal(0.0, Context.mathContext))

                    rotateBy(A, i, j, c, st)
                    rotateBy(orthogonal, i, j, c, st)
                    rotations.add(Rotation(i, j, c, st))
                }
            }
        }
        return Pair(orthogonal.transpose, A)
    }


    fun rotateBy(A: Matrix, i: Int, j: Int, c: Complex, s: Complex) {
        for (k in 0 until A.cols) {
            A[i][k] = A[i][k] * c + A[j][k] * s.also { A[j][k] = (-A[i][k]) * s + A[j][k] * c }
        }
    }
}