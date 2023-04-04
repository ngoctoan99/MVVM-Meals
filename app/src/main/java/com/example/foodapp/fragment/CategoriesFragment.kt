package com.example.foodapp.fragment

import android.content.Intent
import android.os.Binder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodapp.R
import com.example.foodapp.activity.CategoryMealActivity
import com.example.foodapp.activity.MainActivity
import com.example.foodapp.adapter.CategoryAdapter
import com.example.foodapp.adapter.FavoriteMealAdapter
import com.example.foodapp.databinding.FragmentCategoriesBinding
import com.example.foodapp.viewmodel.HomeViewModel


class CategoriesFragment : Fragment() {
    private lateinit var binding : FragmentCategoriesBinding
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var viewModel : HomeViewModel
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
        onPrepareRecyclerView()
        observeCategory()
        onClickCategory()
    }

    private fun onClickCategory() {
       categoryAdapter.onItemClick = {
           val intent = Intent(activity, CategoryMealActivity::class.java)
           intent.putExtra(HomeFragment.CATEGORY_NAME,it.strCategory)
           startActivity(intent)
       }
    }

    private fun observeCategory() {
        viewModel.observerCategoryLIveData().observe(viewLifecycleOwner){
            categoryAdapter.setCategoryList(it)
        }
    }

    private fun onPrepareRecyclerView() {
        categoryAdapter = CategoryAdapter()
        binding.rvCategories.apply {
            adapter = categoryAdapter
            layoutManager = GridLayoutManager(context, 3,GridLayoutManager.VERTICAL,false)
        }
    }
}