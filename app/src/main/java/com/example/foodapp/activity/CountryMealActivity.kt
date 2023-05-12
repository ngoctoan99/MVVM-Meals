package com.example.foodapp.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodapp.R
import com.example.foodapp.adapter.CategoryMealsAdapter
import com.example.foodapp.adapter.CountryAdapter
import com.example.foodapp.databinding.ActivityCategoryMealBinding
import com.example.foodapp.databinding.ActivityCountryMealBinding
import com.example.foodapp.fragment.HomeFragment
import com.example.foodapp.viewmodel.CategoryMealsViewModel
import com.example.foodapp.viewmodel.CountryMealViewModel

class CountryMealActivity : AppCompatActivity() {
    private lateinit var countryMealsAdapter: CategoryMealsAdapter
    private lateinit var countryViewModel: CountryMealViewModel
    private lateinit var countryName: String
    private lateinit var binding : ActivityCountryMealBinding
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCountryMealBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prepareRecyclerView()
        //get name country to activity before
        countryName = intent.getStringExtra(HomeFragment.COUNTRY_NAME)!!
        // instance countryViewmodel
        countryViewModel = ViewModelProviders.of(this)[CountryMealViewModel::class.java]
        countryViewModel.getMealsByCountry(countryName)

        // get data MVVM to set adapter
        countryViewModel.observerMealsLiveData().observe(this, Observer {mealList ->
            countryMealsAdapter.setMealList(mealList)
            binding.tvCountryName.text = "$countryName : ${mealList.size}"
        })
        onClickMeal()

    }
    /// action click item in recyclerview
    private fun onClickMeal() {
        countryMealsAdapter.onItemClick = {
            val intent = Intent(this,MealActivity::class.java)
            intent.putExtra(HomeFragment.MEAL_ID,it.idMeal)
            intent.putExtra(HomeFragment.MEAL_NAME,it.strMeal)
            intent.putExtra(HomeFragment.MEAL_THUMB,it.strMealThumb)
            startActivity(intent)
        }
    }
    // setup recyclerview
    private fun prepareRecyclerView() {
        countryMealsAdapter = CategoryMealsAdapter()
        binding.rvMealsCountry.apply {
            adapter = countryMealsAdapter
            layoutManager = GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
        }
    }
}