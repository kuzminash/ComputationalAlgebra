package tasks

import dataclasses.Complex
import dataclasses.Matrix
import methods.ShiftsAnd3DiagAlgoToGainSpecter
import java.math.BigDecimal
import java.math.RoundingMode

class FindAlphaForSecondGraph {
    fun Solve(p: Int, allowedTime: Int = Int.MAX_VALUE): BigDecimal {
        val G = Matrix(p + 1, p + 1)
        for (i in 0 until p) {
            G[(i - 1 + p) % p][i] = G[(i - 1 + p) % p][i] + Complex.one
            G[(i + 1) % p][i] = G[(i + 1) % p][i] + Complex.one
            val to = rev(p, i)
            G[to][i] = G[to][i] + Complex.one
        }
        G[0][p] = Complex.one
        G[p][p] = Complex.one * 2.0
        val (values, Q) = ShiftsAnd3DiagAlgoToGainSpecter().Solve(G, 0.00001, allowedTime)
        val sortedValues = values.sortedByDescending { it.real }

        return sortedValues[1].real.abs(Context.mathContext)
            .max(sortedValues.last().real.abs(Context.mathContext)) / (BigDecimal(3.0).setScale(
            Context.SCALE,
            RoundingMode.HALF_UP
        ))

    }

    private fun binpow(x: Int, t: Int, p: Int): Int {
        if (t == 0) return 1
        return if (t % 2 == 1) {
            val temp = binpow(x, t - 1, p)
            (temp * x) % p
        } else {
            val temp = binpow(x, t / 2, p)
            (temp * temp) % p
        }
    }

    private fun rev(p: Int, x: Int): Int {
        if (x == 0) return p
        if (x == p) return 0
        return binpow(x, p - 2, p)
    }

    private fun checkPrime(a: Int): Boolean {
        for (i in 2 until a - 1) {
            if (a % i == 0) return false
        }
        return true
    }

    // take interval [l,r] and find best a for all primes in [l,r]
    fun invokeTask(l: Int, r: Int): MutableList<Pair<Int, BigDecimal>> {
        val result = mutableListOf<Pair<Int, BigDecimal>>()
        for (p in l..r) {
            if (!checkPrime(p)) continue
            val beginTime = System.currentTimeMillis()
            val ans = Solve(p, 3600000)
            if (System.currentTimeMillis() - beginTime >= 3600000) {
                result.add(Pair(-1, ans))
            } else {
                result.add(Pair(p, ans))
            }
        }
        return result
    }
}