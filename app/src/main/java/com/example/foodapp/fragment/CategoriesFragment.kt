package com.example.foodapp.fragment

import android.content.Intent
import android.os.Binder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.R
import com.example.foodapp.activity.CategoryMealActivity
import com.example.foodapp.activity.MainActivity
import com.example.foodapp.activity.MealActivity
import com.example.foodapp.adapter.CategoryAdapter
import com.example.foodapp.adapter.CategoryFragmentAdapter
import com.example.foodapp.adapter.CategoryMealsAdapter
import com.example.foodapp.adapter.FavoriteMealAdapter
import com.example.foodapp.databinding.FragmentCategoriesBinding
import com.example.foodapp.viewmodel.CategoryMealsViewModel
import com.example.foodapp.viewmodel.HomeViewModel


class CategoriesFragment : Fragment() {
    private lateinit var binding : FragmentCategoriesBinding
    private lateinit var categoryAdapter: CategoryFragmentAdapter
    private lateinit var viewModel : HomeViewModel

    private lateinit var categoryMealsViewModel : CategoryMealsViewModel
    private lateinit var categoryMealsAdapter : CategoryMealsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoriesBinding.inflate(LayoutInflater.from(context),container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ///// set data category item
        onPrepareRecyclerViewCategory()
        observeCategory()
        onClickCategory()
        //set data meal item
        setMealData("Beef")
    }
    private fun setMealData(categoryName:String) {
        categoryMealsViewModel = ViewModelProviders.of(this)[CategoryMealsViewModel::class.java]
        categoryMealsViewModel.getMealsByCategory(categoryName)
        categoryMealsViewModel.observerMealsLiveData().observe(viewLifecycleOwner){
            categoryMealsAdapter.setMealList(it)
        }
        onPrepareRecyclerViewMeal()
        onClickItemMeal()
    }
    private fun onClickCategory() {
       categoryAdapter.onItemClick = {
           setMealData(it.strCategory)
       }
    }
    private fun observeCategory() {
        viewModel.observerCategoryLIveData().observe(viewLifecycleOwner){
            it[0].isCheck = true
            for (i in 1 until it.size){
                it[i].isCheck = false
            }
            categoryAdapter.setCategoryList(it)
        }
    }
    private fun onPrepareRecyclerViewCategory() {
        categoryAdapter = CategoryFragmentAdapter()
        binding.rvCategories.apply {
            adapter = categoryAdapter
            layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        }
    }
    private fun onPrepareRecyclerViewMeal(){
        categoryMealsAdapter = CategoryMealsAdapter()
        binding.rvMeal.apply {
            adapter = categoryMealsAdapter
            layoutManager = GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
        }
    }

    private fun onClickItemMeal(){
        categoryMealsAdapter.onItemClick = {
            val intent = Intent(context, MealActivity::class.java)
            intent.putExtra(HomeFragment.MEAL_ID,it.idMeal)
            intent.putExtra(HomeFragment.MEAL_NAME,it.strMeal)
            intent.putExtra(HomeFragment.MEAL_THUMB,it.strMealThumb)
            startActivity(intent)
        }
    }
}