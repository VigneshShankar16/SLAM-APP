package com.example.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.DailyLog
import com.example.data.DietMealItem
import com.example.data.FitnessGoal
import com.example.ui.theme.*

@Composable
fun TrackingAndGoalsScreen(
    goal: FitnessGoal?,
    dailyLogs: List<DailyLog>,
    dietMeals: List<DietMealItem>,
    onSelectGoalType: (String) -> Unit,
    onLogToday: (String, Float, Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var showLogModal by remember { mutableStateOf(false) }
    var weightInput by remember { mutableStateOf(goal?.currentWeightKg?.toString() ?: "78.5") }
    var caloriesConsumedInput by remember { mutableStateOf("1550") }
    var caloriesBurnedInput by remember { mutableStateOf("640") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SlamObsidian)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Top Banner
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "BIOMETRIC CALORIE TRACKER",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black, color = Color.White)
                )
                Text(
                    text = "Syncs Walking Pedometer & Workout Burn Rates",
                    style = MaterialTheme.typography.bodySmall.copy(color = ElectricLime)
                )
            }
            Badge(containerColor = SlamCrimson, contentColor = Color.White) {
                Text("VILLIVAKKAM AI DIET", fontWeight = FontWeight.Bold, modifier = Modifier.padding(4.dp))
            }
        }

        // Goal Selection Tabs (Reduce vs Gain)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(SlamSurfaceElevated, RoundedCornerShape(16.dp))
                .padding(6.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            val isReduce = goal?.goalType == "REDUCE_WEIGHT"
            Button(
                onClick = { onSelectGoalType("REDUCE_WEIGHT") },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isReduce) SlamCrimson else Color.Transparent,
                    contentColor = if (isReduce) Color.White else TextSecondary
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(Icons.Default.TrendingDown, contentDescription = null, modifier = Modifier.size(16.dp))
                    Text("REDUCE FAT", fontWeight = FontWeight.Black)
                }
            }

            Button(
                onClick = { onSelectGoalType("GAIN_WEIGHT") },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (!isReduce) SlamCrimson else Color.Transparent,
                    contentColor = if (!isReduce) Color.White else TextSecondary
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(Icons.Default.TrendingUp, contentDescription = null, modifier = Modifier.size(16.dp))
                    Text("GAIN MUSCLE", fontWeight = FontWeight.Black)
                }
            }
        }

        // Hero Goal Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SlamSurface),
            shape = RoundedCornerShape(22.dp),
            border = BorderStroke(1.dp, SlamBorder)
        ) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Badge(containerColor = SlamSurfaceElevated, contentColor = ElectricLime) {
                        Text("ACTIVE TARGET", fontWeight = FontWeight.Bold, modifier = Modifier.padding(2.dp))
                    }
                    Text("DEADLINE: 12 WEEKS", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary, fontWeight = FontWeight.Bold))
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column {
                        Text("CURRENT WEIGHT", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary))
                        Text(
                            text = "${goal?.currentWeightKg ?: 78.5f} kg",
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Black, color = Color.White)
                        )
                    }
                    Icon(Icons.Default.ArrowForward, contentDescription = null, tint = SlamCrimson, modifier = Modifier.size(28.dp).align(Alignment.CenterVertically))
                    Column(horizontalAlignment = Alignment.End) {
                        Text("TARGET GOAL", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary))
                        Text(
                            text = "${goal?.targetWeightKg ?: 73.0f} kg",
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Black, color = ElectricLime)
                        )
                    }
                }

                HorizontalDivider(color = SlamBorder)

                // Calorie Target & Macro Summary
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column {
                        Text("DAILY CALORIES", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary))
                        Text("${goal?.dailyCalorieTarget ?: 1950} kcal", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black, color = ElectricOrange))
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("PROTEIN TARGET", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary))
                        Text("${goal?.proteinGrams ?: 175} g / day", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black, color = Color.White))
                    }
                    Button(
                        onClick = { showLogModal = true },
                        colors = ButtonDefaults.buttonColors(containerColor = SlamCrimson, contentColor = Color.White),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("+ LOG TODAY", fontWeight = FontWeight.Black, fontSize = 11.sp)
                    }
                }
            }
        }

        // Daily Calories Burned & Consumed Graph Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SlamSurface),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, SlamBorder)
        ) {
            Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column {
                        Text("7-DAY CALORIE & WEIGHT LOG", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Black, color = Color.White))
                        Text("Walking & Gym Burn vs Food Intake", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary))
                    }
                    Icon(Icons.Default.Insights, contentDescription = null, tint = SlamCrimson)
                }

                val recentLogs = if (dailyLogs.isNotEmpty()) dailyLogs else listOf(
                    DailyLog(1, "02 Jul", 78.5f, 1550, 640),
                    DailyLog(2, "01 Jul", 78.7f, 1800, 720),
                    DailyLog(3, "30 Jun", 79.0f, 1920, 580)
                )

                recentLogs.forEach { log ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(SlamSurfaceElevated, RoundedCornerShape(12.dp))
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(log.dateStr, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Black, color = Color.White))
                            Text("Weight: ${log.weightKg} kg", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary))
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                            Column(horizontalAlignment = Alignment.End) {
                                Text("INTAKE", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted, fontSize = 9.sp))
                                Text("${log.caloriesConsumed} kcal", style = MaterialTheme.typography.labelMedium.copy(color = ElectricOrange, fontWeight = FontWeight.Bold))
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("BURNED", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted, fontSize = 9.sp))
                                Text("${log.caloriesBurned} kcal", style = MaterialTheme.typography.labelMedium.copy(color = ElectricLime, fontWeight = FontWeight.Bold))
                            }
                        }
                    }
                }
            }
        }

        // Custom Diet Plans Section
        Text(
            text = "RECOMMENDED DIET PLAN FOR YOUR GOAL",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black, color = Color.White)
        )

        dietMeals.forEach { meal ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SlamSurface),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, SlamBorder)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Badge(containerColor = SlamCrimson) {
                                Text(meal.mealTiming, fontWeight = FontWeight.Black, fontSize = 9.sp, color = Color.White)
                            }
                            Text(meal.mealName, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = Color.White))
                        }
                        Spacer(Modifier.height(4.dp))
                        Text(meal.ingredients, style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("${meal.calories} kcal", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Black, color = ElectricLime))
                        Text("${meal.proteinG}g Protein", style = MaterialTheme.typography.labelSmall.copy(color = ElectricOrange, fontWeight = FontWeight.Bold))
                    }
                }
            }
        }

        Spacer(Modifier.height(30.dp))
    }

    if (showLogModal) {
        AlertDialog(
            onDismissRequest = { showLogModal = false },
            containerColor = SlamSurface,
            title = {
                Text("LOG TODAY'S ACTIVITY", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black, color = Color.White))
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    OutlinedTextField(
                        value = weightInput,
                        onValueChange = { weightInput = it },
                        label = { Text("Weight Today (kg)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = caloriesConsumedInput,
                        onValueChange = { caloriesConsumedInput = it },
                        label = { Text("Calories Consumed (Food)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = caloriesBurnedInput,
                        onValueChange = { caloriesBurnedInput = it },
                        label = { Text("Calories Burned (Walking/Workout)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val w = weightInput.toFloatOrNull() ?: 78.5f
                        val cCons = caloriesConsumedInput.toIntOrNull() ?: 1500
                        val cBurn = caloriesBurnedInput.toIntOrNull() ?: 600
                        onLogToday("02 Jul", w, cCons, cBurn)
                        showLogModal = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SlamCrimson)
                ) {
                    Text("SAVE LOG", fontWeight = FontWeight.Black)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogModal = false }) {
                    Text("CANCEL", color = TextSecondary)
                }
            }
        )
    }
}
