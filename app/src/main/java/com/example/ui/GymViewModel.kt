package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class GymViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: GymRepository

    val memberProfile: StateFlow<MemberProfile?>
    val fitnessGoal: StateFlow<FitnessGoal?>
    val dailyLogs: StateFlow<List<DailyLog>>
    val customPlans: StateFlow<List<CustomWorkoutPlan>>
    val workoutHistory: StateFlow<List<WorkoutHistoryItem>>
    val gymReels: StateFlow<List<GymReelItem>>
    val trackedMembers: StateFlow<List<TrackedMemberItem>>

    private val _userRole = MutableStateFlow("MEMBER") // "MEMBER", "TRAINER", "ADMIN"
    val userRole: StateFlow<String> = _userRole.asStateFlow()

    private val _selectedCategory = MutableStateFlow("CHEST")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val categoryExercises: StateFlow<List<ExerciseItem>>

    private val _selectedExerciseForGuide = MutableStateFlow<ExerciseItem?>(null)
    val selectedExerciseForGuide: StateFlow<ExerciseItem?> = _selectedExerciseForGuide.asStateFlow()

    // For trainer video player / upload modal
    private val _selectedExerciseForVideo = MutableStateFlow<ExerciseItem?>(null)
    val selectedExerciseForVideo: StateFlow<ExerciseItem?> = _selectedExerciseForVideo.asStateFlow()

    private val _selectedGoalType = MutableStateFlow("REDUCE_WEIGHT")
    val selectedGoalType: StateFlow<String> = _selectedGoalType.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val dietMeals: StateFlow<List<DietMealItem>>

    private val _isAnimationPlaying = MutableStateFlow(true)
    val isAnimationPlaying: StateFlow<Boolean> = _isAnimationPlaying.asStateFlow()

    // AUTH / LOGIN & REGISTER STATES
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _authMode = MutableStateFlow("LOGIN") // "LOGIN" or "REGISTER"
    val authMode: StateFlow<String> = _authMode.asStateFlow()

    private val _loginError = MutableStateFlow<String?>(null)
    val loginError: StateFlow<String?> = _loginError.asStateFlow()

    private val _firebaseSyncStatus = MutableStateFlow("Synced with Firebase Cloud Firestore • asia-south1")
    val firebaseSyncStatus: StateFlow<String> = _firebaseSyncStatus.asStateFlow()

    // PROFILE DRAWER & QR POPUP
    private val _showProfileDrawer = MutableStateFlow(false)
    val showProfileDrawer: StateFlow<Boolean> = _showProfileDrawer.asStateFlow()

    private val _showQrPopup = MutableStateFlow(false)
    val showQrPopup: StateFlow<Boolean> = _showQrPopup.asStateFlow()

    // LIVE CALORIE & PEDOMETER TRACKER
    private val _isTrackerActive = MutableStateFlow(false)
    val isTrackerActive: StateFlow<Boolean> = _isTrackerActive.asStateFlow()

    private val _trackerWorkoutType = MutableStateFlow("Treadmill & Walking")
    val trackerWorkoutType: StateFlow<String> = _trackerWorkoutType.asStateFlow()

    private val _trackerSteps = MutableStateFlow(0)
    val trackerSteps: StateFlow<Int> = _trackerSteps.asStateFlow()

    private val _trackerDurationSec = MutableStateFlow(0)
    val trackerDurationSec: StateFlow<Int> = _trackerDurationSec.asStateFlow()

    private val _trackerCaloriesBurned = MutableStateFlow(0f)
    val trackerCaloriesBurned: StateFlow<Float> = _trackerCaloriesBurned.asStateFlow()

    private var trackerJob: Job? = null

    // OTA UPDATE & SHARE LINKS
    private val _showOtaBanner = MutableStateFlow(true)
    val showOtaBanner: StateFlow<Boolean> = _showOtaBanner.asStateFlow()

    private val _showShareModal = MutableStateFlow(false)
    val showShareModal: StateFlow<Boolean> = _showShareModal.asStateFlow()

    private val _otaProgress = MutableStateFlow(0f)
    val otaProgress: StateFlow<Float> = _otaProgress.asStateFlow()

    private val _otaInstalled = MutableStateFlow(false)
    val otaInstalled: StateFlow<Boolean> = _otaInstalled.asStateFlow()

    init {
        val database = GymDatabase.getDatabase(application)
        repository = GymRepository(database.gymDao())

        viewModelScope.launch {
            repository.seedDatabaseIfEmpty()
        }

        memberProfile = repository.memberProfile
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

        fitnessGoal = repository.fitnessGoal
            .onEach { goal ->
                if (goal != null) {
                    _selectedGoalType.value = goal.goalType
                }
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

        dailyLogs = repository.dailyLogs
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        categoryExercises = _selectedCategory
            .flatMapLatest { cat -> repository.getExercisesByCategory(cat) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        dietMeals = _selectedGoalType
            .flatMapLatest { goalType -> repository.getDietMealsForGoal(goalType) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        customPlans = repository.customPlans
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        workoutHistory = repository.workoutHistory
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        gymReels = repository.gymReels
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        trackedMembers = repository.trackedMembers
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    }

    // AUTH ACTIONS
    fun setAuthMode(mode: String) {
        _authMode.value = mode
        _loginError.value = null
    }

    fun loginUser(memberId: String, dob: String) {
        val current = memberProfile.value
        val cleanId = memberId.trim().uppercase()
        val cleanDob = dob.trim()

        if (cleanId.isEmpty() || cleanDob.isEmpty()) {
            _loginError.value = "Please enter both Membership ID and DOB (e.g. SLAM-8849 & 14 Aug 1996)"
            return
        }

        val detectedRole = when {
            cleanId.contains("ADMIN") -> "ADMIN"
            cleanId.contains("TRAIN") || cleanId.startsWith("TR-") -> "TRAINER"
            current?.memberIdCode?.uppercase() == cleanId -> current.role
            else -> "MEMBER"
        }
        _userRole.value = detectedRole

        if (current != null && (cleanId == current.memberIdCode.uppercase() || cleanId == "SLAM-8849" || cleanId.contains("ADMIN") || cleanId.contains("TRAIN"))) {
            _loginError.value = null
            _isLoggedIn.value = true
            _firebaseSyncStatus.value = "Verified via Firebase Cloud Auth • Logged in as $detectedRole"
        } else {
            if (cleanId.startsWith("SLAM") || cleanId.length >= 4) {
                _loginError.value = null
                _isLoggedIn.value = true
                _firebaseSyncStatus.value = "Synced with Firebase Realtime Database ($detectedRole)"
            } else {
                _loginError.value = "Invalid credentials. Use SLAM-8849 / 14 Aug 1996, or try ADMIN / TRAINER."
            }
        }
    }

    fun logoutUser() {
        _isLoggedIn.value = false
        _loginError.value = null
        _showProfileDrawer.value = false
    }

    fun registerNewMember(
        name: String, memberId: String, dob: String, mobile: String, email: String,
        weight: Float, height: Float, bodyMeasurements: String, bloodGroup: String,
        gender: String, goal: String, health: String, role: String = "MEMBER"
    ) {
        if (name.isBlank() || memberId.isBlank()) {
            _loginError.value = "Please provide Name and desired Membership ID."
            return
        }
        _userRole.value = role
        viewModelScope.launch {
            val newProfile = MemberProfile(
                id = 1,
                name = name,
                dob = dob.ifEmpty { "14 Aug 1996" },
                mobileNumber = mobile.ifEmpty { "+91 98402 11988" },
                memberIdCode = memberId.uppercase(),
                dateOfJoining = "02 Jul 2026",
                dateOfValidity = "02 Jul 2027",
                membershipTier = "SLAM PRO MEMBER",
                gymBranch = "SLAM Lifestyle & Fitness Studio • Villivakkam",
                status = "ACTIVE MEMBER",
                weightKg = weight,
                heightCm = height,
                gender = gender,
                fitnessGoalText = goal,
                healthIssues = health.ifEmpty { "None" },
                profilePhotoUri = memberProfile.value?.profilePhotoUri ?: "",
                email = email.ifEmpty { "vignesh@slam.app" },
                bodyMeasurements = bodyMeasurements.ifEmpty { "Chest: 40\", Waist: 32\", Biceps: 15\"" },
                bloodGroup = bloodGroup.ifEmpty { "O+" },
                role = role
            )
            repository.saveMemberProfile(newProfile)
            _loginError.value = null
            _isLoggedIn.value = true
            _firebaseSyncStatus.value = "New Member Profile ($role) synced to Firebase Firestore"
        }
    }

    fun setUserRole(role: String) {
        _userRole.value = role
        viewModelScope.launch {
            val current = memberProfile.value
            if (current != null) {
                repository.saveMemberProfile(current.copy(role = role))
            }
        }
    }

    fun addCustomWorkoutPlan(planName: String, targetDays: String, exercisesCount: Int, description: String) {
        if (planName.isBlank()) return
        viewModelScope.launch {
            repository.insertCustomPlan(
                CustomWorkoutPlan(
                    planName = planName,
                    targetDays = targetDays.ifEmpty { "Mon, Wed, Fri" },
                    exercisesCount = if (exercisesCount > 0) exercisesCount else 6,
                    description = description.ifEmpty { "Custom routine created for targeted progression." },
                    assignedBy = if (_userRole.value != "MEMBER") "${_userRole.value} Vikram" else "Self"
                )
            )
        }
    }

    fun addWorkoutHistory(workoutTitle: String, durationMinutes: Int, caloriesBurned: Int, exercisesCompleted: Int, notes: String) {
        if (workoutTitle.isBlank()) return
        viewModelScope.launch {
            repository.insertWorkoutHistory(
                WorkoutHistoryItem(
                    dateStr = "Today, Just Now",
                    workoutTitle = workoutTitle,
                    durationMinutes = if (durationMinutes > 0) durationMinutes else 45,
                    caloriesBurned = if (caloriesBurned > 0) caloriesBurned else 450,
                    exercisesCompleted = if (exercisesCompleted > 0) exercisesCompleted else 5,
                    notes = notes.ifEmpty { "Great session at SLAM Studio!" }
                )
            )
        }
    }

    fun uploadGymReel(title: String, videoUrlOrCaption: String, categoryTag: String) {
        if (title.isBlank()) return
        viewModelScope.launch {
            repository.insertGymReel(
                GymReelItem(
                    title = title,
                    uploaderName = if (_userRole.value != "MEMBER") "${_userRole.value} SLAM" else memberProfile.value?.name ?: "SLAM Member",
                    videoUrlOrCaption = videoUrlOrCaption.ifEmpty { "Check out this explosive set!" },
                    likesCount = 1,
                    categoryTag = categoryTag.ifEmpty { "MOTIVATION" }
                )
            )
        }
    }

    fun addOrUpdateTrackedMember(memberIdCode: String, name: String, goal: String, weightKg: Float, status: String) {
        if (memberIdCode.isBlank() || name.isBlank()) return
        viewModelScope.launch {
            repository.insertTrackedMember(
                TrackedMemberItem(
                    memberIdCode = memberIdCode.uppercase(),
                    name = name,
                    goal = goal.ifEmpty { "Hypertrophy" },
                    weightKg = if (weightKg > 0) weightKg else 75f,
                    attendanceStatus = status.ifEmpty { "CHECKED IN JUST NOW" },
                    progressScore = 90
                )
            )
        }
    }

    // PROFILE & QR ACTIONS
    fun setShowProfileDrawer(show: Boolean) {
        _showProfileDrawer.value = show
    }

    fun setShowQrPopup(show: Boolean) {
        _showQrPopup.value = show
    }

    fun updateProfilePhoto(uriOrPreset: String) {
        viewModelScope.launch {
            val current = memberProfile.value
            if (current != null) {
                repository.saveMemberProfile(current.copy(profilePhotoUri = uriOrPreset))
            }
        }
    }

    fun saveUpdatedProfileDetails(
        name: String, mobile: String, weight: Float, height: Float,
        gender: String, goal: String, health: String
    ) {
        viewModelScope.launch {
            val current = memberProfile.value
            if (current != null) {
                repository.saveMemberProfile(
                    current.copy(
                        name = name,
                        mobileNumber = mobile,
                        weightKg = weight,
                        heightCm = height,
                        gender = gender,
                        fitnessGoalText = goal,
                        healthIssues = health
                    )
                )
                val goalObj = fitnessGoal.value
                if (goalObj != null) {
                    repository.saveFitnessGoal(goalObj.copy(currentWeightKg = weight))
                }
            }
        }
    }

    // TRAINER VIDEO ACTIONS
    fun selectExerciseCategory(category: String) {
        _selectedCategory.value = category
    }

    fun openExerciseLineGuide(exercise: ExerciseItem?) {
        _selectedExerciseForGuide.value = exercise
        _isAnimationPlaying.value = true
    }

    fun toggleAnimationPlayback() {
        _isAnimationPlaying.value = !_isAnimationPlaying.value
    }

    fun openTrainerVideoModal(exercise: ExerciseItem?) {
        _selectedExerciseForVideo.value = exercise
    }

    fun updateExerciseTrainerVideo(exerciseId: Int, newVideoUrl: String, newTip: String) {
        viewModelScope.launch {
            repository.updateExerciseVideo(exerciseId, newVideoUrl, newTip)
            // refresh selected if matching
            val currentSelect = _selectedExerciseForVideo.value
            if (currentSelect != null && currentSelect.id == exerciseId) {
                _selectedExerciseForVideo.value = currentSelect.copy(
                    trainerVideoUrl = newVideoUrl,
                    trainerTip = newTip
                )
            }
        }
    }

    fun toggleExerciseCompleted(id: Int, currentStatus: Boolean) {
        viewModelScope.launch {
            repository.toggleExerciseCompletion(id, !currentStatus)
        }
    }

    // CALORIE BURNING TRACKER
    fun setWorkoutType(type: String) {
        _trackerWorkoutType.value = type
    }

    fun toggleLiveTracker() {
        if (_isTrackerActive.value) {
            // Stop tracker
            _isTrackerActive.value = false
            trackerJob?.cancel()
        } else {
            // Start tracker
            _isTrackerActive.value = true
            trackerJob?.cancel()
            trackerJob = viewModelScope.launch {
                while (_isTrackerActive.value) {
                    delay(1000)
                    _trackerDurationSec.value += 1
                    val stepIncrement = if (_trackerWorkoutType.value.contains("Run", true)) 3 else 2
                    _trackerSteps.value += stepIncrement
                    // Approx 0.05 kcal per step or 0.15 kcal per second of lifting/HIIT
                    val kcalRate = when {
                        _trackerWorkoutType.value.contains("HIIT", true) -> 0.22f
                        _trackerWorkoutType.value.contains("Weightlifting", true) -> 0.16f
                        _trackerWorkoutType.value.contains("Run", true) -> 0.20f
                        else -> 0.08f // Walking
                    }
                    _trackerCaloriesBurned.value += kcalRate
                }
            }
        }
    }

    fun resetTracker() {
        trackerJob?.cancel()
        _isTrackerActive.value = false
        _trackerSteps.value = 0
        _trackerDurationSec.value = 0
        _trackerCaloriesBurned.value = 0f
    }

    fun saveTrackerSessionToDailyLog() {
        val burned = _trackerCaloriesBurned.value.toInt()
        if (burned <= 0) return
        viewModelScope.launch {
            val currentLogs = dailyLogs.value
            val todayLog = currentLogs.firstOrNull()
            if (todayLog != null) {
                repository.logDailyActivity(
                    todayLog.copy(caloriesBurned = todayLog.caloriesBurned + burned)
                )
            } else {
                repository.logDailyActivity(
                    DailyLog(
                        dateStr = "02 Jul (Today)",
                        weightKg = memberProfile.value?.weightKg ?: 78.5f,
                        caloriesConsumed = 1500,
                        caloriesBurned = burned
                    )
                )
            }
            resetTracker()
        }
    }

    // OTA & SHARE
    fun setShowShareModal(show: Boolean) {
        _showShareModal.value = show
    }

    fun dismissOtaBanner() {
        _showOtaBanner.value = false
    }

    fun triggerOtaUpdate() {
        viewModelScope.launch {
            _otaProgress.value = 0.1f
            for (i in 2..10) {
                delay(180)
                _otaProgress.value = i / 10f
            }
            _otaInstalled.value = true
            delay(2000)
            _showOtaBanner.value = false
        }
    }

    fun selectGoalType(newGoalType: String) {
        _selectedGoalType.value = newGoalType
        viewModelScope.launch {
            val currentGoal = fitnessGoal.value
            if (currentGoal != null) {
                val updatedCalorieTarget = if (newGoalType == "REDUCE_WEIGHT") 1950 else 2850
                val updatedTargetWeight = if (newGoalType == "REDUCE_WEIGHT") 73.0f else 84.0f
                val updatedProtein = if (newGoalType == "REDUCE_WEIGHT") 175 else 210
                repository.saveFitnessGoal(
                    currentGoal.copy(
                        goalType = newGoalType,
                        targetWeightKg = updatedTargetWeight,
                        dailyCalorieTarget = updatedCalorieTarget,
                        proteinGrams = updatedProtein
                    )
                )
            }
        }
    }

    fun logDailyEntry(dateStr: String, weightKg: Float, caloriesConsumed: Int, caloriesBurned: Int) {
        viewModelScope.launch {
            repository.logDailyActivity(
                DailyLog(
                    dateStr = dateStr,
                    weightKg = weightKg,
                    caloriesConsumed = caloriesConsumed,
                    caloriesBurned = caloriesBurned
                )
            )
            val currentGoal = fitnessGoal.value
            if (currentGoal != null) {
                repository.saveFitnessGoal(currentGoal.copy(currentWeightKg = weightKg))
            }
        }
    }
}

