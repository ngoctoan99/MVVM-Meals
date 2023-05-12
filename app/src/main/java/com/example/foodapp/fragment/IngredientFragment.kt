package com.example.foodapp.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodapp.R
import com.example.foodapp.activity.MainActivity
import com.example.foodapp.adapter.IngredientAdapter
import com.example.foodapp.databinding.FragmentIngredientBinding
import com.example.foodapp.databinding.IngredientItemBinding
import com.example.foodapp.fragment.bottomDialog.IngredientBottomSheetFragment
import com.example.foodapp.viewmodel.HomeViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class IngredientFragment : Fragment() {

    private lateinit var binding: FragmentIngredientBinding
    private lateinit var viewModel : HomeViewModel
    private lateinit var ingredientAdapter : IngredientAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentIngredientBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvIngredient.visibility = View.GONE
        binding.isLoading.visibility = View.VISIBLE
        prepareRecyclerview()
        viewModel.getAllIngredient()
        observerIngredient()
        onClickIngredient()
        actionSearchIngredient()
    }

    private fun observerSearchIngredient() {
        binding.rvIngredient.visibility = View.GONE
        binding.isLoading.visibility = View.VISIBLE
        viewModel.observerSearchIngredientLiveData().observe(viewLifecycleOwner){
            ingredientAdapter.setIngredientList(it)
            binding.rvIngredient.visibility = View.VISIBLE
            binding.isLoading.visibility = View.GONE

        }
    }

    private fun actionSearchIngredient() {
        binding.btnEnter.setOnClickListener {
            val searchQuery = binding.edSearch.text.toString().trim()
            if(searchQuery.isNotEmpty()){
                viewModel.searchIngredient(searchQuery)
            }
            observerSearchIngredient()
        }
        // automatic search
        var searchJob : Job? = null
        // automatic search text change after 500 ms
        binding.edSearch.addTextChangedListener{ searchQuery->
            searchJob?.cancel()
            searchJob = lifecycleScope.launch{
                delay(500)
                viewModel.searchIngredient(searchQuery.toString())
                observerSearchIngredient()
            }
        }
    }

    private fun onClickIngredient() {
        ingredientAdapter.onItemClick ={
            val ingredientBottomSheetFragment = IngredientBottomSheetFragment.newInstance(it.strIngredient+"",it.strDescription+"")
            ingredientBottomSheetFragment.show(childFragmentManager,"Ingredient Info")
        }
    }
    private fun observerIngredient() {
        viewModel.observeIngredientLiveData().observe(viewLifecycleOwner){
            ingredientAdapter.setIngredientList(it)
            binding.rvIngredient.visibility = View.VISIBLE
            binding.isLoading.visibility = View.GONE
        }
    }

    private fun prepareRecyclerview() {
        ingredientAdapter = IngredientAdapter()
        binding.rvIngredient.apply {
            adapter = ingredientAdapter
            layoutManager = GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
        }
    }
}