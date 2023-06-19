package com.example.proyectodonapawfinal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope

import com.example.proyectodonapawfinal.databinding.ActivityMyDonationsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyDonationsActivity : AppCompatActivity() {

    lateinit var binding: ActivityMyDonationsBinding
    val viewModel by viewModels<DonairViewModel>()
    val adapter by lazy { NeedAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyDonationsBinding.inflate(layoutInflater);
        setContentView(binding.root)
        collectState()


    }

    private fun collectState() {
        lifecycleScope.launch {
            viewModel.state1.collect {
                adapter.submitList(it.listDonair)
                if (adapter.itemCount != 0) {
                    binding.rvItems.adapter = adapter
                }

                binding.progressBar.isIndeterminate = it.isLoading

                if (it.error != null) {
                    Toast.makeText(applicationContext, it.error, Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}