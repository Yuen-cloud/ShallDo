package com.example.shalldo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.Window
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.shalldo.logic.model.User
import com.example.shalldo.ui.account.AccountViewModel
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    private val viewModel by lazy {
        ViewModelProviders.of(this).get(AccountViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        register_btn.setOnClickListener {
            val username = register_username.text.toString()
            val password = register_password.text.toString()
            if (username == "" || password == "") {
                Toast.makeText(this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.register(username, password)
            }
        }
        checkBox_register_password.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {
                register_password.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                register_password.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }
        viewModel.registerLiveData.observe(this, Observer { result ->
            val msg = result.getOrNull()
            if (msg != null) {
                val username = register_username.text.toString()
                val password = register_password.text.toString()
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                intent.putExtra("username", username)
                intent.putExtra("password", password)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, result.exceptionOrNull()?.message, Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        })
    }
}