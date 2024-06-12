package com.college.foodrecipe.activity

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentActivity
import com.college.foodrecipe.R

class ProfileActivity : AppCompatActivity() {


    private lateinit var txtUserName: TextView
    private lateinit var txtPhone: TextView
    private lateinit var txtAddress: TextView
    private lateinit var txtEmail: TextView
    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        sharedPrefs = getSharedPreferences("FoodApp", Context.MODE_PRIVATE)
        txtUserName = findViewById(R.id.txtUserName)
        txtPhone =  findViewById(R.id.txtPhone)
        txtEmail = findViewById(R.id.txtEmail)
        txtAddress = findViewById(R.id.txtAddress)
        txtUserName.text = sharedPrefs.getString("user_name", null)
        val phoneText = "+91-${sharedPrefs.getString("user_mobile_number", null)}"
        txtPhone.text = phoneText
        txtEmail.text = sharedPrefs.getString("user_email", null)
        val address = sharedPrefs.getString("user_address", null)
        txtAddress.text = address

        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            finish()
        }
    }
}