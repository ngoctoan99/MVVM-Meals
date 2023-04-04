package com.example.foodapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.database.MealDatabase
import com.example.foodapp.model.*
import com.example.foodapp.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(
    private val mealDatabase: MealDatabase
):ViewModel() {
    private var randomMealLiveData = MutableLiveData<Meal>()
    private var popularItemLiveData = MutableLiveData<List<MealByCategory>>()
    private var categoryLiveData = MutableLiveData<List<Category>>()
    private var favoriteMealLIveData = mealDatabase.mealDao().getAllMeal()
    fun getRandomMeal(){
        RetrofitInstance.api.getRandomMeal().enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if(response.body() != null){
                    val randomMeal : Meal = response.body()!!.meals[0]
                    randomMealLiveData.value = randomMeal
                }else {

                }
            }
            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("fail",t.message.toString())
            }
        })
    }

    fun getPopularItems(){
        RetrofitInstance.api.getPopularItems("Seafood").enqueue(object :Callback<MealByCategoryList>{
            override fun onResponse(call: Call<MealByCategoryList>, response: Response<MealByCategoryList>) {
                if(response.body() != null) {
                    popularItemLiveData.value = response.body()!!.meals
                }else {

                }
            }

            override fun onFailure(call: Call<MealByCategoryList>, t: Throwable) {
                Log.d("homefragmentdata",t.message.toString())
            }

        })
    }
    fun getCategories(){
        RetrofitInstance.api.getCategories().enqueue(object : Callback<CategoryList>{
            override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
                if(response.body() != null){
                    response.body()?.let { categoryList ->  
                        categoryLiveData.postValue(categoryList.categories)
                    }
                }else {

                }
            }

            override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                Log.d("categorylist",t.message.toString())
            }

        })
    }
    fun observerRandomMealLiveData():LiveData<Meal>{
        return randomMealLiveData
    }
    fun observerPopularItemLiveData():LiveData<List<MealByCategory>>{
        return popularItemLiveData
    }

    fun observerCategoryLIveData():LiveData<List<Category>>{
        return categoryLiveData
    }

    fun observeFavoritesMealsLiveData():LiveData<List<Meal>>{
        return favoriteMealLIveData
    }
    fun deleteMeal(meal:Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().delete(meal)
        }
    }
    fun insertMeal(meal :Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().upsert(meal)
        }
    }
}