package com.example.calculator

import org.mariuszgromada.math.mxparser.Expression
import org.mariuszgromada.math.mxparser.License

fun evaluateExpression(rawExpression: String): String {
    License.iConfirmNonCommercialUse("Student")

    val normalized = rawExpression.replace(",", ".").trim()
    if (normalized.isEmpty()) return ""

    val expression = Expression(normalized)
    if (!expression.checkSyntax()) return ""

    val result = expression.calculate()
    if (result.isNaN() || result.isInfinite()) return "Błąd"

    return if (result % 1.0 == 0.0 && result in Long.MIN_VALUE.toDouble()..Long.MAX_VALUE.toDouble()) {
        result.toLong().toString()
    } else {
        result.toString()
    }
}