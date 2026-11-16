package com.example.calculator

import org.junit.Assert.assertEquals
import org.junit.Test

class CalculatorLogicTest {

    // --- PRZYPADKI Z TABELI 3.1 ---

    @Test
    fun orderOfOperations_returnsSix() {
        assertEquals("6", evaluateExpression("2+2*2"))
    }

    @Test
    fun parentheses_returnsEight() {
        assertEquals("8", evaluateExpression("(2+2)*2"))
    }

    @Test
    fun divisionByZero_returnsErrorMessage() {
        assertEquals("Błąd", evaluateExpression("1/0"))
    }

    @Test
    fun incompleteExpression_returnsEmptyString() {
        assertEquals("", evaluateExpression("2+"))
    }

    @Test
    fun unclosedParenthesis_returnsEmptyString() {
        assertEquals("", evaluateExpression("((2+2)"))
    }

    @Test
    fun emptyInput_returnsEmptyString() {
        assertEquals("", evaluateExpression(""))
    }

    @Test
    fun domainError_sqrtNegative_returnsErrorMessage() {
        assertEquals("Błąd", evaluateExpression("sqrt(-1)"))
    }

    @Test
    fun largeNumbers_returnsFormattedResult() {
        assertEquals("1524157875019052032", evaluateExpression("1234567890*1234567890"))
    }

    // --- TESTY SEPARATORA DZIESIĘTNEGO ---

    @Test
    fun decimalSeparator_dotAndComma_returnSameResult() {
        assertEquals("3.5", evaluateExpression("2.5+1"))
        assertEquals("3.5", evaluateExpression("2,5+1"))
    }
}