package com.example.proyectodonapawfinal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.proyectodonapawfinal.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnDonate.setOnClickListener {
            startActivity(Intent(this, DonationActivity::class.java))
        }

        binding.btnNeed.setOnClickListener {
            startActivity(Intent(this, NeedActivity::class.java))
        }

        binding.btnMyDonations.setOnClickListener {
            startActivity(Intent(this, MyDonationsActivity::class.java))
        }

        binding.btnAccountSetting.setOnClickListener {
            startActivity(Intent(this, AccountSettingActivity::class.java))
        }

        binding.btnLogout.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.btnExplanation.setOnClickListener {
            startActivity(Intent(this, ExplanationActivity::class.java))
        }
    }
}
