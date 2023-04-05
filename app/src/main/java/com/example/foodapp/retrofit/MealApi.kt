package com.example.foodapp.retrofit

import com.example.foodapp.model.*
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

    @GET("filter.php")
    fun getMealsByCategory(@Query("c")categoryName : String):Call<MealByCategoryList>

    @GET("search.php?")
    fun searchMealByName(@Query("s") mealName: String):Call<MealList>

    @GET("list.php?a=list")
    fun getAllArea():Call<ListCountry>

    @GET("filter.php")
    fun getMealByArea(@Query("a") countryName:String):Call<MealByCategoryList>


}