package dataclasses

import java.io.File
import java.math.BigDecimal
import java.math.RoundingMode

data class Matrix(
    val rows: Int, val cols: Int,
    val data: MutableList<MutableList<Complex>> = MutableList(rows) { MutableList(cols) { Complex.zero } }
) {

    operator fun get(i: Int) = data[i]

    val conjugate: Matrix
        get() {
            val result = Matrix(cols, rows)
            for (i in 0 until rows) {
                for (j in 0 until cols) {
                    result[j][i] = this[i][j].conjugate
                }
            }
            return result
        }

    val abs: Double
        get() = kotlin.math.sqrt(data.sumByDouble { it.sumByDouble { r: Complex -> r.abs * r.abs } })

    val transpose: Matrix
        get() {
            val result = Matrix(cols, rows)
            for (i in 0 until rows)
                for (j in 0 until cols) {
                    result[j][i] = this[i][j]
                }
            return result
        }

    operator fun plus(other: Matrix): Matrix {
        assert(rows == other.rows && cols == other.cols)
        val result = Matrix(rows, cols)
        for (i in 0 until rows)
            for (j in 0 until cols)
                result[i][j] = this[i][j] + other[i][j]
        return result
    }

    operator fun unaryMinus(): Matrix {
        val result = Matrix(rows, cols)
        for (i in 0 until rows)
            for (j in 0 until cols)
                result[i][j] = -this[i][j]
        return result
    }

    operator fun minus(other: Matrix): Matrix {
        assert(rows == other.rows && cols == other.cols)
        return this + (-other)
    }

    operator fun times(other: Matrix): Matrix {
        assert(cols == other.rows)
        val result = Matrix(rows, other.cols)
        for (i in 0 until rows)
            for (j in 0 until other.cols)
                for (k in 0 until cols)
                    result[i][j] = result[i][j] + this[i][k] * other[k][j]
        return result
    }

    operator fun times(other: Complex): Matrix {
        val result = Matrix(rows, cols)
        for (i in 0 until rows)
            for (j in 0 until cols)
                result[i][j] = this[i][j] * other
        return result
    }

    operator fun times(other: Double): Matrix {
        val result = Matrix(rows, cols)
        for (i in 0 until rows)
            for (j in 0 until cols)
                result[i][j] = this[i][j] * Complex(other, 0.0)
        return result
    }

    operator fun div(other: Double): Matrix = this * (1 / other)

    fun copy(): Matrix {
        val result = Matrix(rows, cols)
        for (i in 0 until rows)
            for (j in 0 until cols)
                result[i][j] = this[i][j]
        return result
    }


    companion object {
        fun fromFile(path: String): Matrix {
            val lines = File(path).readLines()
            val firstLine = lines[0].split(' ').map { it.toInt() }
            assert(firstLine.size == 2)
            val result = Matrix(firstLine[0], firstLine[1])

            for (i in 0 until result.rows) {
                val numbers: List<Double> = lines[i + 1].split(' ').map { it.toDouble() }
                assert(numbers.size == result.cols)
                for (j in 0 until result.cols)
                    result[i][j] = Complex(numbers[j], 0.0)
            }
            return result
        }

        fun idMatrix(n: Int): Matrix {
            val result = Matrix(n, n)
            for (i in 0 until n) {
                result[i][i] = Complex.one
            }
            return result
        }
    }


}
