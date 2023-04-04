package com.example.foodapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodapp.databinding.MealItemBinding
import com.example.foodapp.model.Meal
import com.example.foodapp.model.MealByCategory

class FavoriteMealAdapter: RecyclerView.Adapter<FavoriteMealAdapter.FavoriteMealViewHolder>() {
    class FavoriteMealViewHolder( val binding:MealItemBinding):RecyclerView.ViewHolder(binding.root)
    private val diffUtil = object : DiffUtil.ItemCallback<Meal>(){
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return  oldItem.idMeal == newItem.idMeal
        }

        override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this , diffUtil)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteMealViewHolder {
       return FavoriteMealViewHolder(MealItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: FavoriteMealViewHolder, position: Int) {
        val meal =  differ.currentList[position]

        holder.itemView.apply {
            Glide.with(holder.itemView).load(meal.strMealThumb).into(holder.binding.imgMeal)
            holder.binding.tvMealName.text = meal.strMeal
            setOnClickListener {
                onItemClickListener?.let {
                    it(meal)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Meal) -> Unit) ? = null
    fun setOnItemClickListener(listener : (Meal) -> Unit){
        onItemClickListener = listener
    }
}