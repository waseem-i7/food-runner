package com.college.foodrecipe.fragment

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.SeekBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.college.foodrecipe.R
import com.college.foodrecipe.activity.DashboardActivity
import com.college.foodrecipe.databinding.FragmentTimerBinding
import kotlin.random.Random


class TimerFragment : Fragment() {

    private var timer: CountDownTimer? = null
    private var timeLeftInMillis: Long = 0
    private var isTimerRunning = false

    private var preparationTime: Long = 60000 // 1 minute
    private var cookingTime: Long = 120000 // 2 minutes
    private var restingTime: Long = 30000 // 30 seconds

    private var currentStage = Stage.PREPARATION

    enum class Stage {
        PREPARATION,
        COOKING,
        RESTING
    }

    lateinit var binding: FragmentTimerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTimerBinding.inflate(layoutInflater)

        if (arguments != null) {

            preparationTime = requireArguments().getInt("preparationTime").toLong()
            cookingTime = requireArguments().getInt("cookingTime").toLong()
            restingTime = requireArguments().getInt("restingTime").toLong()

            binding.mainLayout.visibility = View.VISIBLE
            binding.chooseRecipeButton.visibility = View.GONE

            binding.recipeNameTextView.text = "Example Recipe"
            updateTotalTimeText()
            resetTimer()

            binding.timerSeekBar.max = 2
            binding.progressBar.max = 100

            binding.timerSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    when (progress) {
                        0 -> {
                            currentStage = Stage.PREPARATION
                            timeLeftInMillis = preparationTime
                        }

                        1 -> {
                            currentStage = Stage.COOKING
                            timeLeftInMillis = cookingTime
                        }

                        2 -> {
                            currentStage = Stage.RESTING
                            timeLeftInMillis = restingTime
                        }
                    }
                    updateRemainingTimeText()
                    resetTimer()
                    binding.progressBar.progress = 0
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })

            binding.startButton.setOnClickListener {
                if (!isTimerRunning) {
                    startTimer()
                }
            }

            binding.pauseButton.setOnClickListener {
                if (isTimerRunning) {
                    pauseTimer()
                }
            }

            binding.resetButton.setOnClickListener {
                resetTimer()
            }
        }else{
            binding.chooseRecipeButton.visibility = View.VISIBLE
            binding.mainLayout.visibility = View.GONE
        }

        binding.chooseRecipeButton.setOnClickListener {
            activity?.let {
                val fragmentTransaction = it.supportFragmentManager.beginTransaction()
                val recipeFragment = RecipeFragment()
                fragmentTransaction.replace(R.id.frame, recipeFragment)
                fragmentTransaction.commit()
                (activity as? DashboardActivity)?.supportActionBar?.setTitle("Recipe")
                (activity as? DashboardActivity)?.setBottomNavigationItem(R.id.recipe)
            }
        }
        return binding.root
    }

    private fun startTimer() {
        timer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateRemainingTimeText()
                binding.progressBar.progress = ((timeLeftInMillis.toFloat() / getStageDuration(currentStage) * 100).toInt())
            }

            override fun onFinish() {
                resetTimer()
            }
        }.start()

        isTimerRunning = true
        updateButtons()
    }

    private fun pauseTimer() {
        timer?.cancel()
        isTimerRunning = false
        updateButtons()
    }

    private fun resetTimer() {
        timer?.cancel()
        timeLeftInMillis = getStageDuration(currentStage)
        updateRemainingTimeText()
        binding.progressBar.progress = 0
        isTimerRunning = false
        updateButtons()
    }

    private fun getStageDuration(stage: Stage): Long {
        return when (stage) {
            Stage.PREPARATION -> preparationTime
            Stage.COOKING -> cookingTime
            Stage.RESTING -> restingTime
        }
    }

    private fun updateRemainingTimeText() {
        binding.remainingTimeTextView.text = "Remaining Time: ${formatTime(timeLeftInMillis)}"
    }

    private fun updateTotalTimeText() {
        val totalTime = preparationTime + cookingTime + restingTime
        binding.totalTimeTextView.text = "Total Time: ${formatTime(totalTime)}"
    }

    private fun updateButtons() {
        if (isTimerRunning) {
            binding.startButton.visibility = View.GONE
            binding.pauseButton.visibility = View.VISIBLE
        } else {
            binding.startButton.visibility = View.VISIBLE
            binding.pauseButton.visibility = View.GONE
        }
    }

    private fun formatTime(millis: Long): String {
        val minutes = (millis / 1000) / 60
        val seconds = (millis / 1000) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }
}

