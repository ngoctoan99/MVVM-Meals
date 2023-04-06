package com.example.foodapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodapp.databinding.IngredientItemBinding
import com.example.foodapp.model.Ingredient
import com.example.foodapp.model.MealByCategory

class IngredientAdapter :RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder>(){
    private var ingredientList = ArrayList<Ingredient>()
    lateinit var onItemClick:((Ingredient)->Unit)

    @SuppressLint("NotifyDataSetChanged")
    fun setIngredientList(ingredientList: List<Ingredient>){
        this.ingredientList = ingredientList as ArrayList<Ingredient>
        notifyDataSetChanged()
    }
    class IngredientViewHolder(val binding : IngredientItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        return IngredientViewHolder(IngredientItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        Glide.with(holder.itemView).load("https://www.themealdb.com/images/ingredients/${ingredientList[position].strIngredient}.png").into(holder.binding.imgMeal)
        holder.binding.tvIngredientName.text = ingredientList[position].strIngredient
        holder.itemView.setOnClickListener{
            onItemClick.invoke(ingredientList[position])
        }
    }
    override fun getItemCount(): Int {
        return ingredientList.size
    }
}