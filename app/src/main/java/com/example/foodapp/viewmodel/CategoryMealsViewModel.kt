package com.example.foodapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodapp.model.MealByCategory
import com.example.foodapp.model.MealByCategoryList
import com.example.foodapp.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class CategoryMealsViewModel :ViewModel() {
    val mealLiveData = MutableLiveData<List<MealByCategory>>()

    fun getMealsByCategory(categoryName : String){
        RetrofitInstance.api.getMealsByCategory(categoryName).enqueue(object :Callback<MealByCategoryList>{
            override fun onResponse(
                call: Call<MealByCategoryList>,
                response: Response<MealByCategoryList>
            ) {
                response.body()?.let { mealByCategoryList ->  
                    mealLiveData.postValue(mealByCategoryList.meals)
                }
            }

            override fun onFailure(call: Call<MealByCategoryList>, t: Throwable) {
                Log.e("categorydata",t.message.toString())
            }

        })
    }
    fun observerMealsLiveData():LiveData<List<MealByCategory>>{
        return mealLiveData
    }
}