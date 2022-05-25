package com.example.shalldo.ui.account

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.shalldo.logic.Repository
import com.example.shalldo.logic.model.Account

class AccountViewModel : ViewModel() {
    private val accountLiveData = MutableLiveData<Account>()
    private val resultMsgLiveData = MutableLiveData<String>()
    val loginLiveData = Transformations.switchMap(accountLiveData) { account ->
        Repository.login(account.username, account.password)
    }
    val registerLiveData = Transformations.switchMap(accountLiveData) { account ->
        Repository.register(account.username, account.password)
    }

    fun login(username: String, password: String) {
        accountLiveData.value = Account(-1, username, password)
    }

    fun register(username: String, password: String) {
        accountLiveData.value = Account(-1, username, password)
    }
}