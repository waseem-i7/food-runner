package com.college.foodrecipe.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.college.foodrecipe.R
import com.college.foodrecipe.databinding.ItemCookingTipsBinding
import com.college.foodrecipe.databinding.ItemImageSliderBinding
import com.college.foodrecipe.util.Utils


class CookingTipsAdapter(private val list: List<String>): RecyclerView.Adapter<CookingTipsAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemCookingTipsBinding): RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemCookingTipsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val context = parent.context
        val screenWidth = context.resources.displayMetrics.widthPixels
        val marginPixel = Utils.dpToPixel(40f, context).toInt()
        val cardWidth = (screenWidth - marginPixel)
        val ratio = (screenWidth - Utils.dpToPixel(60f, context)) / (2 * cardWidth)
        val height = (cardWidth * ratio-60)
        view.root.minimumHeight = height.toInt()
        val layoutParams = FrameLayout.LayoutParams(cardWidth, height.toInt())
        view.root.layoutParams = layoutParams
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]

        holder.binding.textViewCoolingTips.text = data

        val param = holder.itemView.layoutParams as ViewGroup.MarginLayoutParams
        val correctionPad = holder.itemView.context.resources.getDimensionPixelSize(R.dimen.adjust_top_margin)
        param.marginEnd = correctionPad
        param.bottomMargin = holder.itemView.context.resources.getDimensionPixelSize(R.dimen.adjust_bottom_margin)
        param.topMargin = correctionPad
        if (position == 0) {
            param.marginStart = correctionPad
        }
        holder.itemView.layoutParams = param

    }
}