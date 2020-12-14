package tasks

import dataclasses.Complex
import dataclasses.Matrix
import methods.ShiftsAnd3DiagAlgoToGainSpecter
import java.lang.Double.max
import java.math.BigDecimal
import java.math.RoundingMode

class FindAlphaForFirstGraph {
    fun Solve(n: Int, allowedTime: Int = Int.MAX_VALUE): BigDecimal {
        val G = Matrix(n * n, n * n)
        val cnt = MutableList(n * n) { 0 }
        for (i in 0 until n * n) {

            val x = i / n
            val y = i % n


            val to =
                mutableListOf(
                    Pair((x + 2 * y) % n, y),
                    Pair((x - 2 * y + 4 * n) % n, y),
                    Pair((x + (2 * y + 1)) % n, y),
                    Pair((x - (2 * y + 1) + 4 * n) % n, y),
                    Pair(x, (y + 2 * x) % n),
                    Pair(x, (y - 2 * x + 4 * n) % n),
                    Pair(x, (y + (2 * x + 1)) % n),
                    Pair(x, (y - (2 * x + 1) + 4 * n) % n)
                )
            for (p in to) {
                cnt[p.first * n + p.second]++
                G[p.first * n + p.second][i] = G[p.first * n + p.second][i] + Complex.one
            }

        }

        val (values, Q) = ShiftsAnd3DiagAlgoToGainSpecter().Solve(G, 0.00001, allowedTime)
        val sortedValues = values.sortedByDescending { it.real }

        return sortedValues[1].real.max(sortedValues.last().real) / (BigDecimal(8.0).setScale(
            Context.SCALE,
            RoundingMode.HALF_UP
        ))
    }

    // take interval [l,r] and find best for all integers in [l,r]
    fun invokeTask(l: Int, r: Int): MutableList<Pair<Int, BigDecimal>> {
        val result = mutableListOf<Pair<Int, BigDecimal>>()
        for (n in l..r) {
            val beginTime = System.currentTimeMillis()
            val ans = Solve(n, 3600000)
            if (System.currentTimeMillis() - beginTime >= 3600000) {
                result.add(Pair(-1, ans))
            } else {
                result.add(Pair(n, ans))
            }
        }
        return result
    }
}