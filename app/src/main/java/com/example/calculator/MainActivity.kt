package com.example.calculator

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.calculator.ui.theme.CalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalculatorTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CalculatorScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

// --- MODELE DANYCH ---

enum class ButtonType {
    DIGIT, OPERATOR, EQUALS, FUNCTION
}

data class CalcButton(
    val label: String,
    val textToAppend: String = label,
    val type: ButtonType = ButtonType.DIGIT,
    val isClear: Boolean = false,
    val isDelete: Boolean = false
)

// --- LISTY PRZYCISKÓW ---

val basicButtons = listOf(
    CalcButton("C", type = ButtonType.FUNCTION, isClear = true),
    CalcButton("⌫", type = ButtonType.FUNCTION, isDelete = true),
    CalcButton("(", type = ButtonType.OPERATOR),
    CalcButton(")", type = ButtonType.OPERATOR),

    CalcButton("7"), CalcButton("8"), CalcButton("9"), CalcButton("/", type = ButtonType.OPERATOR),
    CalcButton("4"), CalcButton("5"), CalcButton("6"), CalcButton("*", type = ButtonType.OPERATOR),
    CalcButton("1"), CalcButton("2"), CalcButton("3"), CalcButton("-", type = ButtonType.OPERATOR),
    CalcButton("0"), CalcButton("."), CalcButton("+", type = ButtonType.OPERATOR), CalcButton("=", type = ButtonType.EQUALS)
)

val scientificButtons = listOf(
    CalcButton("sin", "sin(" , ButtonType.FUNCTION),
    CalcButton("cos", "cos(" , ButtonType.FUNCTION),
    CalcButton("tan", "tan(" , ButtonType.FUNCTION),
    CalcButton("π", "pi", ButtonType.FUNCTION),
    CalcButton("√", "sqrt(", ButtonType.FUNCTION),
    CalcButton("x^y", "^", ButtonType.FUNCTION),
    CalcButton("ln", "ln(" , ButtonType.FUNCTION),
    CalcButton("e", "e", ButtonType.FUNCTION)
)

// --- KOMPONENTY UI ---

@Composable
fun CalculatorScreen(modifier: Modifier = Modifier) {
    var expression by remember { mutableStateOf("") }
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val onButtonClick: (CalcButton) -> Unit = { button ->
        when {
            button.isClear -> expression = ""
            button.isDelete -> if (expression.isNotEmpty()) expression = expression.dropLast(1)
            else -> expression += button.textToAppend
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        DisplaySection(
            expression = expression,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (isLandscape) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(3f),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CalculatorGrid(
                    buttons = scientificButtons,
                    columns = 2,
                    onButtonClick = onButtonClick,
                    modifier = Modifier.weight(1f)
                )
                CalculatorGrid(
                    buttons = basicButtons,
                    columns = 4,
                    onButtonClick = onButtonClick,
                    modifier = Modifier.weight(2f)
                )
            }
        } else {
            CalculatorGrid(
                buttons = basicButtons,
                columns = 4,
                onButtonClick = onButtonClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(3f)
            )
        }
    }
}

@Composable
fun DisplaySection(expression: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHighest)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = if (expression.isEmpty()) "0" else expression,
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.End,
                maxLines = 3,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun CalculatorGrid(
    buttons: List<CalcButton>,
    columns: Int,
    onButtonClick: (CalcButton) -> Unit,
    modifier: Modifier = Modifier
) {
    val rows = buttons.chunked(columns)

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        for (row in rows) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (button in row) {
                    CalcButtonView(
                        button = button,
                        onClick = { onButtonClick(button) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun CalcButtonView(
    button: CalcButton,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val containerColor = when (button.type) {
        ButtonType.DIGIT -> MaterialTheme.colorScheme.surfaceVariant
        ButtonType.OPERATOR -> MaterialTheme.colorScheme.secondaryContainer
        ButtonType.EQUALS -> MaterialTheme.colorScheme.primary
        ButtonType.FUNCTION -> MaterialTheme.colorScheme.tertiaryContainer
    }

    val contentColor = when (button.type) {
        ButtonType.DIGIT -> MaterialTheme.colorScheme.onSurfaceVariant
        ButtonType.OPERATOR -> MaterialTheme.colorScheme.onSecondaryContainer
        ButtonType.EQUALS -> MaterialTheme.colorScheme.onPrimary
        ButtonType.FUNCTION -> MaterialTheme.colorScheme.onTertiaryContainer
    }

    Box(
        modifier = modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(16.dp))
            .background(containerColor)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = button.label,
            style = MaterialTheme.typography.titleMedium,
            color = contentColor
        )
    }
}