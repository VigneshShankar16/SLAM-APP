package com.example.ui.screens

import androidx.compose.animation.core.*
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
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.ExerciseItem
import com.example.ui.theme.*
import kotlin.math.sin

@Composable
fun FormLineGuideModal(
    exercise: ExerciseItem,
    isAnimationPlaying: Boolean,
    onTogglePlay: () -> Unit,
    onDismiss: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "formTrajectory")
    val animProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "trajectoryProgress"
    )

    val currentProgress = if (isAnimationPlaying) animProgress else 0.5f

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .fillMaxHeight(0.88f)
                .clip(RoundedCornerShape(24.dp)),
            color = SlamSurface,
            border = BorderStroke(2.dp, SlamCrimson)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(SlamObsidian)
            ) {
                // Modal Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SlamSurfaceElevated)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Badge(containerColor = SlamCrimson) {
                                Text(exercise.category, color = Color.White, fontWeight = FontWeight.Bold)
                            }
                            Text(
                                text = "BIOMECHANIC FORM GUIDE",
                                style = MaterialTheme.typography.labelSmall.copy(color = ElectricLime, fontWeight = FontWeight.ExtraBold)
                            )
                        }
                        Text(
                            text = exercise.name,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black, color = Color.White)
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Line Animation Canvas
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(SlamSurface)
                            .border(1.dp, SlamBorder, RoundedCornerShape(16.dp))
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val w = size.width
                            val h = size.height

                            val gridSpacing = 30.dp.toPx()
                            var x = 0f
                            while (x < w) {
                                drawLine(
                                    color = Color.White.copy(alpha = 0.04f),
                                    start = Offset(x, 0f),
                                    end = Offset(x, h),
                                    strokeWidth = 1f
                                )
                                x += gridSpacing
                            }
                            var y = 0f
                            while (y < h) {
                                drawLine(
                                    color = Color.White.copy(alpha = 0.04f),
                                    start = Offset(0f, y),
                                    end = Offset(w, y),
                                    strokeWidth = 1f
                                )
                                y += gridSpacing
                            }

                            when (exercise.lineAnimationTrajectory) {
                                "ARC_UP" -> {
                                    val startPt = Offset(w * 0.2f, h * 0.75f)
                                    val peakPt = Offset(w * 0.5f, h * 0.25f)
                                    val endPt = Offset(w * 0.8f, h * 0.75f)

                                    val path = androidx.compose.ui.graphics.Path().apply {
                                        moveTo(startPt.x, startPt.y)
                                        quadraticBezierTo(peakPt.x, peakPt.y - 40f, endPt.x, endPt.y)
                                    }
                                    drawPath(
                                        path = path,
                                        color = SlamCrimson.copy(alpha = 0.4f),
                                        style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round, pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 10f), 0f))
                                    )

                                    val dotX = startPt.x + (endPt.x - startPt.x) * currentProgress
                                    val parabolaOffset = sin(currentProgress * Math.PI.toFloat()) * (h * 0.48f)
                                    val dotY = startPt.y - parabolaOffset

                                    drawCircle(color = SlamCrimson.copy(alpha = 0.25f), radius = 24.dp.toPx(), center = Offset(dotX, dotY))
                                    drawCircle(color = ElectricLime, radius = 10.dp.toPx(), center = Offset(dotX, dotY))
                                    drawCircle(color = Color.White, radius = 4.dp.toPx(), center = Offset(dotX, dotY))
                                }
                                "PRESS_FORWARD" -> {
                                    val startPt = Offset(w * 0.15f, h * 0.6f)
                                    val endPt = Offset(w * 0.85f, h * 0.35f)

                                    drawLine(color = SlamCrimson.copy(alpha = 0.4f), start = startPt, end = endPt, strokeWidth = 4.dp.toPx(), cap = StrokeCap.Round)
                                    val dotX = startPt.x + (endPt.x - startPt.x) * currentProgress
                                    val dotY = startPt.y + (endPt.y - startPt.y) * currentProgress

                                    drawCircle(color = SlamCrimson.copy(alpha = 0.25f), radius = 24.dp.toPx(), center = Offset(dotX, dotY))
                                    drawCircle(color = ElectricLime, radius = 10.dp.toPx(), center = Offset(dotX, dotY))
                                }
                                else -> {
                                    val startPt = Offset(w * 0.5f, h * 0.8f)
                                    val endPt = Offset(w * 0.5f, h * 0.2f)

                                    drawLine(color = SlamCrimson.copy(alpha = 0.4f), start = startPt, end = endPt, strokeWidth = 4.dp.toPx(), cap = StrokeCap.Round)
                                    val dotY = startPt.y + (endPt.y - startPt.y) * currentProgress

                                    drawCircle(color = SlamCrimson.copy(alpha = 0.25f), radius = 24.dp.toPx(), center = Offset(w * 0.5f, dotY))
                                    drawCircle(color = ElectricLime, radius = 10.dp.toPx(), center = Offset(w * 0.5f, dotY))
                                }
                            }
                        }

                        Row(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(10.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(SlamSurfaceElevated)
                                .clickable { onTogglePlay() }
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = if (isAnimationPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = null,
                                tint = ElectricLime,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = if (isAnimationPlaying) "LINE LIVE" else "PAUSED",
                                style = MaterialTheme.typography.labelSmall.copy(color = Color.White)
                            )
                        }
                    }

                    // Target Muscle & Equipment Cards
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                        Card(
                            modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(containerColor = SlamSurface),
                            border = BorderStroke(1.dp, SlamBorder)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("TARGET MUSCLE GROUP", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary))
                                Text(exercise.targetMuscle, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = ElectricLime))
                            }
                        }
                        Card(
                            modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(containerColor = SlamSurface),
                            border = BorderStroke(1.dp, SlamBorder)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("RECOMMENDED EQUIPMENT", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary))
                                Text(exercise.equipment, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = Color.White))
                            }
                        }
                    }

                    // Step by step instructions
                    Text(
                        text = "STEP-BY-STEP FORM INSTRUCTIONS",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Black, color = ElectricLime)
                    )

                    exercise.executionSteps.split("\n").forEachIndexed { idx, step ->
                        if (step.isNotBlank()) {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = SlamSurface),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.Top,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clip(CircleShape)
                                            .background(SlamCrimson),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "${idx + 1}",
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Black, color = Color.White)
                                        )
                                    }
                                    Text(
                                        text = step.replace(Regex("^\\d+\\.\\s*"), ""),
                                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.White, lineHeight = 20.sp)
                                    )
                                }
                            }
                        }
                    }

                    // Pro Tip Coach Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF240A0D)),
                        border = BorderStroke(1.dp, SlamCrimson)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(Icons.Default.Verified, contentDescription = null, tint = SlamCrimson, modifier = Modifier.size(26.dp))
                            Column {
                                Text("SLAM STUDIO FORM TIP", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Black, color = SlamCrimson))
                                Text(
                                    text = "Never bounce the weight off your chest. Keep shoulders locked back and follow the biomechanical trajectory for 100% isolation.",
                                    style = MaterialTheme.typography.bodySmall.copy(color = Color.White)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}
