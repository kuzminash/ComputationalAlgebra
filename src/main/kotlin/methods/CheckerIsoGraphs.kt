package methods

import dataclasses.Matrix

class CheckerIsoGraphs {
    // true - notIso, false - notKnown
    fun Solve(A: Matrix, B: Matrix, allowedTime: Int = Int.MAX_VALUE): Boolean {

        if (A.rows != B.rows || A.cols != B.cols) return true
        val beginTime = System.currentTimeMillis()
        val (valuesA, QA) = ShiftsAnd3DiagAlgoToGainSpecter().Solve(A, 0.0001, allowedTime)
        val (valuesB, QB) = ShiftsAnd3DiagAlgoToGainSpecter().Solve(B, 0.0001, allowedTime)
        if (System.currentTimeMillis() - beginTime >= allowedTime) return false
        val sortedA = valuesA.sortedBy { it.real }
        val sortedB = valuesB.sortedBy { it.real }

        for (i in 0 until A.rows) {
            if ((sortedA[i] - sortedB[i]).abs >= 0.0002) return true
        }
        return false
    }
}