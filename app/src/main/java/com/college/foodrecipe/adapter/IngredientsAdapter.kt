package com.college.foodrecipe.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.college.foodrecipe.databinding.ItemIngredientBinding


class IngredientsAdapter(private val dataList: List<String>) :
    RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientsViewHolder {
        val binding = ItemIngredientBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return IngredientsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IngredientsViewHolder, position: Int) {
        holder.textView.text = dataList[position]
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class IngredientsViewHolder(binding: ItemIngredientBinding) : RecyclerView.ViewHolder(binding.root) {
        val textView = binding.text
    }
}