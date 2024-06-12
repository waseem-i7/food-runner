package com.college.foodrecipe.fragment


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.college.foodrecipe.R
import com.college.foodrecipe.activity.DashboardActivity
import com.college.foodrecipe.activity.RecipeDetailsActivity
import com.college.foodrecipe.adapter.AllRestaurantsAdapter
import com.college.foodrecipe.model.Restaurants
import com.college.foodrecipe.util.ConnectionManager
import com.college.foodrecipe.util.DrawerLocker
import com.college.foodrecipe.util.FETCH_RESTAURANTS
import com.college.foodrecipe.util.Sorter
import org.json.JSONException
import org.json.JSONObject
import java.util.Collections
import kotlin.random.Random

/**
 * A simple [Fragment] subclass.
 */
class RecipeFragment : Fragment() {

    private lateinit var recyclerRestaurant: RecyclerView
    private lateinit var allRestaurantsAdapter: AllRestaurantsAdapter
    private var restaurantList = arrayListOf<Restaurants>()
    private lateinit var progressBar: ProgressBar
    private lateinit var rlLoading: RelativeLayout
    private var checkedItem: Int = -1

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data?.getIntExtra("time",0)
            data?.let {
                activity?.let { activity->
                    val fiveMinutesInMillis = 5 * 60 * 1000
                    val fragmentTransaction = activity.supportFragmentManager.beginTransaction()
                    val timerFragment = TimerFragment()
                    val bundle = Bundle()
                    bundle.putInt("preparationTime",Random.nextInt(fiveMinutesInMillis + 1))
                    bundle.putInt("cookingTime",Random.nextInt(fiveMinutesInMillis + 1))
                    bundle.putInt("restingTime",Random.nextInt(fiveMinutesInMillis + 1))
                    timerFragment.arguments = bundle

                    fragmentTransaction.replace(R.id.frame, timerFragment)
                    fragmentTransaction.commit()
                    (activity as? DashboardActivity)?.supportActionBar?.setTitle("Timer")
                    (activity as? DashboardActivity)?.setBottomNavigationItem(R.id.timer)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_recipe, container, false)
        (activity as DrawerLocker).setDrawerEnabled(true)
        progressBar = view?.findViewById(R.id.progressBar) as ProgressBar
        rlLoading = view.findViewById(R.id.rlLoading) as RelativeLayout
        rlLoading.visibility = View.VISIBLE

        /*A separate method for setting up our recycler view*/
        setUpRecycler(view)

        setHasOptionsMenu(true)

        return view
    }

    private fun setUpRecycler(view: View) {
        recyclerRestaurant = view.findViewById(R.id.recyclerRestaurants) as RecyclerView

        /*Create a queue for sending the request*/
        val queue = Volley.newRequestQueue(activity as Context)


        /*Check if the internet is present or not*/
        if (ConnectionManager().isNetworkAvailable(activity as Context)) {

            /*Create a JSON object request*/
            val jsonObjectRequest = object : JsonObjectRequest(
                Request.Method.GET,
                FETCH_RESTAURANTS,
                null,
                Response.Listener<JSONObject> { response ->
                    rlLoading.visibility = View.GONE

                    /*Once response is obtained, parse the JSON accordingly*/
                    try {
                        val data = response.getJSONObject("data")
                        val success = data.getBoolean("success")
                        if (success) {

                            val resArray = data.getJSONArray("data")
                            for (i in 0 until resArray.length()) {
                                val resObject = resArray.getJSONObject(i)
                                val restaurant = Restaurants(
                                    resObject.getString("id").toInt(),
                                    resObject.getString("name"),
                                    resObject.getString("rating"),
                                    resObject.getString("cost_for_one").toInt(),
                                    resObject.getString("image_url"),
                                )
                                restaurantList.add(restaurant)
                                if (activity != null) {
                                    allRestaurantsAdapter = AllRestaurantsAdapter(restaurantList, activity as Context){restaurant->
                                        activity?.let {
                                            val intent = Intent(it, RecipeDetailsActivity::class.java)
                                            intent.putExtra("id", restaurant.id)
                                            intent.putExtra("name", restaurant.name)
                                            intent.putExtra("rating", restaurant.rating)
                                            intent.putExtra("imageUrl", restaurant.imageUrl)
                                            resultLauncher.launch(intent)
                                        }
                                    }
                                    val mLayoutManager = LinearLayoutManager(activity)
                                    recyclerRestaurant.layoutManager = mLayoutManager
                                    recyclerRestaurant.itemAnimator = DefaultItemAnimator()
                                    recyclerRestaurant.adapter = allRestaurantsAdapter
                                    recyclerRestaurant.setHasFixedSize(true)
                                }

                            }
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { error: VolleyError? ->
                    Toast.makeText(activity as Context, error?.message, Toast.LENGTH_SHORT).show()
                }) {

                /*Send the headers using the below method*/
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-type"] = "application/json"

                    /*The below used token will not work, kindly use the token provided to you in the training*/
                    headers["token"] = "9bf534118365f1"
                    return headers
                }
            }

            queue.add(jsonObjectRequest)

        } else {
            val builder = AlertDialog.Builder(activity as Context)
            builder.setTitle("Error")
            builder.setMessage("No Internet Connection found. Please connect to the internet and re-open the app.")
            builder.setCancelable(false)
            builder.setPositiveButton("Ok") { _, _ ->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            builder.create()
            builder.show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.dashboard_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.action_sort -> showDialog(context as Context)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showDialog(context: Context) {

        val builder: AlertDialog.Builder? = AlertDialog.Builder(context)
        builder?.setTitle("Sort By?")
        builder?.setSingleChoiceItems(R.array.filters, checkedItem) { _, isChecked ->
            checkedItem = isChecked
        }
        builder?.setPositiveButton("Ok") { _, _ ->

            when (checkedItem) {
                0 -> {
                    Collections.sort(restaurantList, Sorter.ratingComparator)
                    restaurantList.reverse()
                }
            }
            allRestaurantsAdapter.notifyDataSetChanged()
        }
        builder?.setNegativeButton("Cancel") { _, _ ->

        }
        builder?.create()
        builder?.show()
    }

}
