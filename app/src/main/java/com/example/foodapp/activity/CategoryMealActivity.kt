package com.example.foodapp.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodapp.R
import com.example.foodapp.adapter.CategoryMealsAdapter
import com.example.foodapp.databinding.ActivityCategoryMealBinding
import com.example.foodapp.fragment.HomeFragment
import com.example.foodapp.viewmodel.CategoryMealsViewModel

class CategoryMealActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCategoryMealBinding
    private lateinit var categoryMealsViewModel : CategoryMealsViewModel
    private lateinit var categoryName : String
    private lateinit var intentString : String
    private lateinit var categoryMealsAdapter : CategoryMealsAdapter
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryMealBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left)
        prepareRecyclerView()
        categoryName = intent.getStringExtra(HomeFragment.CATEGORY_NAME)!!


        // instance categoryMealsViewModel
        categoryMealsViewModel = ViewModelProviders.of(this)[CategoryMealsViewModel::class.java]
        categoryMealsViewModel.getMealsByCategory(categoryName)

        // get data MVVM category meal
        observeCategoryMealData()
        onClickCategoryMeal()
    }

    private fun observeCategoryMealData() {
        binding.rvMeals.visibility = View.GONE
        binding.isLoading.visibility = View.VISIBLE
        categoryMealsViewModel.observerMealsLiveData().observe(this, Observer {mealList ->
            categoryMealsAdapter.setMealList(mealList)
            binding.rvMeals.visibility = View.VISIBLE
            binding.isLoading.visibility = View.GONE
            binding.tvCategoryCount.text = "$categoryName : ${mealList.size}"
        })
    }

    // set up recyclerview
    private fun prepareRecyclerView() {
        categoryMealsAdapter = CategoryMealsAdapter()
        binding.rvMeals.apply {
            layoutManager = GridLayoutManager(context,2,GridLayoutManager.VERTICAL, false)
            adapter = categoryMealsAdapter
        }
    }
    // action click item category
    private fun onClickCategoryMeal(){
        categoryMealsAdapter.onItemClick = {
            val intent = Intent(this,MealActivity::class.java)
            intent.putExtra(HomeFragment.MEAL_ID,it.idMeal)
            intent.putExtra(HomeFragment.MEAL_NAME,it.strMeal)
            intent.putExtra(HomeFragment.MEAL_THUMB,it.strMealThumb)
            startActivity(intent)
        }
    }

}