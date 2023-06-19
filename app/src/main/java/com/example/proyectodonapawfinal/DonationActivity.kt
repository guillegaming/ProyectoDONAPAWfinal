package com.example.proyectodonapawfinal

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.proyectodonapawfinal.databinding.ActivityDonationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DonationActivity : AppCompatActivity() {

    lateinit var binding: ActivityDonationBinding
    private var imgUri: String = ""
    private lateinit var uriImage: Uri
    val viewModel by viewModels<DonairViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donation)

        binding = ActivityDonationBinding.inflate(layoutInflater);
        setContentView(binding.root)

        binding.ivImage.setOnClickListener {
            com.github.dhaval2404.imagepicker.ImagePicker.with(this@DonationActivity)
                .maxResultSize(512, 512)
                .start();
        }

        binding.btnTake.setOnClickListener {
            collectState()

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            //Getting Gallery & CameraImage uri
            uriImage = data!!.data!!
            Glide.with(binding?.ivImage!!).load(uriImage)
                .into(binding?.ivImage!!)
            binding?.ivVisible?.visibility = View.GONE
            try {
                imgUri = uriImage.toString()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun collectState() {
        lifecycleScope.launch {
            viewModel.donair(
                NeedModel(
                    binding.etClothes.text.toString(),
                    pet = binding.etPets.text.toString(),
                    clothSize = binding.etClothSize.text.toString(),
                    address = binding.etAddress.text.toString(),
                    userId = FirebaseAuth.getInstance().currentUser!!.uid
                ), uriImage
            )

            viewModel.state.collect {
                binding.progressBar.isIndeterminate = it.isLoading

                if (it.success != null) {
                    Toast.makeText(applicationContext, it.success, Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@DonationActivity, MainActivity::class.java))
                }

                if (it.error != null) {
                    Toast.makeText(applicationContext, it.error, Toast.LENGTH_SHORT).show()
                }

            }
        }

    }

}