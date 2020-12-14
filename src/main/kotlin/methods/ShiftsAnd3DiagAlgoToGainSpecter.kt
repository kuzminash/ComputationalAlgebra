package methods

import dataclasses.Complex
import dataclasses.Matrix

class ShiftsAnd3DiagAlgoToGainSpecter {
    fun Solve(A: Matrix, eps: Double, allowedTime: Int = Int.MAX_VALUE): Pair<MutableList<Complex>, Matrix> {
        val (newA, Q) = ThreeDiags().Solve(A)
        val (values, D) = Shift().Solve(newA, eps, allowedTime)
        return Pair(values, Q * D)
    }
}