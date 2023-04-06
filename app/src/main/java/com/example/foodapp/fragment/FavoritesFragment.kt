package com.example.foodapp.fragment

import android.content.ClipData
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.example.foodapp.R
import com.example.foodapp.activity.MainActivity
import com.example.foodapp.activity.MealActivity
import com.example.foodapp.adapter.FavoriteMealAdapter
import com.example.foodapp.databinding.FragmentFavoritesBinding
import com.example.foodapp.viewmodel.HomeViewModel
import com.google.android.material.snackbar.Snackbar

class FavoritesFragment : Fragment() {
    private lateinit var binding : FragmentFavoritesBinding
    private lateinit var viewModel : HomeViewModel
    private lateinit var favoriteAdapter : FavoriteMealAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(LayoutInflater.from(requireContext()),container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareRecyclerView()
        observeFavorites()
        onFavoriteClick()
        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = true
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                viewModel.deleteMeal(favoriteAdapter.differ.currentList[position])
                Snackbar.make(requireView(),"Meal deleted",Snackbar.LENGTH_LONG).show()
//                    .setAction("Undo", View.OnClickListener {
//                    viewModel.insertMeal(favoriteAdapter.differ.currentList[position])
//                }).show()
            }
        }
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.rvFavorites)
    }




    private fun onFavoriteClick() {
        favoriteAdapter.setOnItemClickListener {
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(HomeFragment.MEAL_ID,it.idMeal)
            intent.putExtra(HomeFragment.MEAL_NAME,it.strMeal)
            intent.putExtra(HomeFragment.MEAL_THUMB,it.strMealThumb)
            intent.putExtra(HomeFragment.SAVE,"saved")
            startActivity(intent)
        }
    }

    private fun prepareRecyclerView() {
        favoriteAdapter = FavoriteMealAdapter()
        binding.rvFavorites.apply {
            adapter = favoriteAdapter
            layoutManager = GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
        }
    }

    private fun observeFavorites() {
        viewModel.observeFavoritesMealsLiveData().observe(requireActivity(), Observer {mealList ->
                favoriteAdapter.differ.submitList(mealList)
        })
    }



}