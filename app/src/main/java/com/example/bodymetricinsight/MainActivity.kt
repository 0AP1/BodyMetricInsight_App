package com.example.bodymetricinsight

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bodymetricinsight.ui.theme.BodyMetricInsightTheme

private const val SCREEN_WELCOME = "welcome"
private const val SCREEN_CALCULATOR = "calculator"
private const val SCREEN_INFO = "info"

private val AppBackground = Color(0xFFF7F4EF)
private val PrimaryGreen = Color(0xFF2F5D50)
private val TextDark = Color(0xFF1F2933)
private val SoftGreen = Color(0xFFEAF3EF)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            BodyMetricInsightTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    BodyMetricInsightApp(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun BodyMetricInsightApp(modifier: Modifier = Modifier) {
    var currentScreen by rememberSaveable { mutableStateOf(SCREEN_WELCOME) }

    when (currentScreen) {
        SCREEN_WELCOME -> WelcomeScreen(
            modifier = modifier,
            onStartClick = {
                currentScreen = SCREEN_CALCULATOR
            },
            onInfoClick = {
                currentScreen = SCREEN_INFO
            }
        )

        SCREEN_CALCULATOR -> CalculatorScreen(
            modifier = modifier,
            onBackClick = {
                currentScreen = SCREEN_WELCOME
            },
            onInfoClick = {
                currentScreen = SCREEN_INFO
            }
        )

        SCREEN_INFO -> InfoScreen(
            modifier = modifier,
            onBackClick = {
                currentScreen = SCREEN_CALCULATOR
            }
        )
    }
}

@Composable
fun WelcomeScreen(
    modifier: Modifier = Modifier,
    onStartClick: () -> Unit,
    onInfoClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AppBackground)
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                Text(
                    text = "BodyMetric Insight",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryGreen,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "A simple BMI calculator to help you understand your body weight range based on your height and weight.",
                    fontSize = 17.sp,
                    color = TextDark,
                    textAlign = TextAlign.Center,
                    lineHeight = 25.sp
                )

                Text(
                    text = "BMI = weight(kg) / height(m)²",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = PrimaryGreen,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Enter your height in centimetres and weight in kilograms to calculate your BMI.",
                    fontSize = 15.sp,
                    color = Color.DarkGray,
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp
                )

                Button(
                    onClick = onStartClick,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryGreen
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "Start Calculator",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(6.dp)
                    )
                }

                OutlinedButton(
                    onClick = onInfoClick,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "View BMI Categories",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(6.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CalculatorScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onInfoClick: () -> Unit
) {
    var heightInput by rememberSaveable { mutableStateOf("") }
    var weightInput by rememberSaveable { mutableStateOf("") }
    var bmiResult by rememberSaveable { mutableStateOf("") }
    var bmiCategory by rememberSaveable { mutableStateOf("") }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AppBackground)
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                Text(
                    text = "BMI Calculator",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryGreen,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Enter your height and weight below.",
                    fontSize = 16.sp,
                    color = TextDark,
                    textAlign = TextAlign.Center
                )

                OutlinedTextField(
                    value = heightInput,
                    onValueChange = { heightInput = it },
                    label = {
                        Text("Height (cm)")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
                )

                OutlinedTextField(
                    value = weightInput,
                    onValueChange = { weightInput = it },
                    label = {
                        Text("Weight (kg)")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
                )

                Button(
                    onClick = {
                        val heightCm = heightInput.toDoubleOrNull()
                        val weightKg = weightInput.toDoubleOrNull()

                        if (heightCm != null && weightKg != null && heightCm > 0 && weightKg > 0) {
                            val heightM = heightCm / 100
                            val bmi = weightKg / (heightM * heightM)

                            bmiCategory = when {
                                bmi < 18.5 -> "Underweight"
                                bmi < 25.0 -> "Normal"
                                bmi < 30.0 -> "Overweight"
                                else -> "Obese"
                            }

                            bmiResult = "Your BMI is %.2f".format(bmi)
                        } else {
                            bmiResult = "Please enter valid height and weight."
                            bmiCategory = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryGreen
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "Calculate BMI",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(6.dp)
                    )
                }

                if (bmiResult.isNotEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = SoftGreen
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = bmiResult,
                                fontSize = 21.sp,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryGreen,
                                textAlign = TextAlign.Center
                            )

                            if (bmiCategory.isNotEmpty()) {
                                Text(
                                    text = "Category: $bmiCategory",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextDark,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }

                OutlinedButton(
                    onClick = onInfoClick,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "View BMI Categories",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(6.dp)
                    )
                }

                OutlinedButton(
                    onClick = onBackClick,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "Back to Welcome",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(6.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun InfoScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AppBackground)
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "BMI Categories",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryGreen,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "BMI is a general guide used to understand weight range based on height.",
                    fontSize = 16.sp,
                    color = TextDark,
                    textAlign = TextAlign.Center,
                    lineHeight = 23.sp
                )

                Spacer(modifier = Modifier.height(2.dp))

                CategoryRow(range = "Below 18.5", category = "Underweight")
                CategoryRow(range = "18.5 - 24.9", category = "Normal")
                CategoryRow(range = "25.0 - 29.9", category = "Overweight")
                CategoryRow(range = "30.0 and above", category = "Obese")

                Text(
                    text = "Note: BMI may not be perfect for everyone, such as athletes, children, elderly people, or pregnant women.",
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    textAlign = TextAlign.Center,
                    lineHeight = 21.sp
                )

                OutlinedButton(
                    onClick = onBackClick,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "Back to Calculator",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(6.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryRow(
    range: String,
    category: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = AppBackground,
                shape = RoundedCornerShape(14.dp)
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = range,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextDark
        )

        Text(
            text = category,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryGreen,
            textAlign = TextAlign.End
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    BodyMetricInsightTheme {
        WelcomeScreen(
            onStartClick = {},
            onInfoClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CalculatorScreenPreview() {
    BodyMetricInsightTheme {
        CalculatorScreen(
            onBackClick = {},
            onInfoClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun InfoScreenPreview() {
    BodyMetricInsightTheme {
        InfoScreen(
            onBackClick = {}
        )
    }
}