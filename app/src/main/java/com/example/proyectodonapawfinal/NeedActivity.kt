package com.example.proyectodonapawfinal

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectodonapawfinal.databinding.ActivityNeedBinding
import androidx.core.content.ContextCompat

class NeedActivity : AppCompatActivity() {

    private var binding: ActivityNeedBinding? = null
    private var adapter: NeedAdapter? = null
    private var list: ArrayList<NeedModel>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNeedBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.rvItems?.setHasFixedSize(true)
        binding?.rvItems?.layoutManager = LinearLayoutManager(this)

        list = ArrayList()
        listData()
        adapter = NeedAdapter()
        binding?.rvItems?.adapter = adapter
        adapter?.submitList(list)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun listData() {
        list?.clear()
        list?.add(
            NeedModel(
                "Manta (80 x 100 cm)",
                "Mascota: Perros",
                ContextCompat.getDrawable(this, R.drawable.l1)?.toString()
            )
        )
        list?.add(
            NeedModel(
                "Chaqueta (talla 25)",
                "Mascota: Perros",
                ContextCompat.getDrawable(this, R.drawable.l2)?.toString()
            )
        )
        list?.add(
            NeedModel(
                "Arnés y correa (talla XL)",
                "Mascota: Gatos",
                ContextCompat.getDrawable(this, R.drawable.l3)?.toString()
            )
        )
        list?.add(
            NeedModel(
                "Chubasquero (talla 36)",
                "Mascota: Perros",
                ContextCompat.getDrawable(this, R.drawable.l4)?.toString()
            )
        )
    }
}
