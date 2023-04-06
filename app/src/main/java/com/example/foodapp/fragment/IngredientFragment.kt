package com.example.foodapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodapp.R
import com.example.foodapp.activity.MainActivity
import com.example.foodapp.adapter.IngredientAdapter
import com.example.foodapp.databinding.FragmentIngredientBinding
import com.example.foodapp.databinding.IngredientItemBinding
import com.example.foodapp.fragment.bottomDialog.IngredientBottomSheetFragment
import com.example.foodapp.viewmodel.HomeViewModel


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
        prepareRecyclerview()
        viewModel.getAllIngredient()
        observerIngredient()
        onClickIngredient()
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