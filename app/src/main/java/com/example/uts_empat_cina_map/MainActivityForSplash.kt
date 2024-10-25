package com.example.yourpackage

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.uts_empat_cina_map.LoginActivity
import com.example.uts_empat_cina_map.MainActivity
import com.example.uts_empat_cina_map.R

class MainActivityForSplash : AppCompatActivity() {

    private lateinit var motionLayout: MotionLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.splash)

        motionLayout = findViewById(R.id.motionLayout)

        Handler().postDelayed({
            motionLayout.transitionToEnd()

            Handler().postDelayed({
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }, 2100)
        }, 150)
    }
}
