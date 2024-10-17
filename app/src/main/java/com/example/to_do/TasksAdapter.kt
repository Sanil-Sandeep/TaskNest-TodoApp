package com.example.to_do

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

/**
 * Adapter class for managing a list of tasks in a RecyclerView.
 */
class TasksAdapter(private var tasks: List<Task>, private val context: Context) :
    RecyclerView.Adapter<TasksAdapter.TaskViewHolder>() {

    private val taskPreferencesHelper = TaskPreferencesHelper(context)  // Helper to manage task preferences

    /**
     * ViewHolder class that holds the views for each task item.
     */
    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)  // Title TextView
        val contentTextView: TextView = itemView.findViewById(R.id.contentTextView)  // Content TextView
        val priorityTextView: TextView = itemView.findViewById(R.id.priorityTextView)  // Priority TextView
        val dateTimeTextView: TextView = itemView.findViewById(R.id.dateTimeTextView)  // Date/Time TextView
        val updateButton: ImageView = itemView.findViewById(R.id.updateButton)    // Update button
        val deleteButton: ImageView = itemView.findViewById(R.id.deleteButton)  // Delete button
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return TaskViewHolder(view)
    }

    /**
     * Called to bind data to the views in the ViewHolder.
     */
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.titleTextView.text = task.title
        holder.contentTextView.text = task.content
        holder.priorityTextView.text = task.priority
        holder.dateTimeTextView.text = task.dateTime

        holder.updateButton.setOnClickListener {
            val intent = Intent(holder.itemView.context, UpdateActivity::class.java).apply {
                putExtra("task_id", task.id)
            }
            holder.itemView.context.startActivity(intent)
        }

        holder.deleteButton.setOnClickListener {
            taskPreferencesHelper.deleteTask(task.id)
            refreshData(taskPreferencesHelper.getAllTasks())
            Toast.makeText(holder.itemView.context, "Task Deleted", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Returns the total number of tasks.
     */
    override fun getItemCount(): Int = tasks.size

    /**
     * Updates the task list and notifies the adapter to refresh the UI.
     */
    fun refreshData(newTasks: List<Task>) {
        tasks = newTasks
        notifyDataSetChanged()
    }
}
