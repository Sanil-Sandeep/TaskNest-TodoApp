package com.example.to_do

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.to_do.databinding.ActivityMain2Binding
import com.example.to_do.databinding.ActivityMainBinding
import java.util.Date
import java.util.Timer
import java.util.TimerTask

class MainActivity2 : AppCompatActivity() {

    lateinit var binding: ActivityMain2Binding  // Binding for the activity's layout
    lateinit var dataHelper: DataHelper // Helper class to manage timer data
    private val timer = Timer()  // Timer instance for scheduling timer updates

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        dataHelper = DataHelper((applicationContext))

        binding.startButton.setOnClickListener{statStopAction()}
        binding.resetButton.setOnClickListener{resetStopAction()}

        // Check if the timer is currently counting
        if (dataHelper.timerCounting()){
            startTimer()
        }
        else{
            stopTimer()
            // If start and stop times are available, calculate and display elapsed time
            if (dataHelper.startTime() != null && dataHelper.stopTime() != null){
                val time = Date().time - calcReStartTime().time
                binding.timeTV.text = timeStringFromLong(time)
            }
        }

        timer.scheduleAtFixedRate(TimeTask(), 0, 500)



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    /**
     * Inner class for handling timer updates at regular intervals.
     */
    private inner class TimeTask : TimerTask() {
        override fun run() {
            if (dataHelper.timerCounting()) {
                val time = Date().time - dataHelper.startTime()!!.time
                runOnUiThread {
                    binding.timeTV.text = timeStringFromLong(time)  // This updates the UI safely on the main thread
                }
            }
        }
    }


    /**
     * Resets the timer and updates the UI accordingly.
     */
    private fun resetStopAction() {
        dataHelper.setStoptime(null)
        dataHelper.setStartTime(null)
        stopTimer()
        binding.timeTV.text = timeStringFromLong(0)
    }

    /**
     * Stops the timer and updates the button text to reflect the current state.
     */
    private fun stopTimer() {
        dataHelper.setTimerCounting(false)
        binding.startButton.text = getString(R.string.start)
    }

    /**
     * Starts the timer and updates the button text to reflect the current state.
     */
    private fun startTimer() {
        dataHelper.setTimerCounting(true)
        binding.startButton.text = getString(R.string.stop)
    }

    /**
     * Handles the start/stop action based on the current timer state.
     */
    private fun statStopAction() {
        if(dataHelper.timerCounting()){
            dataHelper.setStoptime(Date())
            stopTimer()
        }
        else{
            if (dataHelper.stopTime() != null){
                dataHelper.setStartTime(calcReStartTime())
                dataHelper.setStoptime(null)
            }
            else{
                dataHelper.setStartTime(Date())
            }
            startTimer()
        }
    }

    /**
     * Calculates the restart time based on previous start and stop times.
     */
    private fun calcReStartTime(): Date {
        val diff = (dataHelper.startTime()?.time ?: 0L) - (dataHelper.stopTime()?.time ?: 0L)
        return Date(System.currentTimeMillis() + diff)
    }

    /**
     * Converts milliseconds to a formatted time string (HH:mm:ss).
     */
    private fun timeStringFromLong(ms: Long): String? {
        val seconds = (ms / 1000) % 60
        val minutes = (ms / (1000 * 60) % 60)
        val hours = (ms / (1000 * 60 * 60)) % 24

        return makeTimeString(hours, minutes, seconds)
    }

    /**
     * Formats the hours, minutes, and seconds into a string.
     */
    private fun makeTimeString(hours: Long, minutes: Long, seconds: Long): String? {

        return String.format("%02d:%02d:%02d", hours, minutes, seconds)

    }
}