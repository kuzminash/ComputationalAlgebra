import dataclasses.Complex
import dataclasses.Matrix
import methods.*
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.lang.Math.abs
import java.math.BigDecimal
import kotlin.test.assertEquals

class Tests {

    @Test
    fun `test multiplication 1`() {
        val A = readFile("((1,0),(0,1)).txt")
        val B = readFile("((1,0),(0,1)).txt")
        assertEquals(A * B, readFile("((1,0),(0,1)).txt"))
    }

    @Test
    fun `test multiplication 2`() {
        val A = readFile("((1,0),(0,1)).txt")
        assertEquals(A * Complex(3.0, 0.0), readFile("((3,0),(0,3)).txt"))
    }

    @Test
    fun `test simple iteration 1`() {
        val A = readFile("((1,0),(0,1)).txt") * Complex(0.5, 0.0)
        val b = readFile("((4),(4)).txt") * Complex(0.5, 0.0)
        val expected = readFile("((4),(4)).txt")
        val x = SimpleIterationMethod().Solve(A, b, 0.0001)
        assertTrue((x - expected).abs <= 0.001)
    }

    @Test
    fun `test gauss method 1`() {
        val A = readFile("((1,0),(0,1)).txt") * Complex(0.5, 0.0)
        val b = readFile("((4),(4)).txt") * Complex(0.5, 0.0)
        val expected = readFile("((4),(4)).txt")
        val x = GaussMethod().Solve(A, b, 0.0001)
        assertTrue((x - expected).abs <= 0.001)
    }

    @Test
    fun `test gauss method 2`() {
        val A = readFile("((1,0),(0,1)).txt") * Complex(0.25, 0.0)
        val b = readFile("((4),(4)).txt") * Complex(0.5, 0.0)
        val expected = readFile("((4),(4)).txt") * Complex(2.0, 0.0)
        val x = GaussMethod().Solve(A, b, 0.0001)
        assertTrue((x - expected).abs <= 0.001)
    }

    @Test
    fun `test rotation 1`() {
        val A = readFile("((1,1),(1,1)).txt")
        val (Q, R) = Rotations().Solve(A)

        for (i in 0 until 2) {
            for (j in 0 until 2) {
                if (i > j) assertTrue(R[i][j].abs < 0.00000001)
            }
        }
        assertTrue((Q * R - A).abs <= 0.00001)
        assertTrue((Q * Q.transpose - Matrix.idMatrix(2)).abs <= 0.00001)
    }

    @Test
    fun `test rotation 2`() {
        val A = readFile("((1,1),(1,1)).txt")
        val B = Matrix.idMatrix(2)
        val x = A + B
        val (Q, R) = Rotations().Solve(x)
        for (i in 0 until 2) {
            for (j in 0 until 2) {
                if (i > j) assertTrue(R[i][j].abs < 0.00000001)
            }
        }
        assertTrue((Q * R - x).abs <= 0.00001)
        assertTrue((Q * Q.transpose - Matrix.idMatrix(2)).abs <= 0.00001)
    }

    @Test
    fun `test rotation random`() {
        val n = 100
        val A = Matrix(n, n)
        for (i in 0 until n) {
            for (j in 0 until n) {
                A[i][j] = Complex(
                    BigDecimal(Context.randomizer.nextDouble(), Context.mathContext),
                    BigDecimal(0.0, Context.mathContext)
                )
            }
        }
        val (Q, R) = Rotations().Solve(A)
        for (i in 0 until n) {
            for (j in 0 until n) {
                if (i > j) assertTrue(R[i][j].abs < 0.0001)
            }
        }
        assertTrue((Q * R - A).abs <= 0.0001)
        assertTrue((Q * Q.transpose - Matrix.idMatrix(n)).abs <= 0.0001)
    }

    @Test
    fun `test mirroring random`() {
        val n = 100
        val A = Matrix(n, n)
        for (i in 0 until n) {
            for (j in 0 until n) {
                A[i][j] = Complex(
                    BigDecimal(Context.randomizer.nextDouble(), Context.mathContext),
                    BigDecimal(0.0, Context.mathContext)
                )
            }
        }
        val (Q, R) = Mirroring().Solve(A)
        for (i in 0 until n) {
            for (j in 0 until n) {
                if (i > j) assertTrue(R[i][j].abs < 0.0001)
            }
        }
        assertTrue((Q * R - A).abs <= 0.0001)
        assertTrue((Q * Q.transpose - Matrix.idMatrix(n)).abs <= 0.0001)
    }

    @Test
    fun `test eigenvalue simple iteration random`() {
        val n = 100
        val A = Matrix(n, n)
        for (i in 0 until n) {
            for (j in 0 until n) {
                A[i][j] = Complex(
                    BigDecimal(Context.randomizer.nextDouble(), Context.mathContext),
                    BigDecimal(Context.randomizer.nextDouble(), Context.mathContext)
                )
            }
        }
        val (lambda, v) = EigenValueSimpleIteration().Solve(A, 0.0001)
        assertTrue((A * v - v * lambda).abs <= 0.0001)
    }

    @Test
    fun `test QR eigenvalues random`() {
        val n = 20
        val A = Matrix(n, n)
        for (i in 0 until n) {
            for (j in i until n) {
                A[i][j] = Complex(
                    BigDecimal(Context.randomizer.nextDouble(), Context.mathContext),
                    BigDecimal(0.0, Context.mathContext)
                )
                A[j][i] = A[i][j]
            }
        }
        val (values, Q) = QRAlgoToGainSpecter().Solve(A, 0.0001)
        for (i in 0 until Q.cols) {
            val eigenvector = Matrix(Q.rows, 1)
            for (j in 0 until Q.rows) {
                eigenvector[j][0] = Q[j][i]
            }
            assertTrue((A * eigenvector - eigenvector * values[i]).abs <= 0.0001)
        }
        println(values)
    }

    @Test
    fun `test 3diag random`() {
        val n = 50
        val A = Matrix(n, n)
        for (i in 0 until n) {
            for (j in i until n) {
                A[i][j] = Complex(
                    BigDecimal(Context.randomizer.nextDouble(), Context.mathContext),
                    BigDecimal(0.0, Context.mathContext)
                )
                A[j][i] = A[i][j]
            }
        }

        val (newA, Q) = ThreeDiags().Solve(A)
        assertTrue((Q.transpose * A * Q - newA).abs <= 0.0001)
        for (i in 0 until A.rows) {
            for (j in 0 until A.cols) {
                if (kotlin.math.abs(i - j) >= 2) {
                    assertTrue(newA[i][j].abs <= 0.0001)
                }
            }
        }
    }


    @Test
    fun `test QR eigenvalues 3diag random`() {
        val n = 20
        val A = Matrix(n, n)
        for (i in 0 until n) {
            for (j in i until n) {
                if (kotlin.math.abs(i - j) >= 2) continue
                A[i][j] = Complex(
                    BigDecimal(Context.randomizer.nextDouble(), Context.mathContext),
                    BigDecimal(0.0, Context.mathContext)
                )
                A[j][i] = A[i][j]
            }
        }


        val (values, Q) = QRAlgoForThreeDiag().Solve(A, 0.0001)
        for (i in 0 until Q.cols) {
            val eigenvector = Matrix(Q.rows, 1)
            for (j in 0 until Q.rows) {
                eigenvector[j][0] = Q[j][i]
            }
            assertTrue((A * eigenvector - eigenvector * values[i]).abs <= 0.0001)
        }
    }

    @Test
    fun `test QR eigenvalues 3diag shifts random`() {
        val n = 50
        val A = Matrix(n, n)
        for (i in 0 until n) {
            for (j in i until n) {
                if (kotlin.math.abs(i - j) >= 2) continue
                A[i][j] = Complex(
                    BigDecimal(Context.randomizer.nextDouble(), Context.mathContext),
                    BigDecimal(0.0, Context.mathContext)
                )
                A[j][i] = A[i][j]
            }
        }

        val (values, Q) = Shift().Solve(A, 0.0001)
        for (i in 0 until Q.cols) {
            val eigenvector = Matrix(Q.rows, 1)
            for (j in 0 until Q.rows) {
                eigenvector[j][0] = Q[j][i]
            }
            assertTrue((A * eigenvector - eigenvector * values[i]).abs <= 0.0001)
        }
    }

    @Test
    fun `test combined algo to gain specter of symm matrix random`() {
        val n = 50
        val A = Matrix(n, n)
        for (i in 0 until n) {
            for (j in i until n) {
                A[i][j] = Complex(
                    BigDecimal(Context.randomizer.nextDouble(), Context.mathContext),
                    BigDecimal(0.0, Context.mathContext)
                )
                A[j][i] = A[i][j]
            }
        }

        val (values, Q) = ShiftsAnd3DiagAlgoToGainSpecter().Solve(A, 0.0001)
        for (i in 0 until Q.cols) {
            val eigenvector = Matrix(Q.rows, 1)
            for (j in 0 until Q.rows) {
                eigenvector[j][0] = Q[j][i]
            }
            assertTrue((A * eigenvector - eigenvector * values[i]).abs <= 0.0001)
        }
    }


    @Test
    fun `test iso check random`() {
        val n = 20
        val A = Matrix(n, n)
        for (i in 0 until n) {
            for (j in i + 1 until n) {
                val exist = if (Context.randomizer.nextDouble() >= 0.5) Complex.one else Complex.zero
                A[i][j] = exist
                A[j][i] = A[i][j]
            }
        }

        val B = Matrix(n, n)
        for (i in 0 until n) {
            for (j in i + 1 until n) {
                val exist = if (Context.randomizer.nextDouble() >= 0.5) Complex.one else Complex.zero
                B[i][j] = exist
                B[j][i] = B[i][j]
            }
        }

        assert(CheckerIsoGraphs().Solve(A, B, 60000))
    }

    @Test
    fun `test iso check on iso graphs`() {
        val A = readFile("GraphA.txt")
        val B = readFile("GraphB.txt")
        assert(!CheckerIsoGraphs().Solve(A, B, 60000))
    }

    private fun readFile(path: String): Matrix {
        return Matrix.fromFile("src/test/resources/$path")
    }

}