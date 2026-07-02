package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class GymRepository(private val dao: GymDao) {
    val memberProfile: Flow<MemberProfile?> = dao.getMemberProfile()
    val fitnessGoal: Flow<FitnessGoal?> = dao.getFitnessGoal()
    val dailyLogs: Flow<List<DailyLog>> = dao.getAllDailyLogs()
    val allExercises: Flow<List<ExerciseItem>> = dao.getAllExercises()

    fun getExercisesByCategory(category: String): Flow<List<ExerciseItem>> = dao.getExercisesByCategory(category)
    fun getDietMealsForGoal(goalType: String): Flow<List<DietMealItem>> = dao.getDietMealsForGoal(goalType)

    val customPlans: Flow<List<CustomWorkoutPlan>> = dao.getAllCustomPlans()
    val workoutHistory: Flow<List<WorkoutHistoryItem>> = dao.getAllWorkoutHistory()
    val gymReels: Flow<List<GymReelItem>> = dao.getAllGymReels()
    val trackedMembers: Flow<List<TrackedMemberItem>> = dao.getAllTrackedMembers()

    suspend fun insertCustomPlan(plan: CustomWorkoutPlan) = dao.insertCustomPlan(plan)
    suspend fun insertWorkoutHistory(item: WorkoutHistoryItem) = dao.insertWorkoutHistory(item)
    suspend fun insertGymReel(reel: GymReelItem) = dao.insertGymReel(reel)
    suspend fun insertTrackedMember(member: TrackedMemberItem) = dao.insertTrackedMember(member)

    suspend fun saveMemberProfile(profile: MemberProfile) = dao.saveMemberProfile(profile)
    suspend fun saveFitnessGoal(goal: FitnessGoal) = dao.saveFitnessGoal(goal)
    suspend fun logDailyActivity(log: DailyLog) = dao.insertDailyLog(log)
    suspend fun toggleExerciseCompletion(id: Int, completed: Boolean) = dao.updateExerciseCompletion(id, completed)
    suspend fun updateExerciseVideo(id: Int, videoUrl: String, tip: String) = dao.updateExerciseVideo(id, videoUrl, tip)

    suspend fun seedDatabaseIfEmpty() {
        val currentProfile = dao.getMemberProfile().firstOrNull()
        if (currentProfile == null) {
            dao.saveMemberProfile(
                MemberProfile(
                    id = 1,
                    name = "Alex \"The Slammer\" Vance",
                    dob = "14 Aug 1996",
                    mobileNumber = "+1 (555) 092-8411",
                    memberIdCode = "SLAM-8849",
                    dateOfJoining = "01 Jan 2026",
                    dateOfValidity = "31 Dec 2026",
                    membershipTier = "PLATINUM SLAM MEMBER",
                    gymBranch = "SLAM Lifestyle & Fitness Studio • Villivakkam",
                    status = "ACTIVE MEMBER",
                    weightKg = 78.5f,
                    heightCm = 178.0f,
                    gender = "Male",
                    fitnessGoalText = "Muscle Hypertrophy & Shred",
                    healthIssues = "None (Cleared for intense lifting)",
                    profilePhotoUri = ""
                )
            )

            dao.saveFitnessGoal(
                FitnessGoal(
                    id = 1,
                    goalType = "REDUCE_WEIGHT",
                    startWeightKg = 84.0f,
                    currentWeightKg = 78.5f,
                    targetWeightKg = 73.0f,
                    targetDays = 60,
                    dailyCalorieTarget = 1950,
                    dailyCalorieBurnTarget = 650,
                    proteinGrams = 175,
                    carbsGrams = 150,
                    fatGrams = 55
                )
            )

            dao.insertDailyLog(DailyLog(dateStr = "28 Jun", weightKg = 81.2f, caloriesConsumed = 2050, caloriesBurned = 610))
            dao.insertDailyLog(DailyLog(dateStr = "29 Jun", weightKg = 80.5f, caloriesConsumed = 1980, caloriesBurned = 680))
            dao.insertDailyLog(DailyLog(dateStr = "30 Jun", weightKg = 79.8f, caloriesConsumed = 1920, caloriesBurned = 710))
            dao.insertDailyLog(DailyLog(dateStr = "01 Jul", weightKg = 79.1f, caloriesConsumed = 1950, caloriesBurned = 640))
            dao.insertDailyLog(DailyLog(dateStr = "02 Jul (Today)", weightKg = 78.5f, caloriesConsumed = 1420, caloriesBurned = 580))

            val exercises = listOf(
                // CHEST WORKOUTS
                ExerciseItem(
                    category = "CHEST",
                    name = "Barbell Bench Press",
                    setsReps = "4 Sets • 8-10 Reps",
                    restSeconds = 90,
                    targetMuscle = "Mid & Lower Pectorals",
                    equipment = "Olympic Barbell & Flat Bench",
                    executionSteps = "1. Plant feet firmly into floor & retract shoulder blades back.\n2. Grip bar slightly wider than shoulder-width.\n3. Lower bar with elbows tucked at 45° angle to lower chest.\n4. Drive bar upward in smooth curved trajectory over collarbone.",
                    lineAnimationTrajectory = "CHEST_BENCH_PRESS"
                ),
                ExerciseItem(
                    category = "CHEST",
                    name = "Incline Dumbbell Fly",
                    setsReps = "3 Sets • 12-15 Reps",
                    restSeconds = 60,
                    targetMuscle = "Upper Chest (Clavicular Head)",
                    equipment = "Incline Bench (30°) & Dumbbells",
                    executionSteps = "1. Set bench angle to 30-45 degrees.\n2. Maintain slight 15° bend in elbows throughout movement.\n3. Lower dumbbells in wide arc until chest stretch is felt.\n4. Squeeze chest to bring arms back up together like hugging a barrel.",
                    lineAnimationTrajectory = "CHEST_INCLINE_FLY"
                ),
                ExerciseItem(
                    category = "CHEST",
                    name = "High-to-Low Cable Crossover",
                    setsReps = "4 Sets • 15 Reps",
                    restSeconds = 45,
                    targetMuscle = "Lower Chest Squeeze & Definition",
                    equipment = "Dual Cable Pulley Station",
                    executionSteps = "1. Set pulleys to highest top notch.\n2. Step forward in staggered stance with 20° torso lean.\n3. Sweep handles downward and across lower waistline.\n4. Hold isometric squeeze at bottom intersection for 1 full second.",
                    lineAnimationTrajectory = "CHEST_CABLE_CROSSOVER"
                ),
                ExerciseItem(
                    category = "CHEST",
                    name = "Forward-Lean Chest Dips",
                    setsReps = "3 Sets • To Failure",
                    restSeconds = 75,
                    targetMuscle = "Outer & Lower Pectoral Border",
                    equipment = "Parallel Dip Bars",
                    executionSteps = "1. Grip bars and lean torso forward 30 degrees.\n2. Lower body until upper arm is parallel to floor.\n3. Press powerfully back up without fully locking elbows.",
                    lineAnimationTrajectory = "CHEST_DIPS"
                ),

                // BACK WORKOUTS
                ExerciseItem(
                    category = "BACK",
                    name = "Wide-Grip Lat Pulldown",
                    setsReps = "4 Sets • 10-12 Reps",
                    restSeconds = 60,
                    targetMuscle = "Latissimus Dorsi (V-Taper)",
                    equipment = "Cable Lat Machine",
                    executionSteps = "1. Grip bar wider than shoulders.\n2. Drive elbows straight down to ribcage while puffing chest.\n3. Control eccentric stretch back up slowly.",
                    lineAnimationTrajectory = "BACK_PULLDOWN"
                ),
                ExerciseItem(
                    category = "BACK",
                    name = "Barbell Bent-Over Row",
                    setsReps = "4 Sets • 8 Reps",
                    restSeconds = 90,
                    targetMuscle = "Rhomboids & Mid-Back Thickness",
                    equipment = "Barbell",
                    executionSteps = "1. Hinge hips back at 45° torso angle.\n2. Pull barbell toward navel by driving elbows behind torso.\n3. Squeeze shoulder blades together at peak contraction.",
                    lineAnimationTrajectory = "BACK_ROW"
                ),

                // LEGS WORKOUTS
                ExerciseItem(
                    category = "LEGS",
                    name = "Barbell Back Squat",
                    setsReps = "4 Sets • 8 Reps",
                    restSeconds = 120,
                    targetMuscle = "Quadriceps & Glutes",
                    equipment = "Squat Rack & Barbell",
                    executionSteps = "1. Rest bar securely across upper traps.\n2. Brace core and break at hips and knees simultaneously.\n3. Descend until thighs break parallel with floor.\n4. Drive through mid-foot back to standing.",
                    lineAnimationTrajectory = "LEGS_SQUAT"
                ),

                // ARMS WORKOUTS
                ExerciseItem(
                    category = "ARMS",
                    name = "Incline Alternating Dumbbell Curl",
                    setsReps = "3 Sets • 12 Reps",
                    restSeconds = 60,
                    targetMuscle = "Biceps Brachii (Long Head)",
                    equipment = "Incline Bench & Dumbbells",
                    executionSteps = "1. Sit back on 45° incline bench to let arms hang behind torso.\n2. Curl dumbbell up while supinating wrist.\n3. Lower with control under full stretch.",
                    lineAnimationTrajectory = "ARMS_CURL"
                )
            )
            dao.insertExercises(exercises)

            val dietMeals = listOf(
                // REDUCE WEIGHT MEALS
                DietMealItem(
                    goalAssociated = "REDUCE_WEIGHT",
                    mealTiming = "BREAKFAST",
                    mealName = "Avocado Egg-White Slam Scramble",
                    calories = 340,
                    proteinG = 32,
                    carbsG = 14,
                    fatG = 12,
                    ingredients = "5 Egg whites, 1 whole egg, 50g sliced avocado, spinach, cherry tomatoes, multi-grain toast slice."
                ),
                DietMealItem(
                    goalAssociated = "REDUCE_WEIGHT",
                    mealTiming = "PRE-WORKOUT",
                    mealName = "Berry & Greek Yogurt Energy Cup",
                    calories = 210,
                    proteinG = 22,
                    carbsG = 28,
                    fatG = 2,
                    ingredients = "150g 0% Greek Yogurt, half cup blueberries, 10g chia seeds, dash of organic honey."
                ),
                DietMealItem(
                    goalAssociated = "REDUCE_WEIGHT",
                    mealTiming = "POST-WORKOUT LUNCH",
                    mealName = "Grilled Lemon Herb Chicken Bowl",
                    calories = 520,
                    proteinG = 54,
                    carbsG = 45,
                    fatG = 14,
                    ingredients = "200g grilled chicken breast, 120g cooked quinoa, steamed broccoli florets, olive oil drizzle."
                ),
                DietMealItem(
                    goalAssociated = "REDUCE_WEIGHT",
                    mealTiming = "DINNER",
                    mealName = "Wild Seared Salmon & Asparagus",
                    calories = 460,
                    proteinG = 42,
                    carbsG = 16,
                    fatG = 22,
                    ingredients = "180g Atlantic salmon fillet, roasted asparagus garlic spears, mixed green salad with lemon vinaigrette."
                ),

                // GAIN WEIGHT / MUSCLE BUILD MEALS
                DietMealItem(
                    goalAssociated = "GAIN_WEIGHT",
                    mealTiming = "BREAKFAST",
                    mealName = "Powerhouse Peanut Butter Oats Bowl",
                    calories = 680,
                    proteinG = 42,
                    carbsG = 78,
                    fatG = 24,
                    ingredients = "100g rolled oats, 1 scoop whey protein, 2 tbsp natural peanut butter, whole banana, whole milk."
                ),
                DietMealItem(
                    goalAssociated = "GAIN_WEIGHT",
                    mealTiming = "POST-WORKOUT LUNCH",
                    mealName = "Beast Mode Ribeye & Sweet Potato",
                    calories = 820,
                    proteinG = 65,
                    carbsG = 68,
                    fatG = 32,
                    ingredients = "250g lean ribeye steak, large roasted sweet potato, sautéed green beans in grass-fed butter."
                ),
                DietMealItem(
                    goalAssociated = "GAIN_WEIGHT",
                    mealTiming = "DINNER",
                    mealName = "Salmon & Brown Rice Mass Feast",
                    calories = 740,
                    proteinG = 52,
                    carbsG = 65,
                    fatG = 28,
                    ingredients = "220g salmon fillet, 200g brown rice, avocado slices, roasted zucchini peppers."
                )
            )
            dao.insertDietMeals(dietMeals)

            dao.insertCustomPlan(
                CustomWorkoutPlan(
                    planName = "SLAM Villivakkam 5-Day Push/Pull/Legs Split",
                    targetDays = "Mon, Tue, Wed, Fri, Sat",
                    exercisesCount = 18,
                    description = "Master Trainer Vikram's recommended hypertrophy routine focused on progressive overload and high intensity.",
                    assignedBy = "Coach Vikram"
                )
            )
            dao.insertCustomPlan(
                CustomWorkoutPlan(
                    planName = "Extreme Fat Burn & HIIT Cardio Blitz",
                    targetDays = "Tue, Thu, Sat",
                    exercisesCount = 12,
                    description = "High-energy interval conditioning designed to maximize metabolic burn rate for 48 hours post-workout.",
                    assignedBy = "SLAM Studio AI"
                )
            )

            dao.insertWorkoutHistory(
                WorkoutHistoryItem(
                    dateStr = "Yesterday, 6:30 PM",
                    workoutTitle = "Heavy Chest & Anterior Delts",
                    durationMinutes = 54,
                    caloriesBurned = 580,
                    exercisesCompleted = 4,
                    notes = "Completed 100kg Bench Press PR! Great pump on high-to-low cable crossovers."
                )
            )
            dao.insertWorkoutHistory(
                WorkoutHistoryItem(
                    dateStr = "30 Jun, 7:15 AM",
                    workoutTitle = "V-Taper Lat Pulldown & Rows",
                    durationMinutes = 48,
                    caloriesBurned = 510,
                    exercisesCompleted = 4,
                    notes = "Focused on 3-second eccentric stretch on lat pulldowns."
                )
            )

            dao.insertGymReel(
                GymReelItem(
                    title = "Explosive 220kg Deadlift Form Breakdown",
                    uploaderName = "Trainer Vikram • SLAM Villivakkam",
                    videoUrlOrCaption = "Keep lat engagement locked. Push the floor away rather than pulling with the lower back!",
                    likesCount = 384,
                    categoryTag = "FORM TIP"
                )
            )
            dao.insertGymReel(
                GymReelItem(
                    title = "SLAM Villivakkam Community Transformation Reel",
                    uploaderName = "Admin SLAM",
                    videoUrlOrCaption = "Congrats to member Rahul for shredding 12kg in 90 days with disciplined meal tracking & PPL split!",
                    likesCount = 512,
                    categoryTag = "MOTIVATION"
                )
            )

            val initialMembers = listOf(
                TrackedMemberItem(1, "SLAM-8849", "Alex Vance", "Muscle Hypertrophy", 78.5f, "ACTIVE IN GYM", 92),
                TrackedMemberItem(2, "SLAM-1042", "Rahul Sharma", "Fat Loss Shred", 86.2f, "CHECKED IN 1H AGO", 88),
                TrackedMemberItem(3, "SLAM-2091", "Divya Menon", "Toning & Cardio", 62.0f, "YESTERDAY", 95),
                TrackedMemberItem(4, "SLAM-3118", "Karthik Raj", "Powerlifting PR", 94.5f, "ACTIVE IN GYM", 90),
                TrackedMemberItem(5, "SLAM-4402", "Srinivasan K", "General Fitness", 75.0f, "3 DAYS AGO", 78)
            )
            initialMembers.forEach { dao.insertTrackedMember(it) }
        }
    }
}
