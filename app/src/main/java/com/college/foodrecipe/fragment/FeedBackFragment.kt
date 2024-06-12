package com.college.foodrecipe.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.college.foodrecipe.R
import com.college.foodrecipe.databinding.FragmentFeedBackBinding

class FeedBackFragment : Fragment() {

    lateinit var binding: FragmentFeedBackBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFeedBackBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSubmit.setOnClickListener {
            val rating = binding.ratingBar.rating
            val review = binding.etReview.text.toString()
            val shareFeedback = binding.cbShareFeedback.isChecked

            // Handle the feedback submission
            submitFeedback(rating, review, shareFeedback)
        }
    }

    private fun submitFeedback(rating: Float, review: String, shareFeedback: Boolean) {
        // Here you can add the code to handle the feedback submission,
        // like saving it to a database or sending it to a server.
        // For demonstration, we'll just show a toast.

        val feedbackMessage = StringBuilder()
        feedbackMessage.append("Rating: $rating ")
        feedbackMessage.append("Review: $review ")
        feedbackMessage.append("Share with community: $shareFeedback")

        Toast.makeText(context, feedbackMessage.toString(), Toast.LENGTH_LONG).show()
        // Clear the EditText and checkboxes
        binding.etReview.setText("")
        binding.cbShareFeedback.isChecked = false
        binding.ratingBar.rating = 0f
    }
}
