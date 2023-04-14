package com.example.foodapp.viewmodel

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.ContactsContract
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
    private var countryLiveData = MutableLiveData<List<Country>>()
    private var ingredientLiveData = MutableLiveData<List<Ingredient>>()
    private var searchIngredientLiveData = MutableLiveData<List<Ingredient>>()
    private var bottomSheetMealLiveData = MutableLiveData<Meal>()
    private var bottomSheetIngredientLiveData = MutableLiveData<List<MealByCategory>>()
    private var searchMealLiveData = MutableLiveData<List<Meal>>()
    private var favoriteMealLIveData = mealDatabase.mealDao().getAllMeal()
    private var saveSateRandomMeal : Meal ?= null
    fun getRandomMeal(){
        saveSateRandomMeal?.let {
            randomMealLiveData.postValue(it)
            return
        }
        RetrofitInstance.api.getRandomMeal().enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if(response.body() != null){
                    val randomMeal : Meal = response.body()!!.meals[0]
                    randomMealLiveData.value = randomMeal
                    saveSateRandomMeal = randomMeal
                }else {
                    return
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
 //////////////////// old code
    fun getCategories(){
        RetrofitInstance.api.getCategories().enqueue(object : Callback<CategoryList>{
            override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
                if(response.body() != null){
                    response.body()?.let { categoryList ->
                        categoryLiveData.postValue(categoryList.categories)
                    }
                }else {
                    return
                }
            }

            override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                Log.d("categorylist",t.message.toString())
            }

        })
    }
    fun observerCategoryLIveData():LiveData<List<Category>>{
        return categoryLiveData
    }
 /////////////////////////////////////////////////
    fun searchMeal(searchQuery: String) = RetrofitInstance.api.searchMealByName(searchQuery).enqueue(object :Callback<MealList>{
        override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
            val mealList = response.body()?.meals
            mealList?.let {
                searchMealLiveData.postValue(it)
            }
        }
        override fun onFailure(call: Call<MealList>, t: Throwable) {
            Log.e("toandatasearch",t.message.toString())
        }
    })

    fun searchIngredient(searchQuery: String) {
        val listSearch : ArrayList<Ingredient> = ArrayList()
        RetrofitInstance.api.getAllIngredient().enqueue(object:Callback<IngredinentList>{
            override fun onResponse(
                call: Call<IngredinentList>,
                response: Response<IngredinentList>
            ) {
                if(response.body() != null){
                    response.body()?.let {
                        for( i  in it.meals){
                            if(i.strIngredient.lowercase().contains(searchQuery)){
                                listSearch.add(i)
                            }
                        }
                        searchIngredientLiveData.postValue(listSearch)
                    }
                }else {
                    return
                }
            }
            override fun onFailure(call: Call<IngredinentList>, t: Throwable) {
            }
        })
    }
    fun getCountry(){
        RetrofitInstance.api.getAllArea().enqueue(object: Callback<ListCountry>{
            override fun onResponse(call: Call<ListCountry>, response: Response<ListCountry>) {
                if(response.body() != null){
                    response.body()?.let { countryList ->
                        countryLiveData.postValue(countryList.meals)
                    }
                }else {
                    return
                }
            }

            override fun onFailure(call: Call<ListCountry>, t: Throwable) {
            }

        })
    }
    fun getAllIngredient(){
        RetrofitInstance.api.getAllIngredient().enqueue(object:Callback<IngredinentList>{
            override fun onResponse(
                call: Call<IngredinentList>,
                response: Response<IngredinentList>
            ) {
                if(response.body() != null){
                    response.body()?.let {
                        ingredientLiveData.postValue(it.meals)
                    }
                }else {
                    return
                }
            }

            override fun onFailure(call: Call<IngredinentList>, t: Throwable) {

            }

        })
    }

    fun observerCountryLiveData():LiveData<List<Country>> = countryLiveData
    fun observerSearchMealLiveData():LiveData<List<Meal>> = searchMealLiveData
    fun observerSearchIngredientLiveData():LiveData<List<Ingredient>> = searchIngredientLiveData
    fun observerRandomMealLiveData():LiveData<Meal>{
        return randomMealLiveData
    }
    fun observerPopularItemLiveData():LiveData<List<MealByCategory>>{
        return popularItemLiveData
    }
    fun observeFavoritesMealsLiveData():LiveData<List<Meal>>{
        return favoriteMealLIveData
    }
    fun observeIngredientLiveData():LiveData<List<Ingredient>> = ingredientLiveData

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
    fun getMealById(id : String){
        RetrofitInstance.api.getMealDetail(id).enqueue(object :Callback<MealList>{
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                val meal = response.body()?.meals?.first()
                meal?.let {
                    bottomSheetMealLiveData.postValue(it)
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.e("toandata",t.message.toString())
            }

        })
    }

    fun getMealByIngredientName(ingredientName:String){
        RetrofitInstance.api.getMealByIngredient(ingredientName).enqueue(object :Callback<MealByCategoryList>{
            override fun onResponse(
                call: Call<MealByCategoryList>,
                response: Response<MealByCategoryList>
            ) {
                if(response.body() != null){
                    response.body()?.let {
                        bottomSheetIngredientLiveData.postValue(it.meals)
                    }
                }else {
                    return
                }
            }

            override fun onFailure(call: Call<MealByCategoryList>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    fun observerBottomSheetIngredient():LiveData<List<MealByCategory>> = bottomSheetIngredientLiveData

    fun observeBottomSheetMeal():LiveData<Meal> = bottomSheetMealLiveData
}