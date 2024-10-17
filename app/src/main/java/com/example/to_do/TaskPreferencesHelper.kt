package com.example.to_do

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TaskPreferencesHelper(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("TasksPreferences", Context.MODE_PRIVATE)
    private val gson = Gson()

    companion object {
        private const val TASK_LIST_KEY = "task_list"
    }

    // Fetch all tasks
    fun getAllTasks(): List<Task> {
        val tasksJson = sharedPreferences.getString(TASK_LIST_KEY, "[]")
        val taskType = object : TypeToken<List<Task>>() {}.type
        return gson.fromJson(tasksJson, taskType)
    }

    // Save all tasks (overwrite the previous list)
    private fun saveAllTasks(tasks: List<Task>) {
        val editor = sharedPreferences.edit()
        val tasksJson = gson.toJson(tasks)
        editor.putString(TASK_LIST_KEY, tasksJson)
        editor.apply()
    }

    // Insert a task
    fun insertTask(task: Task) {
        val tasks = getAllTasks().toMutableList()
        tasks.add(task)
        saveAllTasks(tasks)
    }

    // Update a task by ID
    fun updateTask(updatedTask: Task) {
        val tasks = getAllTasks().toMutableList()
        val taskIndex = tasks.indexOfFirst { it.id == updatedTask.id }
        if (taskIndex != -1) {
            tasks[taskIndex] = updatedTask
            saveAllTasks(tasks)
        }
    }

    // Delete a task by ID
    fun deleteTask(taskId: Int) {
        val tasks = getAllTasks().toMutableList()
        tasks.removeAll { it.id == taskId }
        saveAllTasks(tasks)
    }

    // Get task by ID
    fun getTaskByID(taskId: Int): Task? {
        return getAllTasks().find { it.id == taskId }
    }
}
