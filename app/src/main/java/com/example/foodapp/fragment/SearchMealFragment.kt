package com.example.foodapp.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.R
import com.example.foodapp.activity.MainActivity
import com.example.foodapp.activity.MealActivity
import com.example.foodapp.adapter.FavoriteMealAdapter
import com.example.foodapp.databinding.FragmentSearchMealBinding
import com.example.foodapp.viewmodel.HomeViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchMealFragment : Fragment() {
    private lateinit var binding : FragmentSearchMealBinding
    private lateinit var viewModel : HomeViewModel
    private lateinit var searchAdapter : FavoriteMealAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchMealBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareRecyclerView()
        observerSearchMealLiveData()
        // event click search
        onClickSearchMeal()
        actionSearch()
    }

    private fun actionSearch() {
        binding.btnEnter.setOnClickListener {
            searchMeal()
        }
        // automatic  search after text change 500ms
        var searchJob : Job? = null
        binding.edSearch.addTextChangedListener{ searchQuery->
            searchJob?.cancel()
            searchJob = lifecycleScope.launch{
                delay(500)
                viewModel.searchMeal(searchQuery.toString())
            }
        }
    }

    private fun onClickSearchMeal() {
        searchAdapter.setOnItemClickListener {
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(HomeFragment.MEAL_ID,it.idMeal)
            intent.putExtra(HomeFragment.MEAL_NAME,it.strMeal)
            intent.putExtra(HomeFragment.MEAL_THUMB,it.strMealThumb)
            intent.putExtra(HomeFragment.SAVE,"saved")
            startActivity(intent)
        }
    }

    private fun observerSearchMealLiveData() {
        viewModel.observerSearchMealLiveData().observe(viewLifecycleOwner, Observer {
            searchAdapter.differ.submitList(it)
        })
    }

    private fun searchMeal() {
        val searchQuery =binding.edSearch.text.toString().trim()
        if(searchQuery.isNotEmpty()){
            viewModel.searchMeal(searchQuery)
        }
    }

    private fun prepareRecyclerView() {
        searchAdapter = FavoriteMealAdapter()
        binding.rvSearch.apply {
            adapter = searchAdapter
            layoutManager = GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
        }
    }
}