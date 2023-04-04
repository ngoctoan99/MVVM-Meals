package com.example.foodapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.foodapp.R
import com.example.foodapp.database.MealDatabase
import com.example.foodapp.databinding.ActivityMainBinding
import com.example.foodapp.viewmodel.HomeViewModel
import com.example.foodapp.viewmodel.factory.HomeViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
     val viewModel : HomeViewModel by lazy {
        val mealDatabase = MealDatabase.getInstance(this)
        val homeViewModelProviderFactory = HomeViewModelFactory(mealDatabase)
        ViewModelProvider(this,homeViewModelProviderFactory)[HomeViewModel::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment= supportFragmentManager.findFragmentById(R.id.navHost) as NavHostFragment
        val navController= navHostFragment.navController
        binding.botomNav.setupWithNavController(navController)
    }
}