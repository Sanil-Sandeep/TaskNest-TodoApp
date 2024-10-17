package com.example.to_do

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.to_do.databinding.ActivityUpdateBinding

/**
 * Activity for updating an existing task.
 */
class UpdateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateBinding
    private lateinit var db: TaskPreferencesHelper
    private var taskId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = TaskPreferencesHelper(this)

        // Retrieve the task ID from the intent extras
        taskId = intent.getIntExtra("task_id", -1)
        if (taskId == -1) {
            Toast.makeText(this, "Invalid Task ID", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Get the task details from the database
        val task = db.getTaskByID(taskId)
        if (task == null) {
            Toast.makeText(this, "Task not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Assuming priority is the correct property name
        binding.updateTitleEditText.setText(task.title)
        binding.updateContentEditText.setText(task.content)
        binding.updatedeadlineEditText.setText(task.priority)
        binding.updatetvtextTime.setText(task.dateTime)

        binding.updateSaveButton.setOnClickListener {
            val newTitle = binding.updateTitleEditText.text.toString()
            val newContent = binding.updateContentEditText.text.toString()
            val newPriority = binding.updatedeadlineEditText.text.toString()
            val newdateTime = binding.updatetvtextTime.text.toString()

            // Check if fields are not empty
            if (newTitle.isEmpty() || newContent.isEmpty() || newPriority.isEmpty() || newdateTime.isEmpty()) {
                Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val updatedTask = Task(taskId, newTitle, newContent, newPriority, newdateTime)
            db.updateTask(updatedTask)
            Toast.makeText(this, "Changes Saved", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}