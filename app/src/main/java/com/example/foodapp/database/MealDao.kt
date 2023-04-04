package com.example.foodapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.foodapp.model.Meal

@Dao
interface MealDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(meal : Meal)
    @Delete
    suspend fun delete(meal:Meal)
    @Query("SELECT * FROM mealInformation")
    fun getAllMeal():LiveData<List<Meal>>
}