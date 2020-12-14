package methods

import dataclasses.Complex
import dataclasses.Matrix
import dataclasses.Rotation
import java.lang.Math.abs
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

class Shift {
    fun Solve(t: Matrix, eps: Double, allowedTime: Int = Int.MAX_VALUE): Pair<MutableList<Complex>, Matrix> {
        val beginTime = System.currentTimeMillis()

        var A = t.copy()
        val orthogonal = Matrix.idMatrix(A.rows)

        val rotator = Rotations()
        val toSubstract = Matrix.idMatrix(A.rows)
        for (i in A.rows - 1 downTo 1) {

            while (true) {
                if (System.currentTimeMillis() - beginTime >= allowedTime) {
                    return Pair(MutableList(A.rows) { Complex.zero }, Matrix(A.rows, A.cols))
                }
                if (A[i][i - 1].abs + A[i - 1][i].abs <= eps / 2.0) {
                    A[i][i - 1] = Complex.zero
                    A[i - 1][i] = Complex.zero
                    break
                }
                val closestNumber = closestEigenValue(A, i)
                val rotations: MutableList<Rotation> = mutableListOf()

                val newMatrix = A - toSubstract * closestNumber
                val (Q, R) = rotator.Solve(newMatrix, rotations)

                for (r in rotations) {
                    rotator.rotateBy(orthogonal, r.i, r.j, r.c, r.s)
                }

                A = R * Q + toSubstract * closestNumber
                for (k in 0 until A.rows) {
                    for (j in 0 until A.cols) {
                        if (kotlin.math.abs(k - j) >= 2)
                            A[k][j] = Complex.zero
                    }
                }

            }
            toSubstract[i][i] = Complex.zero
        }

        return Pair(
            A.data.fold(mutableListOf(),
                { acc: MutableList<Complex>, next: MutableList<Complex> ->
                    acc.add(next[acc.size])
                    acc
                }), orthogonal.transpose
        )

    }

    // (a-1-1 - t) * (a00 - t)  - a-10*a0-1
    // t^2 + a00a-1-1 + (-a-1-1 - a00)t - a-10*a0-1
    // d = (-a-1-1 - a00)^2  +4 *a-10*a0-1
    private fun closestEigenValue(A: Matrix, i: Int): Complex {
        val a = Complex.one
        val b = (-A[i - 1][i - 1] - A[i][i])
        val c = A[i][i] * A[i - 1][i - 1] - A[i - 1][i] * A[i][i - 1]
        val d = Complex(
            (b * b - a * c * 4.0).real.sqrt(Context.mathContext),
            BigDecimal(0.0).setScale(Context.SCALE, RoundingMode.HALF_UP)
        )
        val x1 = (-b + d) / (a * 2.0)
        val x2 = (-b - d) / (a * 2.0)

        return if ((A[i][i] - x1).abs <= (A[i][i] - x2).abs) x1
        else x2
    }
}