package com.example.foodapp.fragment.bottomDialog

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.foodapp.R
import com.example.foodapp.activity.MainActivity
import com.example.foodapp.activity.MealActivity
import com.example.foodapp.adapter.CategoryMealsAdapter
import com.example.foodapp.databinding.FragmentIngredientBottomSheetBinding
import com.example.foodapp.fragment.HomeFragment
import com.example.foodapp.viewmodel.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

private const val INGREDIENT_NAME = "param3"
private const val INGREDIENT_DESCRIPTION = "param4"
class IngredientBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding : FragmentIngredientBottomSheetBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var bottomIngredientAdapter : CategoryMealsAdapter

    private var ingredientName: String? = ""
    private var ingredientDescription: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            ingredientName = it.getString(INGREDIENT_NAME)
            ingredientDescription =it.getString(INGREDIENT_DESCRIPTION)
        }
        viewModel = (activity as MainActivity).viewModel
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentIngredientBottomSheetBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //set data view orther
        setDateView()
        //set up data in rv bottom ingredient
        prepareRecyclerView()
        ingredientName?.let {
            viewModel.getMealByIngredientName(it)
        }
        observerBottomIngredient()

        onClickItemRecyclerView()
    }

    private fun onClickItemRecyclerView() {
        bottomIngredientAdapter.onItemClick={
            val intent = Intent(activity, MealActivity::class.java)
            intent.apply {
                putExtra(HomeFragment.MEAL_ID,it.idMeal)
                putExtra(HomeFragment.MEAL_NAME,it.strMeal)
                putExtra(HomeFragment.MEAL_THUMB,it.strMealThumb)
            }
            startActivity(intent)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setDateView() {
        Glide.with(this).load("https://www.themealdb.com/images/ingredients/${ingredientName}.png").into(binding.imgBottomSheet)
        binding.tvIngredientName.text = ingredientName
        if(!ingredientDescription.equals("null")){
            binding.tvIngredientDescription.text = ingredientDescription
        }else {
            binding.tvIngredientDescription.text = "Don't have a description"
        }
    }

    private fun observerBottomIngredient() {
        viewModel.observerBottomSheetIngredient().observe(viewLifecycleOwner){
            bottomIngredientAdapter.setMealList(it)
        }
    }

    private fun prepareRecyclerView() {
        bottomIngredientAdapter = CategoryMealsAdapter()
        binding.rvBottomIngredient.apply {
            adapter = bottomIngredientAdapter
            layoutManager = GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
        }
    }
    companion object {
        @JvmStatic
        fun newInstance(param1: String,param2: String) =
            IngredientBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putString(INGREDIENT_NAME,param1)
                    putString(INGREDIENT_DESCRIPTION,param2)
                }
            }

    }

}