package com.college.foodrecipe.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.college.foodrecipe.R
import com.college.foodrecipe.adapter.CookingTipsAdapter
import com.college.foodrecipe.adapter.FeaturedRecipeAdapter
import com.college.foodrecipe.adapter.ImageSliderAdapter
import com.college.foodrecipe.databinding.FragmentFoodHomeBinding
import com.college.foodrecipe.util.Utils
import kotlin.math.max
import kotlin.math.min


class FoodHomeFragment : Fragment() {

    private lateinit var binding : FragmentFoodHomeBinding

    val interval: Long = 3000
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =FragmentFoodHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRv()
    }

    private fun initRv() {
        val list = ArrayList<String>()
        list.add("https://images.stockcake.com/public/9/7/9/979e5e90-521a-412a-a886-a3033fda5859_large/happy-rice-bear-stockcake.jpg")
        list.add("https://images.stockcake.com/public/0/3/7/037c4abc-765a-42d6-a84a-c29c79f66bfe_large/savory-dinner-plate-stockcake.jpg")
        list.add("https://images.stockcake.com/public/a/7/c/a7c8b609-2d85-45cc-922c-253882e48169_large/diverse-food-assortment-stockcake.jpg")
        list.add("https://images.stockcake.com/public/c/7/e/c7e7bc1b-6952-4ace-a3d8-04141f61a001_large/feast-for-eyes-stockcake.jpg")
        list.add("https://images.stockcake.com/public/7/8/5/78547743-d338-4e69-8b10-122535db1da3_large/savory-ratatouille-dish-stockcake.jpg")
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

        with(binding.sliderRv) {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = ImageSliderAdapter(list)
            setHasFixedSize(true)
            if (onFlingListener == null) {
                snapHelper.attachToRecyclerView(this)
            }
        }

        binding.recyclerViewIndicator.setRecyclerView(binding.sliderRv)
        binding.recyclerViewIndicator.forceUpdateItemCount()
        binding.recyclerViewIndicator.setCurrentPosition(0)

        val linearSmoothScroller: LinearSmoothScroller = object : LinearSmoothScroller(binding.sliderRv.context) {
            override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
                return 150f / displayMetrics.densityDpi
            }
        }

        handler = Handler(Looper.getMainLooper())
        runnable = Runnable {
            val layoutManager: LinearLayoutManager = binding.sliderRv.layoutManager as LinearLayoutManager
            val totalElements = layoutManager.itemCount
            val currentPosition = layoutManager.findFirstCompletelyVisibleItemPosition()
            if (currentPosition == totalElements - 1) {
                binding.sliderRv.smoothScrollToPosition(0)
            } else {
                linearSmoothScroller.targetPosition = currentPosition + 1
                layoutManager.startSmoothScroll(linearSmoothScroller)
            }
        }

        handler.postDelayed(runnable, interval)

        binding.sliderRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                handler.removeCallbacks(runnable)
                handler.postDelayed(runnable, interval)
            }
        })


        val listFeaturedRecipe = ArrayList<String>()
        listFeaturedRecipe.add("https://images.stockcake.com/public/0/3/9/039cad37-a746-45db-9107-80e230201a2d_large/delicious-pizza-feast-stockcake.jpg")
        listFeaturedRecipe.add("https://images.stockcake.com/public/1/7/8/1787865a-2547-407c-87f2-2e88fd0affa6_large/delicious-pizza-slice-stockcake.jpg")
        listFeaturedRecipe.add("https://images.stockcake.com/public/4/2/1/4211b17b-6886-4891-880b-9cec1d6221c5_large/ratatouille-vegetable-dish-stockcake.jpg")
        listFeaturedRecipe.add("https://images.stockcake.com/public/7/8/5/78547743-d338-4e69-8b10-122535db1da3_large/savory-ratatouille-dish-stockcake.jpg")

        with(binding.featureRecipeRv) {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = FeaturedRecipeAdapter(listFeaturedRecipe)
            setHasFixedSize(true)
        }


        with(binding.cookingTipsRv) {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = CookingTipsAdapter(Utils.getCookingTips())
            setHasFixedSize(true)
        }
    }

}