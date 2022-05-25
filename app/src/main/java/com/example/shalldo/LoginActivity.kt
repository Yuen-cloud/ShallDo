package com.example.shalldo

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.shalldo.logic.model.User
import com.example.shalldo.ui.account.AccountViewModel
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(AccountViewModel::class.java)
    }
    private var exitTime: Long = 0
    private lateinit var prefs: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        prefs = getSharedPreferences("auto_login", Context.MODE_PRIVATE)
        editor = prefs.edit()
        if(intent.getBooleanExtra("cancel_auto_login", false)) {
            editor.putBoolean("isAutoLogin", false)
            editor.apply()
            checkBox_auto_login.isChecked = false
        }
        login_username.setText(intent.getStringExtra("username"))
        login_password.setText(intent.getStringExtra("password"))
        login_btn.setOnClickListener {
            val username = login_username.text.toString()
            val password = login_password.text.toString()
            if (username == "" || password == "") {
                Toast.makeText(this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.login(username, password)
            }
        }
        enter_register_btn.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        checkBox_login_password.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {
                login_password.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                login_password.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }
        checkBox_auto_login.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {
                editor.putBoolean("isAutoLogin", true)
            } else {
                editor.putBoolean("isAutoLogin", false)
            }
            editor.apply()
        }
        viewModel.loginLiveData.observe(this, Observer { result ->
            val account = result.getOrNull()
            if (account != null) {
                User.setUser(account.userId, account.username, account.password)
                editor.putInt("userId", account.userId)
                editor.putString("username", account.username)
                editor.putString("password", account.password)
                editor.apply()
                Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MenuActivity::class.java))
                finish()
            } else {
                editor.clear()
                checkBox_auto_login.isChecked = false
                Toast.makeText(this, result.exceptionOrNull()?.message, Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        })
        autoLogin()
    }

    private fun autoLogin() {
        val userId = prefs.getInt("userId", -1)
        if (userId != -1) {
            val isAutoLogin = prefs.getBoolean("isAutoLogin", false)
            if (isAutoLogin) {
                val username = prefs.getString("username", "")
                val password = prefs.getString("password", "")
                login_username.setText(username)
                login_password.setText(password)
                checkBox_auto_login.isChecked = isAutoLogin
                username?.let { password?.let { it1 -> viewModel.login(it, it1) } }
            }
        }
    }

    override fun onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show()
            exitTime = System.currentTimeMillis()
        } else {
            finish()
        }
    }
}