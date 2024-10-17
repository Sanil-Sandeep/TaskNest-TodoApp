package com.example.to_do

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.to_do.databinding.ActivityAddTaskBinding
import java.util.Calendar

// AddTask activity handles adding new tasks, including picking a date and time
class AddTask : AppCompatActivity(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    // Variables to hold the selected date and time
    var day = 0
    var month = 0
    var year = 0
    var hour = 0
    var minute = 0

    // Variables to save the selected date and time
    var saveDay = 0
    var saveMonth = 0
    var saveYear = 0
    var saveHour = 0
    var saveMinute = 0

    private lateinit var binding: ActivityAddTaskBinding
    private lateinit var taskPreferencesHelper: TaskPreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the date picker function
        pickDate()

        // Initialize the helper class for managing task preferences (like inserting tasks)
        taskPreferencesHelper = TaskPreferencesHelper(this)

        binding.saveButton.setOnClickListener {
            val title = binding.titleEditText.text.toString()
            val content = binding.contentEditText.text.toString()
            val priority = binding.deadlineEditText.text.toString()
            val dateTime = "$saveDay-$saveMonth-$saveYear $saveHour:$saveMinute"
            val task = Task(System.currentTimeMillis().toInt(), title, content, priority, dateTime) // Unique task ID
            taskPreferencesHelper.insertTask(task)
            finish()
            Toast.makeText(this, "Task saved", Toast.LENGTH_SHORT).show()
        }
    }

    // Function to get the current date and time from the system calendar
    private fun getDateTimeCalendar() {
        val cal: Calendar = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)   // Current day
        month = cal.get(Calendar.MONTH)    // Current month (0-based, so January is 0)
        year = cal.get(Calendar.YEAR)   // Current year
        hour = cal.get(Calendar.HOUR)   // Current hour (12-hour format)
        minute = cal.get(Calendar.MINUTE)    // Current minute
    }

    // Function to open the DatePicker dialog when the time picker button is clicked
    private fun pickDate() {
        binding.btnTimePicker.setOnClickListener {
            getDateTimeCalendar()
            DatePickerDialog(this, this, year, month, day).show()
        }
    }

    // Callback function that gets called when a date is set in the DatePicker
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        saveDay = dayOfMonth
        saveMonth = month + 1
        saveYear = year

        getDateTimeCalendar()
        TimePickerDialog(this, this, hour, minute, true).show()
    }

    // Callback function that gets called when a time is set in the TimePicker
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        saveHour = hourOfDay
        saveMinute = minute

        // Display the selected date and time in the TextView
        binding.tvTextTime.text = "$saveDay-$saveMonth-$saveYear\n Hour:$saveHour Minute:$saveMinute"
    }
}
