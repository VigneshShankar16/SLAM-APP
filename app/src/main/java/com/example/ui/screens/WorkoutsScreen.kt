package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ExerciseItem
import com.example.ui.theme.*

@Composable
fun WorkoutsScreen(
    categories: List<String>,
    selectedCategory: String,
    onSelectCategory: (String) -> Unit,
    exercises: List<ExerciseItem>,
    onOpenGuide: (ExerciseItem) -> Unit,
    onOpenVideoModal: (ExerciseItem) -> Unit,
    onToggleCompleted: (Int, Boolean) -> Unit,
    // Live Tracker Props
    isTrackerActive: Boolean,
    trackerSteps: Int,
    trackerDurationSec: Int,
    trackerCaloriesBurned: Float,
    workoutType: String,
    onSelectWorkoutType: (String) -> Unit,
    onToggleTracker: () -> Unit,
    onSaveTrackerSession: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showTypeDropdown by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SlamObsidian)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Screen Title Banner
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "SLAM WORKOUT STUDIO",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                )
                Text(
                    text = "Trainer Guided Form Videos & Live Burn Tracker",
                    style = MaterialTheme.typography.bodySmall.copy(color = SlamCrimson)
                )
            }
            Badge(containerColor = SlamSurfaceElevated, contentColor = ElectricLime) {
                Text("VILLIVAKKAM PRO", fontWeight = FontWeight.Bold, modifier = Modifier.padding(4.dp))
            }
        }

        // LIVE CALORIE BURNING TRACKER PANEL
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (isTrackerActive) Color(0xFF1B0C10) else SlamSurface
            ),
            shape = RoundedCornerShape(18.dp),
            border = BorderStroke(if (isTrackerActive) 2.dp else 1.dp, if (isTrackerActive) SlamCrimson else SlamBorder)
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(
                            imageVector = if (isTrackerActive) Icons.Default.DirectionsRun else Icons.Default.MonitorHeart,
                            contentDescription = null,
                            tint = if (isTrackerActive) SlamRedBright else ElectricLime,
                            modifier = Modifier.size(22.dp)
                        )
                        Column {
                            Text("LIVE CALORIE & STEP TRACKER", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Black, color = Color.White))
                            Text("Mode: $workoutType", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary, fontSize = 10.sp))
                        }
                    }

                    // Mode switch dropdown button
                    Box {
                        Surface(
                            onClick = { showTypeDropdown = true },
                            shape = RoundedCornerShape(8.dp),
                            color = SlamSurfaceElevated,
                            border = BorderStroke(1.dp, SlamBorder)
                        ) {
                            Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                                Text("CHANGE MODE", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                            }
                        }
                        DropdownMenu(
                            expanded = showTypeDropdown,
                            onDismissRequest = { showTypeDropdown = false },
                            modifier = Modifier.background(SlamSurfaceElevated)
                        ) {
                            listOf("Treadmill & Walking", "Outdoor Run", "Heavy Weightlifting", "HIIT Cardio").forEach { type ->
                                DropdownMenuItem(
                                    text = { Text(type, color = Color.White) },
                                    onClick = {
                                        onSelectWorkoutType(type)
                                        showTypeDropdown = false
                                    }
                                )
                            }
                        }
                    }
                }

                // Live Stats Metrics Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SlamObsidian, RoundedCornerShape(12.dp))
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("DURATION", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted, fontSize = 9.sp))
                        val mins = trackerDurationSec / 60
                        val secs = trackerDurationSec % 60
                        Text("%02d:%02d".format(mins, secs), style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black, color = Color.White))
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("PEDOMETER STEPS", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted, fontSize = 9.sp))
                        Text("$trackerSteps", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black, color = ElectricLime))
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("CALORIES BURNED", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted, fontSize = 9.sp))
                        Text("%.1f kcal".format(trackerCaloriesBurned), style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black, color = ElectricOrange))
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(
                        onClick = onToggleTracker,
                        modifier = Modifier.weight(1f).height(42.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isTrackerActive) SlamRedBright else SlamCrimson
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(if (isTrackerActive) Icons.Default.Pause else Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(6.dp))
                        Text(if (isTrackerActive) "PAUSE SESSION" else "START TRACKING", fontWeight = FontWeight.Black, fontSize = 12.sp)
                    }

                    if (trackerCaloriesBurned > 0f) {
                        Button(
                            onClick = onSaveTrackerSession,
                            modifier = Modifier.weight(1f).height(42.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = ElectricLime, contentColor = SlamObsidian),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("SAVE TO DAILY LOG", fontWeight = FontWeight.Black, fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        // Category Selector Tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            categories.forEach { cat ->
                val isSelected = cat == selectedCategory
                Surface(
                    onClick = { onSelectCategory(cat) },
                    shape = RoundedCornerShape(14.dp),
                    color = if (isSelected) SlamCrimson else SlamSurface,
                    border = BorderStroke(
                        width = 1.dp,
                        color = if (isSelected) SlamCrimson else SlamBorder
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = when (cat) {
                                "CHEST" -> Icons.Default.FitnessCenter
                                "BACK" -> Icons.Default.ViewAgenda
                                "LEGS" -> Icons.Default.DirectionsRun
                                "ARMS" -> Icons.Default.SportsMartialArts
                                else -> Icons.Default.Bolt
                            },
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "$cat DAY",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                        )
                    }
                }
            }
        }

        // Exercise List
        if (exercises.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxWidth().height(180.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("No exercises listed for this category yet.", color = TextMuted)
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(exercises, key = { it.id }) { exercise ->
                    ExerciseCard(
                        exercise = exercise,
                        onOpenVideoModal = { onOpenVideoModal(exercise) },
                        onOpenGuide = { onOpenGuide(exercise) },
                        onToggleCompleted = { onToggleCompleted(exercise.id, exercise.isCompletedToday) }
                    )
                }
            }
        }
    }
}

@Composable
fun ExerciseCard(
    exercise: ExerciseItem,
    onOpenVideoModal: () -> Unit,
    onOpenGuide: () -> Unit,
    onToggleCompleted: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SlamSurface),
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(
            width = if (exercise.isCompletedToday) 1.5.dp else 1.dp,
            color = if (exercise.isCompletedToday) ElectricLime else SlamBorder
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = exercise.name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    )
                    Text(
                        text = exercise.setsReps,
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = ElectricOrange,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Surface(
                    onClick = onToggleCompleted,
                    shape = CircleShape,
                    color = if (exercise.isCompletedToday) ElectricLime else SlamSurfaceElevated,
                    border = BorderStroke(1.dp, if (exercise.isCompletedToday) ElectricLime else SlamBorder)
                ) {
                    Icon(
                        imageVector = if (exercise.isCompletedToday) Icons.Default.Check else Icons.Default.RadioButtonUnchecked,
                        contentDescription = "Mark completed",
                        tint = if (exercise.isCompletedToday) SlamObsidian else TextSecondary,
                        modifier = Modifier.padding(8.dp).size(20.dp)
                    )
                }
            }

            HorizontalDivider(color = SlamBorder)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("TARGET AREA", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted, fontSize = 10.sp))
                    Text(exercise.targetMuscle, style = MaterialTheme.typography.bodySmall.copy(color = Color.White, fontWeight = FontWeight.SemiBold))
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("TRAINER", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted, fontSize = 10.sp))
                    Text("Master Vikram", style = MaterialTheme.typography.bodySmall.copy(color = ElectricLime, fontWeight = FontWeight.Bold))
                }
            }

            // Action Buttons: WATCH TRAINER VIDEO & FORM GUIDE
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = onOpenVideoModal,
                    modifier = Modifier.weight(1.2f).height(46.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SlamCrimson,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(Icons.Default.PlayCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                        Text(
                            text = "TRAINER VIDEO",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Black,
                                fontSize = 11.sp
                            )
                        )
                    }
                }

                OutlinedButton(
                    onClick = onOpenGuide,
                    modifier = Modifier.weight(1f).height(46.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                    border = BorderStroke(1.dp, SlamBorder),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(Icons.Default.Timeline, contentDescription = null, tint = ElectricLime, modifier = Modifier.size(16.dp))
                        Text("LINE GUIDE", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
