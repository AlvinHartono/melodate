package com.example.melodate

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.melodate.ui.register.RegisterEmailPasswordActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        splashScreen.setKeepOnScreenCondition {
            Thread.sleep(2000)
            false
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val imageView = findViewById<ImageView>(R.id.imageView)
        val isDarkMode = when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> true
            Configuration.UI_MODE_NIGHT_NO -> false
            else -> false
        }
        val imageRes = if (isDarkMode) {
            R.drawable.light_icon_full
        } else {
            R.drawable.icon_full
        }
        imageView.setImageResource(imageRes)

        val buttonGetStarted = findViewById<Button>(R.id.buttonGetStarted)
        buttonGetStarted.setOnClickListener {
//            val intent = Intent(this@MainActivity, HomeActivity::class.java)
            val intent = Intent(this@MainActivity, RegisterEmailPasswordActivity::class.java)
            startActivity(intent)
        }
    }
}