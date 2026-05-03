package com.example.bodymetricinsight

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bodymetricinsight.ui.theme.BodyMetricInsightTheme

private const val SCREEN_HOME = "home"
private const val SCREEN_CALCULATOR = "calculator"
private const val SCREEN_INFO = "info"

private const val HEIGHT_CM = "cm"
private const val HEIGHT_FEET = "feet"
private const val WEIGHT_KG = "kg"
private const val WEIGHT_LB = "lb"

private val NavyDark = Color(0xFF061A33)
private val NavyMid = Color(0xFF0B2B4C)
private val CyanBlue = Color(0xFF12B8F2)
private val Aqua = Color(0xFF18D6C2)
private val LimeGreen = Color(0xFF8CE63F)
private val SoftBlue = Color(0xFFEAF8FF)
private val SoftGreen = Color(0xFFEAFBEF)
private val CardWhite = Color(0xFFFFFFFF)
private val TextDark = Color(0xFF102033)
private val TextMuted = Color(0xFF5D6B78)
private val WarningRed = Color(0xFFB3261E)
private val WarningBackground = Color(0xFFFFECEA)
private val PrimaryGreen = Color(0xFF2E6B5A)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            BodyMetricInsightTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    BodyMetricInsightsApp(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun BodyMetricInsightsApp(modifier: Modifier = Modifier) {
    var currentScreen by rememberSaveable { mutableStateOf(SCREEN_HOME) }

    when (currentScreen) {
        SCREEN_HOME -> HomeScreen(
            modifier = modifier,
            onStartClick = { currentScreen = SCREEN_CALCULATOR },
            onInfoClick = { currentScreen = SCREEN_INFO }
        )

        SCREEN_CALCULATOR -> CalculatorScreen(
            modifier = modifier,
            onHomeClick = { currentScreen = SCREEN_HOME },
            onInfoClick = { currentScreen = SCREEN_INFO }
        )

        SCREEN_INFO -> InfoScreen(
            modifier = modifier,
            onCalculatorClick = { currentScreen = SCREEN_CALCULATOR },
            onHomeClick = { currentScreen = SCREEN_HOME }
        )
    }
}

@Composable
fun AppPage(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(NavyDark, NavyMid)
                )
            )
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            content()
        }
    }
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onStartClick: () -> Unit,
    onInfoClick: () -> Unit
) {
    AppPage(modifier = modifier) {
        LogoHeader()

        MainCard {
            Text(
                text = "Know Your BMI Clearly",
                fontSize = 27.sp,
                fontWeight = FontWeight.ExtraBold,
                color = NavyDark,
                textAlign = TextAlign.Center
            )

            Text(
                text = "BodyMetric Insights helps you quickly calculate your Body Mass Index using your preferred height and weight units.",
                fontSize = 16.sp,
                color = TextDark,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )

            FormulaCard()

            Text(
                text = "You can use centimetres or feet/inches for height, and kilograms or pounds for weight.",
                fontSize = 15.sp,
                color = TextMuted,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )

            PrimaryButton(
                text = "Start BMI Calculator",
                onClick = onStartClick
            )

            SecondaryButton(
                text = "View BMI Categories",
                onClick = onInfoClick
            )
        }

        WarningCard()
        FooterText()
    }
}

@Composable
fun CalculatorScreen(
    modifier: Modifier = Modifier,
    onHomeClick: () -> Unit,
    onInfoClick: () -> Unit
) {
    var heightUnit by rememberSaveable { mutableStateOf(HEIGHT_CM) }
    var weightUnit by rememberSaveable { mutableStateOf(WEIGHT_KG) }

    var heightCmInput by rememberSaveable { mutableStateOf("") }
    var feetInput by rememberSaveable { mutableStateOf("") }
    var inchesInput by rememberSaveable { mutableStateOf("") }

    var weightKgInput by rememberSaveable { mutableStateOf("") }
    var weightLbInput by rememberSaveable { mutableStateOf("") }

    var bmiResult by rememberSaveable { mutableStateOf("") }
    var bmiCategory by rememberSaveable { mutableStateOf("") }
    var bmiMessage by rememberSaveable { mutableStateOf("") }

    AppPage(modifier = modifier) {
        PageTitle(
            title = "BMI Calculator",
            subtitle = "Choose your units and calculate your BMI."
        )

        MainCard {
            SectionTitle("Height Unit")

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                UnitButton(
                    text = "cm",
                    selected = heightUnit == HEIGHT_CM,
                    onClick = { heightUnit = HEIGHT_CM },
                    modifier = Modifier.weight(1f)
                )

                UnitButton(
                    text = "feet/inches",
                    selected = heightUnit == HEIGHT_FEET,
                    onClick = { heightUnit = HEIGHT_FEET },
                    modifier = Modifier.weight(1f)
                )
            }

            if (heightUnit == HEIGHT_CM) {
                NumberInputField(
                    value = heightCmInput,
                    onValueChange = { heightCmInput = it },
                    label = "Height (cm)"
                )
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    NumberInputField(
                        value = feetInput,
                        onValueChange = { feetInput = it },
                        label = "Feet",
                        modifier = Modifier.weight(1f)
                    )

                    NumberInputField(
                        value = inchesInput,
                        onValueChange = { inchesInput = it },
                        label = "Inches",
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            SectionTitle("Weight Unit")

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                UnitButton(
                    text = "kg",
                    selected = weightUnit == WEIGHT_KG,
                    onClick = { weightUnit = WEIGHT_KG },
                    modifier = Modifier.weight(1f)
                )

                UnitButton(
                    text = "pounds",
                    selected = weightUnit == WEIGHT_LB,
                    onClick = { weightUnit = WEIGHT_LB },
                    modifier = Modifier.weight(1f)
                )
            }

            if (weightUnit == WEIGHT_KG) {
                NumberInputField(
                    value = weightKgInput,
                    onValueChange = { weightKgInput = it },
                    label = "Weight (kg)"
                )
            } else {
                NumberInputField(
                    value = weightLbInput,
                    onValueChange = { weightLbInput = it },
                    label = "Weight (lb)"
                )
            }

            PrimaryButton(
                text = "Calculate BMI",
                onClick = {
                    val heightMeters = calculateHeightMeters(
                        heightUnit = heightUnit,
                        heightCmInput = heightCmInput,
                        feetInput = feetInput,
                        inchesInput = inchesInput
                    )

                    val weightKg = calculateWeightKg(
                        weightUnit = weightUnit,
                        weightKgInput = weightKgInput,
                        weightLbInput = weightLbInput
                    )

                    if (heightMeters != null && weightKg != null && heightMeters > 0 && weightKg > 0) {
                        val bmi = weightKg / (heightMeters * heightMeters)
                        val category = calculateBmiCategory(bmi)

                        bmiResult = "Your BMI is %.2f".format(bmi)
                        bmiCategory = category
                        bmiMessage = getAdviceMessage(category)
                    } else {
                        bmiResult = "Please enter valid height and weight."
                        bmiCategory = ""
                        bmiMessage = ""
                    }
                }
            )

            if (bmiResult.isNotEmpty()) {
                ResultCard(
                    bmiResult = bmiResult,
                    bmiCategory = bmiCategory,
                    bmiMessage = bmiMessage
                )
            }

            SecondaryButton(
                text = "View BMI Categories",
                onClick = onInfoClick
            )

            OutlinedButton(
                onClick = onHomeClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                border = BorderStroke(1.dp, NavyDark)
            ) {
                Text(
                    text = "Back to Home",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = NavyDark,
                    modifier = Modifier.padding(7.dp)
                )
            }
        }

        WarningCard()
        FooterText()
    }
}

@Composable
fun InfoScreen(
    modifier: Modifier = Modifier,
    onCalculatorClick: () -> Unit,
    onHomeClick: () -> Unit
) {
    AppPage(modifier = modifier) {
        PageTitle(
            title = "BMI Categories",
            subtitle = "Understand what your BMI result means."
        )

        MainCard {
            CategoryRow(range = "Below 18.5", category = "Underweight")
            CategoryRow(range = "18.5 - 24.9", category = "Normal")
            CategoryRow(range = "25.0 - 29.9", category = "Overweight")
            CategoryRow(range = "30.0 and above", category = "Obese")

            InfoNoteCard(
                title = "Important Note",
                body = "BMI is only a general guide. It does not directly measure body fat, muscle mass, fitness level, or medical condition."
            )

            PrimaryButton(
                text = "Back to Calculator",
                onClick = onCalculatorClick
            )

            SecondaryButton(
                text = "Back to Home",
                onClick = onHomeClick
            )
        }

        WarningCard()
        FooterText()
    }
}

@Composable
fun LogoHeader() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 22.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.bodymetric_logo),
                contentDescription = "BodyMetric Insights Logo",
                modifier = Modifier.size(150.dp)
            )

            Text(
                text = "BodyMetric Insights",
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                color = NavyDark,
                textAlign = TextAlign.Center
            )

            Text(
                text = "BMI calculator with clear body metric guidance",
                fontSize = 15.sp,
                color = TextMuted,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun PageTitle(
    title: String,
    subtitle: String
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.bodymetric_logo),
            contentDescription = "BodyMetric Insights Logo",
            modifier = Modifier.size(96.dp)
        )

        Text(
            text = title,
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White,
            textAlign = TextAlign.Center
        )

        Text(
            text = subtitle,
            fontSize = 15.sp,
            color = Color(0xFFD7F7FF),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun MainCard(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            content()
        }
    }
}

@Composable
fun FormulaCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SoftBlue),
        border = BorderStroke(1.dp, CyanBlue)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            Text(
                text = "BMI Formula",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = NavyDark,
                textAlign = TextAlign.Center
            )

            Text(
                text = "BMI = weight(kg) / height(m)²",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = PrimaryGreen,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun UnitButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (selected) {
        Button(
            onClick = onClick,
            modifier = modifier,
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text(
                text = text,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 5.dp)
            )
        }
    } else {
        OutlinedButton(
            onClick = onClick,
            modifier = modifier,
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(1.dp, PrimaryGreen)
        ) {
            Text(
                text = text,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryGreen,
                modifier = Modifier.padding(vertical = 5.dp)
            )
        }
    }
}

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
        shape = RoundedCornerShape(18.dp)
    ) {
        Text(
            text = text,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, CyanBlue)
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = NavyDark,
            modifier = Modifier.padding(7.dp)
        )
    }
}

@Composable
fun NumberInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier.fillMaxWidth()
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
    )
}

@Composable
fun SectionTitle(text: String) {
    Text(
        text = text,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = NavyDark,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Start
    )
}

@Composable
fun ResultCard(
    bmiResult: String,
    bmiCategory: String,
    bmiMessage: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = SoftGreen),
        border = BorderStroke(1.dp, LimeGreen)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = bmiResult,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = PrimaryGreen,
                textAlign = TextAlign.Center
            )

            if (bmiCategory.isNotEmpty()) {
                Text(
                    text = "Category: $bmiCategory",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = NavyDark,
                    textAlign = TextAlign.Center
                )
            }

            if (bmiMessage.isNotEmpty()) {
                Text(
                    text = bmiMessage,
                    fontSize = 15.sp,
                    color = TextDark,
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp
                )
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
                color = SoftBlue,
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 1.dp,
                color = CyanBlue,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 16.dp, vertical = 14.dp),
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

@Composable
fun InfoNoteCard(
    title: String,
    body: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = SoftGreen),
        border = BorderStroke(1.dp, LimeGreen)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = NavyDark,
                textAlign = TextAlign.Center
            )

            Text(
                text = body,
                fontSize = 15.sp,
                color = TextDark,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )
        }
    }
}

@Composable
fun WarningCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = WarningBackground),
        border = BorderStroke(1.dp, WarningRed)
    ) {
        Text(
            text = "Caution: This app is not medical advice. It should not replace professional medical reports or doctor guidance. It only helps calculate BMI for personal awareness.",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = WarningRed,
            textAlign = TextAlign.Center,
            lineHeight = 21.sp,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun FooterText() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.padding(top = 6.dp, bottom = 12.dp)
    ) {
        Text(
            text = "Designed and Developed by",
            fontSize = 13.sp,
            color = Color(0xFFD7F7FF),
            textAlign = TextAlign.Center
        )

        Text(
            text = "Aanchal Poudel",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic,
            color = LimeGreen,
            textAlign = TextAlign.Center
        )
    }
}

fun calculateHeightMeters(
    heightUnit: String,
    heightCmInput: String,
    feetInput: String,
    inchesInput: String
): Double? {
    return if (heightUnit == HEIGHT_CM) {
        val heightCm = heightCmInput.toDoubleOrNull()

        if (heightCm != null && heightCm > 0) {
            heightCm / 100
        } else {
            null
        }
    } else {
        val feet = feetInput.toDoubleOrNull() ?: 0.0
        val inches = inchesInput.toDoubleOrNull() ?: 0.0
        val totalInches = (feet * 12) + inches

        if (totalInches > 0) {
            totalInches * 0.0254
        } else {
            null
        }
    }
}

fun calculateWeightKg(
    weightUnit: String,
    weightKgInput: String,
    weightLbInput: String
): Double? {
    return if (weightUnit == WEIGHT_KG) {
        val weightKg = weightKgInput.toDoubleOrNull()

        if (weightKg != null && weightKg > 0) {
            weightKg
        } else {
            null
        }
    } else {
        val weightLb = weightLbInput.toDoubleOrNull()

        if (weightLb != null && weightLb > 0) {
            weightLb * 0.45359237
        } else {
            null
        }
    }
}

fun calculateBmiCategory(bmi: Double): String {
    return when {
        bmi < 18.5 -> "Underweight"
        bmi < 25.0 -> "Normal"
        bmi < 30.0 -> "Overweight"
        else -> "Obese"
    }
}

fun getAdviceMessage(category: String): String {
    return when (category) {
        "Underweight" -> "Your BMI is below the normal range. Focus on balanced meals, enough calories, strength-building habits, and regular checkups if needed."
        "Normal" -> "Your BMI is within the normal range. Keep maintaining balanced meals, regular movement, hydration, and healthy sleep habits."
        "Overweight" -> "Your BMI is above the normal range. Small steps like walking, balanced meals, portion control, and reducing sugary or highly processed foods may help."
        "Obese" -> "Your BMI is in the obese range. Consider guidance from a qualified health professional and focus on safe, sustainable lifestyle changes."
        else -> ""
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    BodyMetricInsightTheme {
        HomeScreen(
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
            onHomeClick = {},
            onInfoClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun InfoScreenPreview() {
    BodyMetricInsightTheme {
        InfoScreen(
            onCalculatorClick = {},
            onHomeClick = {}
        )
    }
}