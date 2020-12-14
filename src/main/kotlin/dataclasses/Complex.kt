package dataclasses

import java.math.BigDecimal
import java.math.BigDecimal.ROUND_HALF_EVEN
import java.math.BigDecimal.ROUND_UP
import java.math.RoundingMode

data class Complex(val real: BigDecimal, val im: BigDecimal) {
    companion object {
        val zero = Complex(
            BigDecimal(0.0, Context.mathContext).setScale(Context.SCALE, RoundingMode.HALF_UP),
            BigDecimal(0.0, Context.mathContext).setScale(Context.SCALE, RoundingMode.HALF_UP),
        )
        val one = Complex(
            BigDecimal(1.0, Context.mathContext).setScale(Context.SCALE, RoundingMode.HALF_UP),
            BigDecimal(0.0, Context.mathContext).setScale(Context.SCALE, RoundingMode.HALF_UP)
        )
    }

    constructor(real: Double, im: Double) : this(
        BigDecimal(real, Context.mathContext).setScale(Context.SCALE, RoundingMode.HALF_UP),
        BigDecimal(im, Context.mathContext).setScale(Context.SCALE, RoundingMode.HALF_UP)
    )

    operator fun plus(other: Complex): Complex =
        Complex(
            real.add(other.real, Context.mathContext).setScale(Context.SCALE, RoundingMode.HALF_UP),
            im.add(other.im, Context.mathContext).setScale(Context.SCALE, RoundingMode.HALF_UP)
        )

    operator fun unaryMinus(): Complex = Complex(
        real.negate(Context.mathContext).setScale(Context.SCALE, RoundingMode.HALF_UP),
        im.negate(Context.mathContext).setScale(Context.SCALE, RoundingMode.HALF_UP)
    );

    operator fun minus(other: Complex): Complex = this + (-other)

    val abs: Double
        get() = (real * real + im * im).setScale(Context.SCALE, RoundingMode.HALF_UP)
            .sqrt(Context.mathContext).toDouble()

    val conjugate: Complex
        get() = Complex(
            real,
            im.negate().setScale(Context.SCALE, RoundingMode.HALF_UP)
        )

    private val rev: Complex
        get() {
            val scale = (real * real + im * im).setScale(Context.SCALE, RoundingMode.HALF_UP)
            return Complex(
                real.divide(scale, Context.mathContext).setScale(Context.SCALE, RoundingMode.HALF_UP),
                im.divide(scale, Context.mathContext).negate(Context.mathContext)
                    .setScale(Context.SCALE, RoundingMode.HALF_UP)
            )
        }

    operator fun times(other: Complex): Complex =
        Complex(
            (real * other.real - im * other.im).setScale(Context.SCALE, RoundingMode.HALF_UP),
            (real * other.im + im * other.real).setScale(Context.SCALE, RoundingMode.HALF_UP)
        )


    operator fun times(other: Double): Complex =
        Complex(
            (real * (BigDecimal(other).setScale(Context.SCALE, RoundingMode.HALF_UP))).setScale(
                Context.SCALE,
                RoundingMode.HALF_UP
            ),
            (im * (BigDecimal(other).setScale(Context.SCALE, RoundingMode.HALF_UP)).setScale(
                Context.SCALE,
                RoundingMode.HALF_UP
            ))
        )

    operator fun div(other: Complex): Complex = this * other.rev
    operator fun div(other: Double): Complex = this * (1 / other)

    override fun toString(): String {
        return real.toPlainString()
    }
}

