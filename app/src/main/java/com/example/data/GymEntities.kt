package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "member_profile")
data class MemberProfile(
    @PrimaryKey val id: Int = 1,
    val name: String,
    val dob: String,
    val mobileNumber: String,
    val memberIdCode: String,
    val dateOfJoining: String,
    val dateOfValidity: String,
    val membershipTier: String = "SLAM PRO MEMBER",
    val gymBranch: String = "SLAM Lifestyle & Fitness Studio • Villivakkam",
    val status: String = "ACTIVE MEMBER",
    val weightKg: Float = 78.5f,
    val heightCm: Float = 178.0f,
    val gender: String = "Male",
    val fitnessGoalText: String = "Muscle Hypertrophy & Shred",
    val healthIssues: String = "None (Cleared for intense lifting)",
    val profilePhotoUri: String = "",
    val email: String = "member@slam.app",
    val bodyMeasurements: String = "Chest: 40\", Waist: 32\", Biceps: 15\"",
    val bloodGroup: String = "O+",
    val role: String = "MEMBER" // "MEMBER", "TRAINER", "ADMIN"
)

@Entity(tableName = "fitness_goals")
data class FitnessGoal(
    @PrimaryKey val id: Int = 1,
    val goalType: String, // "REDUCE_WEIGHT" or "GAIN_WEIGHT" or "MUSCLE_BUILD"
    val startWeightKg: Float,
    val currentWeightKg: Float,
    val targetWeightKg: Float,
    val targetDays: Int, // e.g. 60 days
    val dailyCalorieTarget: Int, // calculated calorie budget
    val dailyCalorieBurnTarget: Int,
    val proteinGrams: Int,
    val carbsGrams: Int,
    val fatGrams: Int
)

@Entity(tableName = "daily_logs")
data class DailyLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val dateStr: String,
    val weightKg: Float,
    val caloriesConsumed: Int,
    val caloriesBurned: Int,
    val workoutsCompleted: Int = 1
)

@Entity(tableName = "workout_exercises")
data class ExerciseItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val category: String, // "CHEST", "BACK", "LEGS", "ARMS", "CARDIO"
    val name: String,
    val setsReps: String,
    val restSeconds: Int,
    val targetMuscle: String,
    val equipment: String,
    val executionSteps: String, // newline separated steps
    val lineAnimationTrajectory: String, // "ARC_UP", "PRESS_FORWARD", "PULL_DOWN", "SQUAT_DOWN"
    val isCompletedToday: Boolean = false,
    val trainerVideoUrl: String = "https://slamstudio.video/demo_chest.mp4",
    val trainerName: String = "Master Trainer Vikram • SLAM Villivakkam",
    val trainerTip: String = "Keep elbows at a 45-degree angle. Control the eccentric descent for 3 seconds."
)

@Entity(tableName = "diet_meals")
data class DietMealItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val goalAssociated: String, // "REDUCE_WEIGHT", "GAIN_WEIGHT", "GENERAL"
    val mealTiming: String, // "BREAKFAST", "PRE-WORKOUT", "POST-WORKOUT", "DINNER"
    val mealName: String,
    val calories: Int,
    val proteinG: Int,
    val carbsG: Int,
    val fatG: Int,
    val ingredients: String
)

@Entity(tableName = "custom_workout_plans")
data class CustomWorkoutPlan(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val planName: String,
    val targetDays: String,
    val exercisesCount: Int,
    val description: String,
    val assignedBy: String = "Self / Coach"
)

@Entity(tableName = "workout_history")
data class WorkoutHistoryItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val dateStr: String,
    val workoutTitle: String,
    val durationMinutes: Int,
    val caloriesBurned: Int,
    val exercisesCompleted: Int,
    val notes: String
)

@Entity(tableName = "gym_reels")
data class GymReelItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val uploaderName: String,
    val videoUrlOrCaption: String,
    val likesCount: Int = 142,
    val categoryTag: String = "MOTIVATION"
)

@Entity(tableName = "tracked_members")
data class TrackedMemberItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val memberIdCode: String,
    val name: String,
    val goal: String,
    val weightKg: Float,
    val attendanceStatus: String,
    val progressScore: Int
)

