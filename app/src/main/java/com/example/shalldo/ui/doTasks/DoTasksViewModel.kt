package com.example.shalldo.ui.doTasks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.shalldo.MenuActivity
import com.example.shalldo.logic.Repository
import com.example.shalldo.logic.model.Task

class DoTasksViewModel : ViewModel() {
    private val queryLiveData = MutableLiveData<String>()
    val taskList = ArrayList<Task>()
    val tasksLiveData = Transformations.switchMap(queryLiveData) {
        Repository.queryAllTasks()
    }

    fun queryAllTasks() {
        queryLiveData.value = ""
    }
}