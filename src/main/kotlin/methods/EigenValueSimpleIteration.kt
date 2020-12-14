package methods

import dataclasses.Complex
import dataclasses.Matrix
import java.math.BigDecimal
import java.math.RoundingMode

class EigenValueSimpleIteration {
    fun Solve(A: Matrix, eps: Double, allowedTime: Int = Int.MAX_VALUE): Pair<Complex, Matrix> {
        var currentVector = Matrix(A.rows, 1)
        val beginTime = System.currentTimeMillis()
        for (i in 0 until A.rows) {
            currentVector[i][0] =
                Complex(
                    BigDecimal(Context.randomizer.nextDouble()).setScale(Context.SCALE, RoundingMode.HALF_UP),
                    BigDecimal(Context.randomizer.nextDouble()).setScale(Context.SCALE, RoundingMode.HALF_UP)
                )
        }
        currentVector = currentVector / currentVector.abs

        while (true) {
            if (System.currentTimeMillis() - beginTime >= allowedTime) return Pair(Complex.zero, Matrix(A.rows, 1))
            val lambda = (currentVector.conjugate * A * currentVector)

            if ((A * currentVector - currentVector * lambda).abs < eps) return Pair(lambda[0][0], currentVector)
            val next = A * currentVector
            currentVector = next / next.abs
        }
    }
}