package com.college.foodrecipe.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.college.foodrecipe.R
import com.college.foodrecipe.adapter.CookingTipsAdapter
import com.college.foodrecipe.adapter.IngredientsAdapter
import com.college.foodrecipe.databinding.ActivityRecipeDetailsBinding
import com.college.foodrecipe.util.Utils
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.squareup.picasso.Picasso
import kotlin.math.max
import kotlin.math.min

class RecipeDetailsActivity : AppCompatActivity() {

    lateinit var binding: ActivityRecipeDetailsBinding

    val interval: Long = 3000
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the data from the intent
        val recipeName = intent.getStringExtra("name")
        val recipeImageUrl = intent.getStringExtra("imageUrl")

        // Find the toolbar and set it as the action bar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // Set the toolbar title
        supportActionBar?.title =  recipeName


        // Setup RecyclerView
        setUpRecyclerView()

        // Load image using Glide
        Picasso.get().load(recipeImageUrl).error(R.drawable.res_image).into(binding.recipeImageView)

        binding.btnStartCooking.setOnClickListener {
            val intent = Intent()
            intent.putExtra("time", 10)
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    private fun setUpRecyclerView() {
        val ingredientsLayoutManager = FlexboxLayoutManager(binding.ingredientsRv.context)
        ingredientsLayoutManager.flexDirection = FlexDirection.ROW
        ingredientsLayoutManager.justifyContent = JustifyContent.FLEX_START
        val ingredientsAdapter = IngredientsAdapter(Utils.getRandomIngredients(Utils.createIngredientList(),14))
        binding.ingredientsRv.layoutManager = ingredientsLayoutManager
        binding.ingredientsRv.adapter = ingredientsAdapter


        val snapHelper: LinearSnapHelper = object : LinearSnapHelper() {
            override fun findTargetSnapPosition(
                layoutManager: RecyclerView.LayoutManager,
                velocityX: Int,
                velocityY: Int
            ): Int {
                val centerView = findSnapView(layoutManager) ?: return RecyclerView.NO_POSITION
                val position = layoutManager.getPosition(centerView)
                var targetPosition = -1
                if (layoutManager.canScrollHorizontally()) {
                    targetPosition = if (velocityX < 0) {
                        position - 1
                    } else {
                        position + 1
                    }
                }
                if (layoutManager.canScrollVertically()) {
                    targetPosition = if (velocityY < 0) {
                        position - 1
                    } else {
                        position + 1
                    }
                }
                val firstItem = 0
                val lastItem = layoutManager.itemCount - 1
                targetPosition = min(lastItem, max(targetPosition, firstItem))
                return targetPosition
            }
        }

        with(binding.cookingTipsRv) {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = CookingTipsAdapter(Utils.getCookingTips())
            setHasFixedSize(true)
            if (onFlingListener == null) {
                snapHelper.attachToRecyclerView(this)
            }
        }

        val linearSmoothScroller: LinearSmoothScroller = object : LinearSmoothScroller(binding.cookingTipsRv.context) {
            override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
                return 150f / displayMetrics.densityDpi
            }
        }

        handler = Handler(Looper.getMainLooper())
        runnable = Runnable {
            val layoutManager: LinearLayoutManager = binding.cookingTipsRv.layoutManager as LinearLayoutManager
            val totalElements = layoutManager.itemCount
            val currentPosition = layoutManager.findFirstCompletelyVisibleItemPosition()
            if (currentPosition == totalElements - 1) {
                binding.cookingTipsRv.smoothScrollToPosition(0)
            } else {
                linearSmoothScroller.targetPosition = currentPosition + 1
                layoutManager.startSmoothScroll(linearSmoothScroller)
            }
        }

        handler.postDelayed(runnable, interval)

        binding.cookingTipsRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                handler.removeCallbacks(runnable)
                handler.postDelayed(runnable, interval)
            }
        })

        with(binding.cookingTipsRv) {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = CookingTipsAdapter(Utils.getCookingTips())
            setHasFixedSize(true)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        // Handle the back button behavior
        onBackPressed()
        return true
    }


}