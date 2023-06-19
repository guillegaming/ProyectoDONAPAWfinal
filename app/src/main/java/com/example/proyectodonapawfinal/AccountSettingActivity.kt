package com.example.proyectodonapawfinal

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.example.proyectodonapawfinal.databinding.ActivityAccountSettingBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AccountSettingActivity : AppCompatActivity() {

    lateinit var binding: ActivityAccountSettingBinding
    val viewModel: AccountSettingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAccountSettingBinding.inflate(layoutInflater);
        setContentView(binding.root)
        collectState()
        binding.btnChangePassword.setOnClickListener {
            val intent = Intent(this@AccountSettingActivity, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
        binding.btnSaveChanges.setOnClickListener {
            updateProfile()
        }
        binding.changeEmailIcon.setOnClickListener {
            showEditCredentialsDialog()
        }

    }

    private fun collectState() {
        lifecycleScope.launch {
            viewModel.state.collect {
                binding.progressBar.isIndeterminate = it.isLoading
                if (it.user != null) {
                    binding.etFirstName.setText(it.user.firstName)
                    binding.etLastName.setText(it.user.lastName)
                    binding.etEmail.setText(it.user.email)
                    binding.etAddress.setText(it.user.address)
                }
                if (it.error != null) {
                    Toast.makeText(applicationContext, it.error, Toast.LENGTH_SHORT).show()
                }

            }
        }

    }

    private fun updateProfile() {
        lifecycleScope.launch {
            viewModel.updateInfo(
                UserModel(
                    firstName = binding.etFirstName.text.toString(),
                    lastName = binding.etLastName.text.toString(),
                    address = binding.etAddress.text.toString(),
                    email = binding.etEmail.text.toString()
                )
            )

            viewModel.state1.collect {
                binding.progressBar.isIndeterminate = it.isLoading
                if (it.success != null) {
                    Toast.makeText(applicationContext, it.success, Toast.LENGTH_SHORT).show()
                }
                if (it.error != null) {
                    Toast.makeText(applicationContext, it.error, Toast.LENGTH_SHORT).show()
                }

            }

        }

    }

    private fun changeEmail(password: String, newEmail: String) {
        lifecycleScope.launch {
            viewModel.updateEmail(
                 password, newEmail
            )

            viewModel.state2.collect {
                binding.progressBar.isIndeterminate = it.isLoading
                if (it.success != null) {
                    Toast.makeText(applicationContext, it.success, Toast.LENGTH_SHORT).show()
                }
                if (it.error != null) {
                    Toast.makeText(applicationContext, it.error, Toast.LENGTH_SHORT).show()
                }

            }
        }
    }


    private fun showEditCredentialsDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_email_change, null)
        val editTextPassword = dialogView.findViewById<EditText>(R.id.editTextPassword)
        val editTextEmail = dialogView.findViewById<EditText>(R.id.editTextEmail)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton("Update") { dialogInterface: DialogInterface, i: Int ->
                // Handle the update button click here
                val password = editTextPassword.text.toString()
                val email = editTextEmail.text.toString()
                // Perform the necessary operations with the updated credentials
                changeEmail( password,email)
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()
    }
}