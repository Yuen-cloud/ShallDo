package com.example.shalldo.ui.myInfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.shalldo.logic.Repository
import com.example.shalldo.logic.model.Task

class MyInfoViewModel : ViewModel() {
    private val queryLiveData = MutableLiveData<String>()
    var finishedList = ArrayList<Int>()
    var unfinishedList = ArrayList<Int>()
    var dateList = ArrayList<String>()
    val sitLiveData = Transformations.switchMap(queryLiveData) {
        Repository.querySit()
    }

    fun querySit() {
        queryLiveData.value = ""
    }
}