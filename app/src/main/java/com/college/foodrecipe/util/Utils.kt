package com.college.foodrecipe.util

import android.content.Context
import android.util.DisplayMetrics

object Utils {

    fun dpToPixel(dp: Float, context: Context): Float {
        return dp * context.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT
    }

    fun getCookingTips(): ArrayList<String> {
        val listCookingTips = ArrayList<String>()
        listCookingTips.add("1.Read Recipes Thoroughly: Before starting, read through the entire recipe to understand the steps and gather all the ingredients and tools you'll need.")
        listCookingTips.add("2.Prep Ingredients First: Measure and prepare all your ingredients before you start cooking. This is called \"mise en place\" and helps keep you organized.")
        listCookingTips.add("3.Sharp Knives are Safer: A sharp knife is safer than a dull one because it requires less force to cut through food, reducing the chance of slipping.")
        listCookingTips.add("4.Season as You Go: Season your food at different stages of cooking to build depth of flavor. Taste often to adjust seasoning.")
        listCookingTips.add("5.Rest Meat: After cooking meat, let it rest for a few minutes before slicing. This helps retain the juices, making it more flavorful and moist.")
        return listCookingTips
    }

    fun createIngredientList(): ArrayList<String> {
        val ingredients = arrayListOf(
            "Flour", "Sugar", "Butter", "Eggs", "Baking Powder", "Vanilla Extract",
            "Milk", "Salt", "Yeast", "Olive Oil", "Water", "Honey", "Cinnamon",
            "Nutmeg", "Chocolate Chips", "Brown Sugar", "Cream", "Yogurt",
            "Cheese", "Tomatoes", "Garlic", "Onions", "Lettuce", "Chicken",
            "Beef", "Pork", "Fish", "Carrots", "Potatoes", "Bell Peppers",
            "Broccoli", "Mushrooms", "Spinach", "Cucumber", "Avocado", "Lemon",
            "Lime", "Orange", "Banana", "Strawberries", "Blueberries", "Raspberries",
            "Blackberries", "Grapes", "Apples", "Pears", "Peaches", "Plums",
            "Cherries", "Pineapple"
        )

        return ingredients
    }

    fun getRandomIngredients(list: ArrayList<String>, count: Int): List<String> {
        // Shuffle the list and take the first 'count' items
        return list.shuffled().take(count)
    }
}