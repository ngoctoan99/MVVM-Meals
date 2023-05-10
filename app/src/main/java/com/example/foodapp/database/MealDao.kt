package com.example.foodapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.foodapp.model.Meal

@Dao
interface MealDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)

    /// insert data to local memory
    suspend fun upsert(meal : Meal)

    /// delete data to local memory
    @Delete
    suspend fun delete(meal:Meal)
    /// get all data to local memory
    @Query("SELECT * FROM mealInformation")
    fun getAllMeal():LiveData<List<Meal>>
}