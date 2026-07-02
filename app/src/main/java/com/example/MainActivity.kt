package com.example

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.GymViewModel
import com.example.ui.screens.*
import com.example.ui.theme.*

class MainActivity : ComponentActivity() {
    private val viewModel: GymViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                GymSlamApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun GymSlamApp(viewModel: GymViewModel) {
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf("PASS") }

    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    val authMode by viewModel.authMode.collectAsState()
    val loginError by viewModel.loginError.collectAsState()
    val firebaseSyncStatus by viewModel.firebaseSyncStatus.collectAsState()

    val memberProfile by viewModel.memberProfile.collectAsState()
    val userRole by viewModel.userRole.collectAsState()
    val customPlans by viewModel.customPlans.collectAsState()
    val workoutHistory by viewModel.workoutHistory.collectAsState()
    val gymReels by viewModel.gymReels.collectAsState()
    val trackedMembers by viewModel.trackedMembers.collectAsState()

    val fitnessGoal by viewModel.fitnessGoal.collectAsState()
    val dailyLogs by viewModel.dailyLogs.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val categoryExercises by viewModel.categoryExercises.collectAsState()
    val selectedExerciseForGuide by viewModel.selectedExerciseForGuide.collectAsState()
    val selectedExerciseForVideo by viewModel.selectedExerciseForVideo.collectAsState()
    val isAnimationPlaying by viewModel.isAnimationPlaying.collectAsState()
    val dietMeals by viewModel.dietMeals.collectAsState()

    // Profile drawer & QR popups
    val showProfileDrawer by viewModel.showProfileDrawer.collectAsState()
    val showQrPopup by viewModel.showQrPopup.collectAsState()

    // Tracker
    val isTrackerActive by viewModel.isTrackerActive.collectAsState()
    val trackerSteps by viewModel.trackerSteps.collectAsState()
    val trackerDurationSec by viewModel.trackerDurationSec.collectAsState()
    val trackerCaloriesBurned by viewModel.trackerCaloriesBurned.collectAsState()
    val trackerWorkoutType by viewModel.trackerWorkoutType.collectAsState()

    // OTA & Share
    val showOtaBanner by viewModel.showOtaBanner.collectAsState()
    val showShareModal by viewModel.showShareModal.collectAsState()
    val otaProgress by viewModel.otaProgress.collectAsState()
    val otaInstalled by viewModel.otaInstalled.collectAsState()

    if (!isLoggedIn) {
        AuthScreen(
            authMode = authMode,
            onSelectMode = { viewModel.setAuthMode(it) },
            onLogin = { id, dob -> viewModel.loginUser(id, dob) },
            onRegister = { name, id, dob, mob, email, w, h, body, blood, gen, goal, health, role ->
                viewModel.registerNewMember(name, id, dob, mob, email, w, h, body, blood, gen, goal, health, role)
            },
            loginError = loginError,
            firebaseSyncStatus = firebaseSyncStatus
        )
        return
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = SlamObsidian,
        topBar = {
            if (showOtaBanner) {
                Surface(
                    color = SlamSurfaceElevated,
                    contentColor = Color.White,
                    tonalElevation = 6.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding(), start = 12.dp, end = 12.dp, bottom = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.SystemUpdate, contentDescription = null, tint = ElectricLime, modifier = Modifier.size(20.dp))
                            Column {
                                Text(
                                    if (otaInstalled) "SLAM v2.4 UPDATE INSTALLED" else "NEW SLAM VILLIVAKKAM PWA FEATURE v2.4",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Black, color = if (otaInstalled) ElectricLime else Color.White)
                                )
                                Text("Trainer Video Guides & Live Burn Tracker", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary, fontSize = 10.sp))
                            }
                        }

                        if (otaProgress > 0f && !otaInstalled) {
                            Text("${(otaProgress * 100).toInt()}%", fontWeight = FontWeight.Bold, color = ElectricLime, fontSize = 11.sp)
                        } else if (!otaInstalled) {
                            Button(
                                onClick = { viewModel.triggerOtaUpdate() },
                                colors = ButtonDefaults.buttonColors(containerColor = SlamCrimson),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("UPDATE NOW", fontSize = 10.sp, fontWeight = FontWeight.Black)
                            }
                        } else {
                            IconButton(onClick = { viewModel.dismissOtaBanner() }) {
                                Icon(Icons.Default.Close, contentDescription = "Close banner", tint = Color.White, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = SlamSurface,
                contentColor = SlamCrimson,
                tonalElevation = 8.dp,
                windowInsets = NavigationBarDefaults.windowInsets
            ) {
                NavigationBarItem(
                    selected = selectedTab == "PASS",
                    onClick = { selectedTab = "PASS" },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home Page") },
                    label = { Text("HOME PAGE", fontWeight = if (selectedTab == "PASS") FontWeight.Black else FontWeight.Normal) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = SlamCrimson,
                        indicatorColor = SlamCrimson,
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary
                    )
                )
                NavigationBarItem(
                    selected = selectedTab == "WORKOUTS",
                    onClick = { selectedTab = "WORKOUTS" },
                    icon = { Icon(Icons.Default.FitnessCenter, contentDescription = "Daily Workouts") },
                    label = { Text("WORKOUTS", fontWeight = if (selectedTab == "WORKOUTS") FontWeight.Black else FontWeight.Normal) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = SlamCrimson,
                        indicatorColor = SlamCrimson,
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary
                    )
                )
                NavigationBarItem(
                    selected = selectedTab == "TRACKING",
                    onClick = { selectedTab = "TRACKING" },
                    icon = { Icon(Icons.Default.QueryStats, contentDescription = "Calories & Goals") },
                    label = { Text("GOALS & DIET", fontWeight = if (selectedTab == "TRACKING") FontWeight.Black else FontWeight.Normal) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = SlamCrimson,
                        indicatorColor = SlamCrimson,
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary
                    )
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                "PASS" -> {
                    MembershipPassScreen(
                        profile = memberProfile,
                        userRole = userRole,
                        customPlans = customPlans,
                        workoutHistory = workoutHistory,
                        gymReels = gymReels,
                        trackedMembers = trackedMembers,
                        onOpenProfileDrawer = { viewModel.setShowProfileDrawer(true) },
                        onShowQrPopup = { viewModel.setShowQrPopup(true) },
                        onOpenShareModal = { viewModel.setShowShareModal(true) },
                        onLogout = { viewModel.logoutUser() },
                        onAddCustomPlan = { n, d, c, desc -> viewModel.addCustomWorkoutPlan(n, d, c, desc) },
                        onAddWorkoutHistory = { t, dur, cals, ex, notes -> viewModel.addWorkoutHistory(t, dur, cals, ex, notes) },
                        onUploadReel = { title, cap, tag -> viewModel.uploadGymReel(title, cap, tag) },
                        onAddOrUpdateMember = { id, name, goal, w, status -> viewModel.addOrUpdateTrackedMember(id, name, goal, w, status) }
                    )
                }
                "WORKOUTS" -> {
                    WorkoutsScreen(
                        categories = listOf("CHEST", "BACK", "LEGS", "ARMS"),
                        selectedCategory = selectedCategory,
                        onSelectCategory = { viewModel.selectExerciseCategory(it) },
                        exercises = categoryExercises,
                        onOpenGuide = { viewModel.openExerciseLineGuide(it) },
                        onOpenVideoModal = { viewModel.openTrainerVideoModal(it) },
                        onToggleCompleted = { id, status -> viewModel.toggleExerciseCompleted(id, status) },
                        isTrackerActive = isTrackerActive,
                        trackerSteps = trackerSteps,
                        trackerDurationSec = trackerDurationSec,
                        trackerCaloriesBurned = trackerCaloriesBurned,
                        workoutType = trackerWorkoutType,
                        onSelectWorkoutType = { viewModel.setWorkoutType(it) },
                        onToggleTracker = { viewModel.toggleLiveTracker() },
                        onSaveTrackerSession = { viewModel.saveTrackerSessionToDailyLog() }
                    )
                }
                "TRACKING" -> {
                    TrackingAndGoalsScreen(
                        goal = fitnessGoal,
                        dailyLogs = dailyLogs,
                        dietMeals = dietMeals,
                        onSelectGoalType = { viewModel.selectGoalType(it) },
                        onLogToday = { dateStr, weight, consumed, burned ->
                            viewModel.logDailyEntry(dateStr, weight, consumed, burned)
                        }
                    )
                }
            }
        }

        // MODALS
        if (showProfileDrawer) {
            MemberProfileDrawerModal(
                profile = memberProfile,
                onDismiss = { viewModel.setShowProfileDrawer(false) },
                onSaveDetails = { name, mobile, w, h, gender, goal, health ->
                    viewModel.saveUpdatedProfileDetails(name, mobile, w, h, gender, goal, health)
                },
                onUpdatePhoto = { uriOrPreset -> viewModel.updateProfilePhoto(uriOrPreset) },
                onShowQr = { viewModel.setShowQrPopup(true) },
                onLogout = { viewModel.logoutUser() }
            )
        }

        if (showQrPopup) {
            QrVerificationModal(
                profile = memberProfile,
                onDismiss = { viewModel.setShowQrPopup(false) }
            )
        }

        if (selectedExerciseForVideo != null) {
            TrainerVideoUploadModal(
                exercise = selectedExerciseForVideo!!,
                userRole = userRole,
                onDismiss = { viewModel.openTrainerVideoModal(null) },
                onSaveVideo = { url, tip ->
                    viewModel.updateExerciseTrainerVideo(selectedExerciseForVideo!!.id, url, tip)
                }
            )
        }

        if (selectedExerciseForGuide != null) {
            FormLineGuideModal(
                exercise = selectedExerciseForGuide!!,
                isAnimationPlaying = isAnimationPlaying,
                onTogglePlay = { viewModel.toggleAnimationPlayback() },
                onDismiss = { viewModel.openExerciseLineGuide(null) }
            )
        }

        if (showShareModal) {
            val appShareUrl = "https://slamstudio.app/villivakkam-pass"
            val clipboardManager = LocalClipboardManager.current

            AlertDialog(
                onDismissRequest = { viewModel.setShowShareModal(false) },
                containerColor = SlamSurface,
                title = {
                    Text("SHARE SLAM VILLIVAKKAM APP", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black, color = Color.White))
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            "Club Member Pass & App Invite Link:",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                        )
                        Surface(
                            color = SlamObsidian,
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, SlamCrimson)
                        ) {
                            Text(
                                appShareUrl,
                                style = MaterialTheme.typography.labelSmall.copy(color = ElectricLime, fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                        Card(
                            colors = CardDefaults.cardColors(containerColor = SlamSurfaceElevated),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(10.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Icon(Icons.Default.InstallMobile, contentDescription = null, tint = ElectricLime, modifier = Modifier.size(16.dp))
                                    Text("PUBLISH & INSTALL GUIDE:", style = MaterialTheme.typography.labelSmall.copy(color = ElectricLime, fontWeight = FontWeight.Black))
                                }
                                Text("• Web Preview: Click the Share button at the top right of Google AI Studio to generate a live public preview link.", style = MaterialTheme.typography.labelSmall.copy(color = Color.White, fontSize = 11.sp))
                                Text("• Android Install: Click the Export / Download APK menu in AI Studio to install directly onto mobile devices.", style = MaterialTheme.typography.labelSmall.copy(color = Color.White, fontSize = 11.sp))
                            }
                        }
                    }
                },
                confirmButton = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                            OutlinedButton(
                                onClick = {
                                    clipboardManager.setText(AnnotatedString(appShareUrl))
                                    Toast.makeText(context, "Club Invite Link copied!", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.weight(1f),
                                border = BorderStroke(1.dp, ElectricLime),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = ElectricLime)
                            ) {
                                Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(Modifier.width(4.dp))
                                Text("COPY LINK", fontWeight = FontWeight.Black, fontSize = 10.sp)
                            }

                            Button(
                                onClick = {
                                    val shareIntent = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        putExtra(
                                            Intent.EXTRA_TEXT,
                                            "Join SLAM Villivakkam Studio!\nGet your membership pass & workout app access:\n$appShareUrl"
                                        )
                                        type = "text/plain"
                                    }
                                    context.startActivity(Intent.createChooser(shareIntent, "Share SLAM App"))
                                    viewModel.setShowShareModal(false)
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = SlamCrimson)
                            ) {
                                Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(Modifier.width(4.dp))
                                Text("SHARE NOW", fontWeight = FontWeight.Black, fontSize = 10.sp)
                            }
                        }
                        TextButton(
                            onClick = { viewModel.setShowShareModal(false) },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("CLOSE", color = TextSecondary)
                        }
                    }
                },
                dismissButton = {}
            )
        }
    }
}
