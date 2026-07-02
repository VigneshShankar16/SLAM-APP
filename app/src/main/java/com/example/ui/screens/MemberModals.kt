package com.example.ui.screens

import androidx.compose.animation.*
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.ExerciseItem
import com.example.data.MemberProfile
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberProfileDrawerModal(
    profile: MemberProfile?,
    onDismiss: () -> Unit,
    onSaveDetails: (String, String, Float, Float, String, String, String) -> Unit,
    onUpdatePhoto: (String) -> Unit,
    onShowQr: () -> Unit,
    onLogout: () -> Unit
) {
    var nameInput by remember { mutableStateOf(profile?.name ?: "Alex Vance") }
    var mobileInput by remember { mutableStateOf(profile?.mobileNumber ?: "+91 98402 11988") }
    var weightInput by remember { mutableStateOf(profile?.weightKg?.toString() ?: "78.5") }
    var heightInput by remember { mutableStateOf(profile?.heightCm?.toString() ?: "178.0") }
    var genderInput by remember { mutableStateOf(profile?.gender ?: "Male") }
    var goalInput by remember { mutableStateOf(profile?.fitnessGoalText ?: "Muscle Hypertrophy & Shred") }
    var healthInput by remember { mutableStateOf(profile?.healthIssues ?: "None (Cleared for intense lifting)") }

    var showPhotoSelector by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = SlamObsidian,
        dragHandle = { BottomSheetDefaults.DragHandle(color = SlamCrimson) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "MEMBER PROFILE & BIOMETRICS",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black, color = Color.White)
                    )
                    Text(
                        "SLAM Lifestyle & Fitness Studio • Villivakkam",
                        style = MaterialTheme.typography.labelSmall.copy(color = ElectricLime)
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                }
            }

            // Photo Avatar Box
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SlamSurface),
                shape = RoundedCornerShape(18.dp),
                border = BorderStroke(1.dp, SlamBorder)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(contentAlignment = Alignment.BottomEnd) {
                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .clip(CircleShape)
                                .background(SlamSurfaceElevated)
                                .border(2.5.dp, SlamCrimson, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            if (profile?.profilePhotoUri == "AVATAR_PRO") {
                                Image(
                                    painter = painterResource(id = R.drawable.img_chest_workout),
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Icon(Icons.Default.Person, contentDescription = null, tint = SlamCrimson, modifier = Modifier.size(54.dp))
                            }
                        }

                        Surface(
                            onClick = { showPhotoSelector = !showPhotoSelector },
                            shape = CircleShape,
                            color = ElectricLime,
                            shadowElevation = 4.dp
                        ) {
                            Icon(Icons.Default.CameraAlt, contentDescription = "Upload Photo", tint = SlamObsidian, modifier = Modifier.padding(6.dp).size(18.dp))
                        }
                    }

                    if (showPhotoSelector) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = {
                                    onUpdatePhoto("AVATAR_PRO")
                                    showPhotoSelector = false
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = SlamCrimson),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Set Athletic Avatar", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                            OutlinedButton(
                                onClick = {
                                    onUpdatePhoto("")
                                    showPhotoSelector = false
                                },
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondary)
                            ) {
                                Text("Reset Default", fontSize = 11.sp)
                            }
                        }
                    }

                    Text("Membership ID: ${profile?.memberIdCode ?: "SLAM-8849"}", style = MaterialTheme.typography.titleSmall.copy(color = ElectricLime, fontWeight = FontWeight.Black))
                    Text("Verified SLAM Villivakkam Cloud Member", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary))
                }
            }

            // QR Pass Trigger Button
            Button(
                onClick = {
                    onDismiss()
                    onShowQr()
                },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SlamCrimson),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.QrCodeScanner, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("SHOW QR VERIFICATION PASS", fontWeight = FontWeight.Black)
            }

            HorizontalDivider(color = SlamBorder)

            Text("BIOMETRICS & HEALTH DATA", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary, fontWeight = FontWeight.ExtraBold))

            OutlinedTextField(
                value = nameInput,
                onValueChange = { nameInput = it },
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = mobileInput,
                onValueChange = { mobileInput = it },
                label = { Text("Contact Number") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = weightInput,
                    onValueChange = { weightInput = it },
                    label = { Text("Weight (kg)") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                OutlinedTextField(
                    value = heightInput,
                    onValueChange = { heightInput = it },
                    label = { Text("Height (cm)") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }

            OutlinedTextField(
                value = genderInput,
                onValueChange = { genderInput = it },
                label = { Text("Gender") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = goalInput,
                onValueChange = { goalInput = it },
                label = { Text("Fitness Goal") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = healthInput,
                onValueChange = { healthInput = it },
                label = { Text("Health Issues / Medical Cleared Status") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = {
                        val w = weightInput.toFloatOrNull() ?: 78.5f
                        val h = heightInput.toFloatOrNull() ?: 178.0f
                        onSaveDetails(nameInput, mobileInput, w, h, genderInput, goalInput, healthInput)
                        onDismiss()
                    },
                    modifier = Modifier.weight(1.5f).height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricLime, contentColor = SlamObsidian),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("SAVE CHANGES", fontWeight = FontWeight.Black)
                }

                OutlinedButton(
                    onClick = {
                        onDismiss()
                        onLogout()
                    },
                    modifier = Modifier.weight(1f).height(50.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = SlamRedBright),
                    border = BorderStroke(1.dp, SlamCrimson),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("LOGOUT", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(Modifier.height(30.dp))
        }
    }
}

@Composable
fun QrVerificationModal(
    profile: MemberProfile?,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SlamSurface,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text("VERIFIED SLAM QR PASS", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black, color = ElectricLime))
                Badge(containerColor = SlamCrimson) {
                    Text("LIVE SCAN", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // User Photo & Status
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SlamSurfaceElevated, RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(SlamCrimson),
                        contentAlignment = Alignment.Center
                    ) {
                        if (profile?.profilePhotoUri == "AVATAR_PRO") {
                            Image(
                                painter = painterResource(id = R.drawable.img_chest_workout),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Icon(Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
                        }
                    }
                    Column {
                        Text(profile?.name ?: "Alex Vance", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Black, color = Color.White))
                        Text("ID: ${profile?.memberIdCode ?: "SLAM-8849"}", style = MaterialTheme.typography.labelMedium.copy(color = ElectricLime, fontWeight = FontWeight.Bold))
                        Text("Valid Thru: ${profile?.dateOfValidity ?: "31 Dec 2027"}", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary))
                    }
                }

                // QR Display
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                        .border(3.dp, SlamCrimson, RoundedCornerShape(16.dp))
                        .padding(14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val step = size.width / 9f
                        for (row in 0..8) {
                            for (col in 0..8) {
                                val isCorner = (row < 3 && col < 3) || (row < 3 && col > 5) || (row > 5 && col < 3)
                                val isData = ((row * 5 + col * 11) % 2 == 0) && !isCorner
                                if (isCorner || isData) {
                                    drawRect(
                                        color = if (isCorner) Color(0xFF090A0E) else Color(0xFF13151D),
                                        topLeft = Offset(col * step, row * step),
                                        size = androidx.compose.ui.geometry.Size(step * 0.86f, step * 0.86f)
                                    )
                                }
                            }
                        }
                    }
                    Icon(Icons.Default.Verified, contentDescription = null, tint = SlamCrimson, modifier = Modifier.size(36.dp))
                }

                Text(
                    "Show this QR at SLAM Villivakkam reception or scan at automated gate turnstile.",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, textAlign = TextAlign.Center)
                )
            }
        },
        confirmButton = {
            Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(containerColor = SlamCrimson)) {
                Text("CLOSE QR", fontWeight = FontWeight.Black)
            }
        }
    )
}

@Composable
fun TrainerVideoUploadModal(
    exercise: ExerciseItem,
    userRole: String = "MEMBER",
    onDismiss: () -> Unit,
    onSaveVideo: (String, String) -> Unit
) {
    var videoUrlInput by remember { mutableStateOf(exercise.trainerVideoUrl) }
    var trainerTipInput by remember { mutableStateOf(exercise.trainerTip) }
    var isSimulatingUpload by remember { mutableStateOf(false) }
    var uploadStatus by remember { mutableStateOf<String?>(null) }
    val canUpload = userRole == "TRAINER" || userRole == "ADMIN"

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SlamSurface,
        title = {
            Text(if (canUpload) "TRAINER VIDEO & FORM UPLOAD" else "TRAINER VIDEO & FORM GUIDE", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black, color = Color.White))
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Text(exercise.name, style = MaterialTheme.typography.titleSmall.copy(color = SlamCrimson, fontWeight = FontWeight.Bold))

                // Video Player Box Simulation
                Card(
                    modifier = Modifier.fillMaxWidth().height(160.dp),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.5.dp, ElectricLime)
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Image(
                            painter = painterResource(id = R.drawable.img_chest_workout),
                            contentDescription = "Trainer Video",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.4f)))
                        Column(
                            modifier = Modifier.align(Alignment.Center).padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Default.PlayCircle, contentDescription = "Play Trainer Video", tint = ElectricLime, modifier = Modifier.size(48.dp))
                            Spacer(Modifier.height(6.dp))
                            Text("STREAMING TRAINER EXECUTION", style = MaterialTheme.typography.labelSmall.copy(color = Color.White, fontWeight = FontWeight.Bold))
                        }
                    }
                }

                Text("Current Tip: \"${trainerTipInput}\"", style = MaterialTheme.typography.bodySmall.copy(color = TextPrimary, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic))

                HorizontalDivider(color = SlamBorder)

                if (canUpload) {
                    Text("UPLOAD / REPLACE TRAINER VIDEO", style = MaterialTheme.typography.labelSmall.copy(color = ElectricLime, fontWeight = FontWeight.ExtraBold))

                    OutlinedTextField(
                        value = videoUrlInput,
                        onValueChange = { videoUrlInput = it },
                        label = { Text("Video Stream URL / File URI") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = trainerTipInput,
                        onValueChange = { trainerTipInput = it },
                        label = { Text("Trainer Coaching Cue") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2
                    )

                    Button(
                        onClick = {
                            isSimulatingUpload = true
                            uploadStatus = "Uploading video clip to SLAM Cloud Server..."
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = SlamSurfaceElevated, contentColor = ElectricLime),
                        border = BorderStroke(1.dp, ElectricLime)
                    ) {
                        Icon(Icons.Default.UploadFile, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("SELECT FROM DEVICE GALLERY", fontWeight = FontWeight.Bold)
                    }

                    if (uploadStatus != null) {
                        Text(uploadStatus!!, style = MaterialTheme.typography.labelSmall.copy(color = ElectricLime))
                    }
                } else {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = SlamSurfaceElevated),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp).fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = SlamCrimson, modifier = Modifier.size(18.dp))
                            Text(
                                "Workout video upload & editing permissions are restricted to Trainers and Admins.",
                                style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            if (canUpload) {
                Button(
                    onClick = {
                        onSaveVideo(videoUrlInput, trainerTipInput)
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SlamCrimson)
                ) {
                    Text("SAVE VIDEO", fontWeight = FontWeight.Black)
                }
            } else {
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = SlamCrimson)
                ) {
                    Text("CLOSE", fontWeight = FontWeight.Black)
                }
            }
        },
        dismissButton = {
            if (canUpload) {
                TextButton(onClick = onDismiss) {
                    Text("CANCEL", color = TextSecondary)
                }
            }
        }
    )
}
