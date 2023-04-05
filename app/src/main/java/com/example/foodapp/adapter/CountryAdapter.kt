package com.example.foodapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodapp.databinding.CountryItemBinding

import com.example.foodapp.model.Category
import com.example.foodapp.model.Country

class CountryAdapter(): RecyclerView.Adapter<CountryAdapter.CountryViewHolder>() {
    private var countryList = ArrayList<Country>()
    var onItemClick : ((Country) -> Unit)? = null
    inner class CountryViewHolder( var binding : CountryItemBinding): RecyclerView.ViewHolder(binding.root)

    @SuppressLint("NotifyDataSetChanged")
    fun setCountryList(countryList: List<Country>){
        this.countryList = countryList as ArrayList<Country>
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        return CountryViewHolder(CountryItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.binding.tvCountryName.text = countryList[position].strArea
        holder.itemView.setOnClickListener {
            onItemClick!!.invoke(countryList[position])
        }
    }
    override fun getItemCount(): Int {
        return countryList.size
    }
}