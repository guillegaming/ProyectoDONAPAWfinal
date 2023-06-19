package com.example.proyectodonapawfinal

import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectodonapawfinal.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    var binding : ActivitySplashBinding? = null
    var fromTop: Animation? = null
    var fromBottom: Animation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        fromTop = AnimationUtils.loadAnimation(this, R.anim.arriba)
        fromBottom = AnimationUtils.loadAnimation(this, R.anim.abajo)
        //Calling the thread and setting the time 3 seconds so that activity can stay here and then move to next
        val thread: Thread = object : Thread() {
            override fun run() {
                try {
                    sleep(3000)
                    startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                    finish()
                } catch (e: InterruptedException) {
                    Toast.makeText(this@SplashActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
        thread.start()

    }
}