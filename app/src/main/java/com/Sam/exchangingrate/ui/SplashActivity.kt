package com.Sam.exchangingrate.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.Sam.exchangingrate.R
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        lottie_animation.setAnimation("currency.json")
        lottie_animation.playAnimation()

        Handler().postDelayed({
            goToMain()

        }, 2250)
    }

    private fun goToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        Animatoo.animateFade(this)
        finish()
    }
}
