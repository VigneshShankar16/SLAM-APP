package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.*
import com.example.ui.theme.*

@Composable
fun MembershipPassScreen(
    profile: MemberProfile?,
    userRole: String = "MEMBER",
    customPlans: List<CustomWorkoutPlan> = emptyList(),
    workoutHistory: List<WorkoutHistoryItem> = emptyList(),
    gymReels: List<GymReelItem> = emptyList(),
    trackedMembers: List<TrackedMemberItem> = emptyList(),
    onOpenProfileDrawer: () -> Unit,
    onShowQrPopup: () -> Unit,
    onOpenShareModal: () -> Unit,
    onLogout: () -> Unit = {},
    onAddCustomPlan: (String, String, Int, String) -> Unit = { _, _, _, _ -> },
    onAddWorkoutHistory: (String, Int, Int, Int, String) -> Unit = { _, _, _, _, _ -> },
    onUploadReel: (String, String, String) -> Unit = { _, _, _ -> },
    onAddOrUpdateMember: (String, String, String, Float, String) -> Unit = { _, _, _, _, _ -> },
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    var showAddPlanDialog by remember { mutableStateOf(false) }
    var showAddHistoryDialog by remember { mutableStateOf(false) }
    var showUploadReelDialog by remember { mutableStateOf(false) }
    var showAddMemberDialog by remember { mutableStateOf(false) }

    // Dialog state variables
    var planNameInput by remember { mutableStateOf("") }
    var planDaysInput by remember { mutableStateOf("Mon, Wed, Fri") }
    var planDescInput by remember { mutableStateOf("") }

    var historyTitleInput by remember { mutableStateOf("") }
    var historyDurationInput by remember { mutableStateOf("45") }
    var historyCalsInput by remember { mutableStateOf("450") }
    var historyNotesInput by remember { mutableStateOf("") }

    var reelTitleInput by remember { mutableStateOf("") }
    var reelCaptionInput by remember { mutableStateOf("") }
    var reelTagInput by remember { mutableStateOf("MOTIVATION") }

    var memberIdInput by remember { mutableStateOf("SLAM-") }
    var memberNameInput by remember { mutableStateOf("") }
    var memberGoalInput by remember { mutableStateOf("Hypertrophy") }
    var memberStatusInput by remember { mutableStateOf("ACTIVE IN GYM") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SlamObsidian)
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Top Header with Branding & Top-Right Profile Avatar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Image(
                    painter = painterResource(id = R.drawable.img_gym_logo),
                    contentDescription = "SLAM Studio Logo",
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.5.dp, SlamCrimson, RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = androidx.compose.ui.text.buildAnnotatedString {
                                withStyle(androidx.compose.ui.text.SpanStyle(color = Color.White)) { append("S") }
                                withStyle(androidx.compose.ui.text.SpanStyle(color = SlamCrimson)) { append("L") }
                                withStyle(androidx.compose.ui.text.SpanStyle(color = Color.White)) { append("AM") }
                            },
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Black,
                                letterSpacing = 2.sp
                            )
                        )
                        Text(
                            text = "VILLIVAKKAM",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp,
                                color = Color.White
                            )
                        )
                    }
                    Text(
                        text = "HOME & TRAINING DASHBOARD",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = TextSecondary,
                            letterSpacing = 1.sp
                        )
                    )
                }
            }

            // Top Right Profile Icon Button & Logout Button
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Surface(
                    onClick = onOpenProfileDrawer,
                    shape = CircleShape,
                    color = SlamSurfaceElevated,
                    border = BorderStroke(1.5.dp, SlamCrimson)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(SlamCrimson),
                            contentAlignment = Alignment.Center
                        ) {
                            if (profile?.profilePhotoUri?.isNotEmpty() == true) {
                                Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                            } else {
                                val initials = profile?.name?.take(2)?.uppercase() ?: "VP"
                                Text(initials, style = MaterialTheme.typography.labelSmall.copy(color = Color.White, fontWeight = FontWeight.Black, fontSize = 11.sp))
                            }
                        }
                        Icon(Icons.Default.Settings, contentDescription = "Open Profile", tint = Color.White, modifier = Modifier.size(16.dp))
                    }
                }

                Surface(
                    onClick = onLogout,
                    shape = CircleShape,
                    color = SlamSurfaceElevated,
                    border = BorderStroke(1.5.dp, SlamCrimson)
                ) {
                    Box(
                        modifier = Modifier.padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Logout, contentDescription = "Logout", tint = SlamRedBright, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }

        // Hero Banner Card matching uploaded red logo theme
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, SlamBorder)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(id = R.drawable.img_slam_red_hero),
                    contentDescription = "SLAM Villivakkam Arena",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, SlamObsidian.copy(alpha = 0.9f))
                            )
                        )
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(14.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Badge(containerColor = SlamCrimson, contentColor = Color.White) {
                            Text("ROLE: $userRole", fontWeight = FontWeight.Black, fontSize = 10.sp)
                        }
                        Badge(containerColor = ElectricLime, contentColor = Color.Black) {
                            Text("CHENNAI HQ", fontWeight = FontWeight.Bold, fontSize = 9.sp)
                        }
                    }
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "Welcome back, ${profile?.name?.split(" ")?.firstOrNull() ?: "Champion"}!",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    )
                }
            }
        }

        // Compact Digital Pass Access Banner
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SlamSurface),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, SlamBorder)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("DIGITAL MEMBERSHIP PASS", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary, fontWeight = FontWeight.Bold))
                    Text(
                        text = "${profile?.memberIdCode ?: "SLAM-8849"} • ${profile?.membershipTier ?: "SLAM PRO MEMBER"}",
                        style = MaterialTheme.typography.titleSmall.copy(color = Color.White, fontWeight = FontWeight.Black)
                    )
                    Text("Valid until: ${profile?.dateOfValidity ?: "31 Dec 2026"}", style = MaterialTheme.typography.bodySmall.copy(color = ElectricLime, fontSize = 11.sp))
                }
                Button(
                    onClick = onShowQrPopup,
                    colors = ButtonDefaults.buttonColors(containerColor = SlamCrimson),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.QrCode, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("QR PASS", fontWeight = FontWeight.Black, fontSize = 11.sp)
                }
            }
        }

        // ADMIN / TRAINER SECTION (Only visible if Admin or Trainer)
        if (userRole == "TRAINER" || userRole == "ADMIN") {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1F1115)),
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.5.dp, SlamCrimson)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.SupervisorAccount, contentDescription = null, tint = SlamCrimson, modifier = Modifier.size(22.dp))
                            Column {
                                Text("$userRole DASHBOARD", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Black, color = Color.White))
                                Text("Track members & upload training content", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary, fontSize = 10.sp))
                            }
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Button(
                            onClick = { showUploadReelDialog = true },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = SlamCrimson),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.VideoCall, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("UPLOAD REEL", fontWeight = FontWeight.Black, fontSize = 11.sp)
                        }
                        OutlinedButton(
                            onClick = { showAddMemberDialog = true },
                            modifier = Modifier.weight(1f),
                            border = BorderStroke(1.dp, ElectricLime),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = ElectricLime),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.PersonAdd, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("TRACK MEMBER", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                    }

                    Text("TRACKED GYM MEMBERS (${trackedMembers.size})", style = MaterialTheme.typography.labelSmall.copy(color = Color.White, fontWeight = FontWeight.Bold))

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        trackedMembers.forEach { m ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(SlamSurface, RoundedCornerShape(12.dp))
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("${m.name} (${m.memberIdCode})", style = MaterialTheme.typography.titleSmall.copy(color = Color.White, fontWeight = FontWeight.Bold))
                                    Text("Goal: ${m.goal} • ${m.weightKg} kg", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary))
                                }
                                Badge(containerColor = if (m.attendanceStatus.contains("ACTIVE")) ElectricLime else SlamSurfaceElevated) {
                                    Text(
                                        m.attendanceStatus,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = if (m.attendanceStatus.contains("ACTIVE")) Color.Black else Color.White,
                                            fontWeight = FontWeight.Bold
                                        ),
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // DAILY WORKOUT PLANS SECTION
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "MY WORKOUT PLANS",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Black, color = Color.White)
                )
                TextButton(onClick = { showAddPlanDialog = true }) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = ElectricLime, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("ADD PLAN", color = ElectricLime, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }

            if (customPlans.isEmpty()) {
                Text("No custom workout plans added yet. Click Add Plan above!", color = TextSecondary, fontSize = 12.sp)
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    customPlans.forEach { plan ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = SlamSurface),
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(1.dp, SlamBorder)
                        ) {
                            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                    Text(plan.planName, style = MaterialTheme.typography.titleSmall.copy(color = Color.White, fontWeight = FontWeight.Black))
                                    Badge(containerColor = SlamCrimson) {
                                        Text("${plan.exercisesCount} Exercises", color = Color.White, fontSize = 10.sp)
                                    }
                                }
                                Text("Schedule: ${plan.targetDays} • Assigned by: ${plan.assignedBy}", style = MaterialTheme.typography.labelSmall.copy(color = ElectricLime))
                                Text(plan.description, style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, fontSize = 12.sp))
                            }
                        }
                    }
                }
            }
        }

        // SAVED WORKOUT HISTORY SECTION
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "WORKOUT HISTORY SAVED",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Black, color = Color.White)
                )
                TextButton(onClick = { showAddHistoryDialog = true }) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = SlamCrimson, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("LOG WORKOUT", color = SlamRedBright, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }

            if (workoutHistory.isEmpty()) {
                Text("No workout sessions saved yet. Click Log Workout above!", color = TextSecondary, fontSize = 12.sp)
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    workoutHistory.forEach { h ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = SlamSurface),
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(1.dp, SlamBorder)
                        ) {
                            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                    Text(h.workoutTitle, style = MaterialTheme.typography.titleSmall.copy(color = Color.White, fontWeight = FontWeight.Bold))
                                    Text(h.dateStr, style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary))
                                }
                                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                    Badge(containerColor = SlamSurfaceElevated, contentColor = ElectricLime) {
                                        Text("${h.durationMinutes} Mins", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }
                                    Badge(containerColor = SlamSurfaceElevated, contentColor = SlamRedBright) {
                                        Text("${h.caloriesBurned} Kcal Burned", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                                if (h.notes.isNotEmpty()) {
                                    Text("Notes: ${h.notes}", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, fontSize = 11.sp))
                                }
                            }
                        }
                    }
                }
            }
        }

        // GYM REELS & TRAINING VIDEOS FEED
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = "COMMUNITY REELS & TRAINER CLIPS",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Black, color = Color.White)
            )

            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(gymReels) { reel ->
                    Card(
                        modifier = Modifier
                            .width(220.dp)
                            .height(180.dp),
                        colors = CardDefaults.cardColors(containerColor = SlamSurface),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, SlamBorder)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Badge(containerColor = SlamCrimson, contentColor = Color.White) {
                                    Text(reel.categoryTag, fontSize = 9.sp, fontWeight = FontWeight.Black)
                                }
                                Text(reel.title, style = MaterialTheme.typography.titleSmall.copy(color = Color.White, fontWeight = FontWeight.Bold), maxLines = 2)
                                Text("By ${reel.uploaderName}", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary, fontSize = 10.sp))
                            }
                            Text(reel.videoUrlOrCaption, style = MaterialTheme.typography.bodySmall.copy(color = ElectricLime, fontSize = 11.sp), maxLines = 3)
                        }
                    }
                }
            }
        }

        // Quick Gym Stats Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                title = "CURRENT STREAK",
                value = "14 DAYS",
                subtitle = "Villivakkam Top 5%",
                icon = Icons.Default.LocalFireDepartment,
                accentColor = SlamCrimson,
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = "LOCKER ASSIGNED",
                value = "#104 ACTIVE",
                subtitle = "RFID Keycard Active",
                icon = Icons.Default.LockOpen,
                accentColor = ElectricLime,
                modifier = Modifier.weight(1f)
            )
        }

        // Share App Link Box
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SlamSurface),
            shape = RoundedCornerShape(18.dp),
            border = BorderStroke(1.dp, SlamBorder)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Default.InstallMobile, contentDescription = null, tint = ElectricLime, modifier = Modifier.size(24.dp))
                    Column {
                        Text("SHARE & INSTALL LINK", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Black, color = Color.White))
                        Text("iOS & Android Instant PWA / APK Setup", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary))
                    }
                }
                Button(
                    onClick = onOpenShareModal,
                    colors = ButtonDefaults.buttonColors(containerColor = SlamSurfaceElevated, contentColor = ElectricLime),
                    border = BorderStroke(1.dp, ElectricLime),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("SHARE LINK", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            }
        }

        Spacer(Modifier.height(30.dp))
    }

    // DIALOGS
    if (showAddPlanDialog) {
        AlertDialog(
            onDismissRequest = { showAddPlanDialog = false },
            containerColor = SlamSurface,
            titleContentColor = Color.White,
            title = { Text("Add Custom Workout Plan", fontWeight = FontWeight.Black) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(value = planNameInput, onValueChange = { planNameInput = it }, label = { Text("Plan Name") })
                    OutlinedTextField(value = planDaysInput, onValueChange = { planDaysInput = it }, label = { Text("Days (e.g. Mon, Wed, Fri)") })
                    OutlinedTextField(value = planDescInput, onValueChange = { planDescInput = it }, label = { Text("Description") })
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onAddCustomPlan(planNameInput, planDaysInput, 6, planDescInput)
                        showAddPlanDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SlamCrimson)
                ) { Text("SAVE PLAN", fontWeight = FontWeight.Black) }
            },
            dismissButton = { TextButton(onClick = { showAddPlanDialog = false }) { Text("CANCEL", color = TextSecondary) } }
        )
    }

    if (showAddHistoryDialog) {
        AlertDialog(
            onDismissRequest = { showAddHistoryDialog = false },
            containerColor = SlamSurface,
            titleContentColor = Color.White,
            title = { Text("Log Workout Session", fontWeight = FontWeight.Black) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(value = historyTitleInput, onValueChange = { historyTitleInput = it }, label = { Text("Workout Title") })
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(value = historyDurationInput, onValueChange = { historyDurationInput = it }, label = { Text("Mins") }, modifier = Modifier.weight(1f))
                        OutlinedTextField(value = historyCalsInput, onValueChange = { historyCalsInput = it }, label = { Text("Kcal") }, modifier = Modifier.weight(1f))
                    }
                    OutlinedTextField(value = historyNotesInput, onValueChange = { historyNotesInput = it }, label = { Text("Notes / PRs") })
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val dur = historyDurationInput.toIntOrNull() ?: 45
                        val cals = historyCalsInput.toIntOrNull() ?: 450
                        onAddWorkoutHistory(historyTitleInput, dur, cals, 5, historyNotesInput)
                        showAddHistoryDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SlamCrimson)
                ) { Text("LOG SESSION", fontWeight = FontWeight.Black) }
            },
            dismissButton = { TextButton(onClick = { showAddHistoryDialog = false }) { Text("CANCEL", color = TextSecondary) } }
        )
    }

    if (showUploadReelDialog && (userRole == "TRAINER" || userRole == "ADMIN")) {
        AlertDialog(
            onDismissRequest = { showUploadReelDialog = false },
            containerColor = SlamSurface,
            titleContentColor = Color.White,
            title = { Text("Upload Training Reel", fontWeight = FontWeight.Black) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(value = reelTitleInput, onValueChange = { reelTitleInput = it }, label = { Text("Reel Title") })
                    OutlinedTextField(value = reelCaptionInput, onValueChange = { reelCaptionInput = it }, label = { Text("Caption / Video Tip") })
                    OutlinedTextField(value = reelTagInput, onValueChange = { reelTagInput = it }, label = { Text("Category Tag (e.g. FORM TIP)") })
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onUploadReel(reelTitleInput, reelCaptionInput, reelTagInput)
                        showUploadReelDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SlamCrimson)
                ) { Text("PUBLISH REEL", fontWeight = FontWeight.Black) }
            },
            dismissButton = { TextButton(onClick = { showUploadReelDialog = false }) { Text("CANCEL", color = TextSecondary) } }
        )
    }

    if (showAddMemberDialog) {
        AlertDialog(
            onDismissRequest = { showAddMemberDialog = false },
            containerColor = SlamSurface,
            titleContentColor = Color.White,
            title = { Text("Track Member Status", fontWeight = FontWeight.Black) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(value = memberIdInput, onValueChange = { memberIdInput = it }, label = { Text("Member ID (e.g. SLAM-1234)") })
                    OutlinedTextField(value = memberNameInput, onValueChange = { memberNameInput = it }, label = { Text("Member Name") })
                    OutlinedTextField(value = memberGoalInput, onValueChange = { memberGoalInput = it }, label = { Text("Goal") })
                    OutlinedTextField(value = memberStatusInput, onValueChange = { memberStatusInput = it }, label = { Text("Status (e.g. IN GYM NOW)") })
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onAddOrUpdateMember(memberIdInput, memberNameInput, memberGoalInput, 78f, memberStatusInput)
                        showAddMemberDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SlamCrimson)
                ) { Text("SAVE RECORD", fontWeight = FontWeight.Black) }
            },
            dismissButton = { TextButton(onClick = { showAddMemberDialog = false }) { Text("CANCEL", color = TextSecondary) } }
        )
    }
}

@Composable
fun DetailCell(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector, highlight: Boolean = false) {
    Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (highlight) SlamCrimson else TextMuted,
            modifier = Modifier.size(16.dp).padding(top = 2.dp)
        )
        Column {
            Text(text = label, style = MaterialTheme.typography.labelSmall.copy(color = TextMuted, fontSize = 10.sp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = if (highlight) ElectricLime else Color.White,
                    fontWeight = if (highlight) FontWeight.ExtraBold else FontWeight.Bold
                )
            )
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = SlamSurface),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, SlamBorder)
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text(text = title, style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary, fontWeight = FontWeight.Bold))
                Icon(imageVector = icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(20.dp))
            }
            Text(text = value, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black, color = Color.White))
            Text(text = subtitle, style = MaterialTheme.typography.labelSmall.copy(color = accentColor))
        }
    }
}
