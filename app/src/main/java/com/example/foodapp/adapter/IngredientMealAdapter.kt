package com.example.foodapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodapp.databinding.MealIngredientItemBinding

import com.example.foodapp.model.MealByCategory

class IngredientMealAdapter : RecyclerView.Adapter<IngredientMealAdapter.IngredientMealViewHolder>(){

    private var mealList = ArrayList<MealByCategory>()
    lateinit var onItemClick:((MealByCategory)->Unit)
    @SuppressLint("NotifyDataSetChanged")

    fun setMealList(mealList: List<MealByCategory>){
        this.mealList = mealList as ArrayList<MealByCategory>
        notifyDataSetChanged()
    }
    class IngredientMealViewHolder (val binding : MealIngredientItemBinding): RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientMealViewHolder {
        return IngredientMealViewHolder(MealIngredientItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: IngredientMealViewHolder, position: Int) {
        Glide.with(holder.itemView).load(mealList[position].strMealThumb).into(holder.binding.imgMeal)
        holder.binding.tvMealName.text = mealList[position].strMeal
        holder.itemView.setOnClickListener{
            onItemClick.invoke(mealList[position])
        }
    }

    override fun getItemCount(): Int {
        return mealList.size
    }
}