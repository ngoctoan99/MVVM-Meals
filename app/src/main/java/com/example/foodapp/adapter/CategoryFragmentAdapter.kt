package com.example.foodapp.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodapp.R
import com.example.foodapp.databinding.RowCategoryItemBinding
import com.example.foodapp.model.Category

class CategoryFragmentAdapter(): RecyclerView.Adapter<CategoryFragmentAdapter.CategoryFragmentViewHolder>() {
    private var categoryList = ArrayList<Category>()
    var onItemClick : ((Category) -> Unit)? = null
    inner class CategoryFragmentViewHolder( var binding : RowCategoryItemBinding): RecyclerView.ViewHolder(binding.root)
    @SuppressLint("NotifyDataSetChanged")
    fun setCategoryList(categoryList: List<Category>){
        this.categoryList = categoryList as ArrayList<Category>
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryFragmentViewHolder {
        return CategoryFragmentViewHolder(RowCategoryItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: CategoryFragmentViewHolder, position: Int) {
        Glide.with(holder.itemView).load(categoryList[position].strCategoryThumb).into(holder.binding.imgCategory)
        holder.binding.tvCategoryName.text = categoryList[position].strCategory
        holder.itemView.setOnClickListener {
            onItemClick!!.invoke(categoryList[position])
            setFalseCheck()
            categoryList[position].isCheck = true
            notifyDataSetChanged()
        }
        setUIClick(categoryList[position],holder)
    }

    private fun setFalseCheck(){
        for ( i in 0 until categoryList.size){
            categoryList[i].isCheck = false
        }
    }
    private fun setUIClick(category: Category, holder: CategoryFragmentViewHolder) {
        if(category.isCheck){
            holder.binding.cardView.setCardBackgroundColor(Color.parseColor("#f52c56"))
            holder.binding.tvCategoryName.setTextColor(Color.parseColor("#f52c56"))
        }else {
            holder.binding.cardView.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            holder.binding.tvCategoryName.setTextColor(Color.parseColor("#FF000000"))
        }
    }
    override fun getItemCount(): Int {
        return categoryList.size
    }
}