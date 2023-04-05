package com.example.foodapp.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.R
import com.example.foodapp.activity.CountryMealActivity
import com.example.foodapp.activity.MainActivity
import com.example.foodapp.adapter.CountryAdapter
import com.example.foodapp.databinding.FragmentListNationBinding
import com.example.foodapp.viewmodel.HomeViewModel


class ListCountryFragment : Fragment() {
    private lateinit var binding : FragmentListNationBinding
    private lateinit var countryAdapter : CountryAdapter
    private lateinit var viewModel : HomeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListNationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getCountry()
        prepareRecyclerView()
        observerCountryLiveData()
        onClickCountry()
    }

    private fun onClickCountry() {
        countryAdapter.onItemClick = {
           val intent = Intent(activity,CountryMealActivity::class.java)
            intent.putExtra(HomeFragment.COUNTRY_NAME,it.strArea)
            startActivity(intent)
        }
    }

    private fun observerCountryLiveData() {
        viewModel.observerCountryLiveData().observe(viewLifecycleOwner){
            countryAdapter.setCountryList(it)
        }
    }

    private fun prepareRecyclerView() {
        countryAdapter = CountryAdapter()
        binding.rvNation.apply {
            adapter = countryAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }
}