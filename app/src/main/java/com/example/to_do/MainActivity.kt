package com.example.to_do

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.to_do.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding  // Binding for the main activity layout
    private lateinit var taskPreferencesHelper: TaskPreferencesHelper  // Helper to manage task data
    private lateinit var tasksAdapter: TasksAdapter   // Adapter to display tasks in a RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the TaskPreferencesHelper to manage task storage
        taskPreferencesHelper = TaskPreferencesHelper(this)
        // Initialize the TasksAdapter with the list of all tasks from SharedPreferences
        tasksAdapter = TasksAdapter(taskPreferencesHelper.getAllTasks(), this)

        // Set up the RecyclerView with a LinearLayoutManager and attach the adapter
        binding.tasksRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.tasksRecyclerView.adapter = tasksAdapter

        binding.addButton.setOnClickListener {
            val intent = Intent(this, AddTask::class.java)
            startActivity(intent)
        }

        binding.timerButton.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }

        binding.alarmButton.setOnClickListener {
            val intent = Intent(this, MainActivity3::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        tasksAdapter.refreshData(taskPreferencesHelper.getAllTasks())
    }
}
