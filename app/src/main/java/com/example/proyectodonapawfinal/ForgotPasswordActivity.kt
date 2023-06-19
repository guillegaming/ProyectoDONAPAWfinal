package com.example.proyectodonapawfinal

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.proyectodonapawfinal.MainActivity
import com.example.proyectodonapawfinal.R
import com.example.proyectodonapawfinal.databinding.ActivityForgotPasswordBinding
import com.example.proyectodonapawfinal.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ForgotPasswordActivity : AppCompatActivity() {

    lateinit var binding : ActivityForgotPasswordBinding
    val viewModel: ForgetPasswordViewModel by viewModels()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        if (intent.extras!=null){
//            binding.tvLabel.text = "Change Password"
//        }

        binding.btnContinue.setOnClickListener {
           collectState()
        }

    }

    private fun collectState(){
        lifecycleScope.launch{
            viewModel.resetPassword(binding.etEmail.text.toString())

            viewModel.state.collect{
                binding.progressBar.isIndeterminate = it.isLoading
                if (it.success!=null){
                    Toast.makeText(applicationContext, it.success, Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@ForgotPasswordActivity, LoginActivity::class.java))
                }
                if (it.error!=null){
                    Toast.makeText(applicationContext, it.error, Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}