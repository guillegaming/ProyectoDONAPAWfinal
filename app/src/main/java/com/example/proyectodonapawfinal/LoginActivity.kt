package com.example.proyectodonapawfinal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.proyectodonapawfinal.MainActivity
import com.example.proyectodonapawfinal.R
import com.example.proyectodonapawfinal.databinding.ActivityLoginBinding
import com.example.proyectodonapawfinal.databinding.ActivitySplashBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    lateinit var binding : ActivityLoginBinding
    val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater);
        setContentView(binding.root)

        binding.btnContinue.setOnClickListener {
         collectState()
        }

        binding.tvForgotPassword.setOnClickListener {
            startActivity(Intent(this@LoginActivity, ForgotPasswordActivity::class.java))
        }

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }

    }


    private fun collectState() {
        lifecycleScope.launch {
            viewModel.login(
                binding.etEmail.text.toString().trim()
                , binding.etPassword.text.toString().trim()
            )
            viewModel.state.collect {
                binding.progressBar.isIndeterminate = it.isLoading
                if (it.success != null) {
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    Toast.makeText(applicationContext, it.success, Toast.LENGTH_SHORT).show()
                }
                if (it.error != null) {
                    Toast.makeText(applicationContext, it.error, Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}