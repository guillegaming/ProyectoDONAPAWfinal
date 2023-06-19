package com.example.proyectodonapawfinal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.proyectodonapawfinal.R
import com.example.proyectodonapawfinal.databinding.ActivityRegisterBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    lateinit var binding: ActivityRegisterBinding
    val viewModel: registerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCreateAccount.setOnClickListener {
            collectState()
//            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        }


    }

    private fun collectState() {
        lifecycleScope.launch {
            viewModel.register(
                UserModel(
                    binding.etFirstName.text.toString(),
                    binding.etLastName.text.toString(),
                    binding.etEmail.text.toString(),
                    binding.etAddress.text.toString()
                ), binding.etPassword.text.toString()
            )
            viewModel.state.collect {
                binding.progressBar.isIndeterminate = it.isLoading
                if (it.success != null) {
                    startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                    Toast.makeText(applicationContext, it.success, Toast.LENGTH_SHORT).show()
                }
                if (it.error != null) {
                    Toast.makeText(applicationContext, it.error, Toast.LENGTH_SHORT).show()
                }

                Log.e("collectState: ", it.error.toString())

            }
        }

    }
}