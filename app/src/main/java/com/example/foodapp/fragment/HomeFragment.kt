package com.example.foodapp.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat.recreate
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.foodapp.R
import com.example.foodapp.activity.CategoryMealActivity
import com.example.foodapp.activity.MainActivity
import com.example.foodapp.adapter.MostPopularAdapter
import com.example.foodapp.activity.MealActivity
import com.example.foodapp.adapter.CategoryAdapter
import com.example.foodapp.databinding.FragmentHomeBinding
import com.example.foodapp.fragment.bottomDialog.MealBottomSheetFragment
import com.example.foodapp.model.MealByCategory
import com.example.foodapp.model.Meal
import com.example.foodapp.viewmodel.HomeViewModel


class HomeFragment : Fragment() {
    private lateinit var binding : FragmentHomeBinding
    private lateinit var viewModel : HomeViewModel
    private lateinit var randomMeal : Meal
    private lateinit var popularItemAdapter: MostPopularAdapter
    private lateinit var categoryAdapter : CategoryAdapter
    companion object{
        const val MEAL_ID = "com.example.foodapp.fragment.idMeal"
        const val MEAL_NAME = "com.example.foodapp.fragment.nameMeal"
        const val MEAL_THUMB = "com.example.foodapp.fragment.thumbMeal"
        const val CATEGORY_NAME = "com.example.foodapp.fragment.categoryName"
        const val COUNTRY_NAME = "om.example.foodapp.fragment.countryName"
        const val SAVE = "com.example.foodapp.fragment.save"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        homeViewModel = ViewModelProviders.of(this)[HomeViewModel::class.java]
        viewModel = (activity as MainActivity).viewModel
        popularItemAdapter = MostPopularAdapter()
        categoryAdapter = CategoryAdapter()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preparePopularItemRecyclerView()

        viewModel.getRandomMeal()
        observerRandomMeal()
        onRanDomMealClick()

        viewModel.getPopularItems()
        observerPopularItemsLiveData()
        onPopularItemClick()
        onPopularItemLongClick()

        prepareCategoriesRecyclerView()
        viewModel.getCategories()
        observerCategoryLiveData()
        onCategoryClick()


        onClickSearch()
    }

    private fun onClickSearch() {
        binding.imgSearch.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchMealFragment)
        }
    }


    private fun onPopularItemLongClick() {
        popularItemAdapter.onLongItemClick = { meal ->
            val mealBottomSheetFragment = MealBottomSheetFragment.newInstance(meal.idMeal)
            mealBottomSheetFragment.show(childFragmentManager,"Meal Info")
        }
    }

    private fun onCategoryClick() {
        categoryAdapter.onItemClick = { category ->  
            val intent = Intent(activity,CategoryMealActivity::class.java)
            intent.putExtra(CATEGORY_NAME,category.strCategory)
            startActivity(intent)
        }
    }

    private fun prepareCategoriesRecyclerView() {
        binding.recViewCategories.apply {
            layoutManager = GridLayoutManager(context,3,GridLayoutManager.VERTICAL,false)
            adapter = categoryAdapter
        }
    }

    private fun observerCategoryLiveData() {
        viewModel.observerCategoryLIveData().observe(viewLifecycleOwner) { categoryList->
               categoryAdapter.setCategoryList(categoryList)
        }
    }

    private fun onPopularItemClick() {
        popularItemAdapter.onItemClick = { meal ->
            val intent = Intent(activity,MealActivity::class.java)
            intent.putExtra(MEAL_ID,meal.idMeal)
            intent.putExtra(MEAL_NAME,meal.strMeal)
            intent.putExtra(MEAL_THUMB,meal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun preparePopularItemRecyclerView() {
        binding.recViewMealPopular.apply {
            layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
            adapter = popularItemAdapter
        }
    }

    private fun observerPopularItemsLiveData() {
        viewModel.observerPopularItemLiveData().observe(viewLifecycleOwner
        ) { mealList ->
            popularItemAdapter.setMeals(mealList = mealList as ArrayList<MealByCategory> )
        }
    }

    private fun onRanDomMealClick() {
        binding.randomMealCard.setOnClickListener {
            val intent = Intent(activity,MealActivity::class.java)
            intent.putExtra(MEAL_ID,randomMeal.idMeal)
            intent.putExtra(MEAL_NAME,randomMeal.strMeal)
            intent.putExtra(MEAL_THUMB,randomMeal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun observerRandomMeal(){
        viewModel.observerRandomMealLiveData().observe(viewLifecycleOwner) { meal ->
            Glide.with(this@HomeFragment)
                .load(meal!!.strMealThumb)
                .into(binding.imgRandomMeal)
            this.randomMeal = meal
        }
    }
}