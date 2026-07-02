package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GymDao {
    @Query("SELECT * FROM member_profile WHERE id = 1")
    fun getMemberProfile(): Flow<MemberProfile?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMemberProfile(profile: MemberProfile)

    @Query("SELECT * FROM fitness_goals WHERE id = 1")
    fun getFitnessGoal(): Flow<FitnessGoal?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveFitnessGoal(goal: FitnessGoal)

    @Query("SELECT * FROM daily_logs ORDER BY id DESC")
    fun getAllDailyLogs(): Flow<List<DailyLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyLog(log: DailyLog)

    @Query("SELECT * FROM workout_exercises WHERE category = :category")
    fun getExercisesByCategory(category: String): Flow<List<ExerciseItem>>

    @Query("SELECT * FROM workout_exercises")
    fun getAllExercises(): Flow<List<ExerciseItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercises(exercises: List<ExerciseItem>)

    @Query("UPDATE workout_exercises SET isCompletedToday = :completed WHERE id = :id")
    suspend fun updateExerciseCompletion(id: Int, completed: Boolean)

    @Query("UPDATE workout_exercises SET trainerVideoUrl = :videoUrl, trainerTip = :tip WHERE id = :id")
    suspend fun updateExerciseVideo(id: Int, videoUrl: String, tip: String)

    @Query("SELECT * FROM diet_meals WHERE goalAssociated = :goalType OR goalAssociated = 'GENERAL'")
    fun getDietMealsForGoal(goalType: String): Flow<List<DietMealItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDietMeals(meals: List<DietMealItem>)

    @Query("SELECT * FROM custom_workout_plans ORDER BY id DESC")
    fun getAllCustomPlans(): Flow<List<CustomWorkoutPlan>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomPlan(plan: CustomWorkoutPlan)

    @Query("SELECT * FROM workout_history ORDER BY id DESC")
    fun getAllWorkoutHistory(): Flow<List<WorkoutHistoryItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutHistory(item: WorkoutHistoryItem)

    @Query("SELECT * FROM gym_reels ORDER BY id DESC")
    fun getAllGymReels(): Flow<List<GymReelItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGymReel(reel: GymReelItem)

    @Query("SELECT * FROM tracked_members ORDER BY id DESC")
    fun getAllTrackedMembers(): Flow<List<TrackedMemberItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrackedMember(member: TrackedMemberItem)
}

