package com.example.foodapp.retrofit

import com.example.foodapp.model.CategoryList
import com.example.foodapp.model.MealByCategoryList
import com.example.foodapp.model.MealList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApi {
    @GET("random.php")
    fun getRandomMeal(): Call<MealList>

    @GET("lookup.php?")
    fun getMealDetail(@Query("i")id:String): Call<MealList>

    @GET("filter.php?")
    fun getPopularItems(@Query("c")categoryName:String): Call<MealByCategoryList>

    @GET("categories.php")
    fun getCategories(): Call<CategoryList>
}