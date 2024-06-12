package com.college.foodrecipe.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.college.foodrecipe.R
import com.college.foodrecipe.databinding.ItemFeaturedRecipeBinding
import com.college.foodrecipe.util.Utils
import com.squareup.picasso.Picasso


class FeaturedRecipeAdapter(private val images: List<String>): RecyclerView.Adapter<FeaturedRecipeAdapter.FeaturedRecipeViewHolder>() {

    class FeaturedRecipeViewHolder(val binding: ItemFeaturedRecipeBinding): RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeaturedRecipeViewHolder {
        val view = ItemFeaturedRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        val context = parent.context
        val screenWidth = context.resources.displayMetrics.widthPixels
        val marginPixel = Utils.dpToPixel(40f, context).toInt()
        val cardWidth = (screenWidth - marginPixel)
        val ratio = (screenWidth - Utils.dpToPixel(60f, context)) / (2 * cardWidth)
        val height = (cardWidth * ratio)
        view.root.minimumHeight = height.toInt()
        val layoutParams = FrameLayout.LayoutParams(cardWidth, height.toInt())
        view.root.layoutParams = layoutParams
        return FeaturedRecipeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    override fun onBindViewHolder(holder: FeaturedRecipeViewHolder, position: Int) {
        val data = images[position]

        Picasso.get().load(data).error(R.drawable.res_image).into(holder.binding.img)


        val param = holder.itemView.layoutParams as ViewGroup.MarginLayoutParams
        val correctionPad = holder.itemView.context.resources.getDimensionPixelSize(R.dimen.adjust_top_margin)
        param.marginEnd = correctionPad
        param.bottomMargin = correctionPad
        param.topMargin = correctionPad
        if (position == 0) {
            param.marginStart = correctionPad
        }
        holder.itemView.layoutParams = param

    }
}