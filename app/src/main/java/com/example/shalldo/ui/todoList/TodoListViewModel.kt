package com.example.shalldo.ui.todoList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.shalldo.logic.Repository
import com.example.shalldo.logic.model.Task

class TodoListViewModel : ViewModel() {
    private val queryLiveData = MutableLiveData<String>()
    val taskList = ArrayList<Task>()
    val tasksLiveData = Transformations.switchMap(queryLiveData) {
        Repository.queryAllTasks()
    }

    fun queryAllTasks() {
        queryLiveData.value = ""
    }
}