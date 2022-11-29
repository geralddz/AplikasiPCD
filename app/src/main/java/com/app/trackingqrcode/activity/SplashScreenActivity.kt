package com.app.trackingqrcode.activity
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.app.trackingqrcode.R
import com.app.trackingqrcode.utils.SharedPref


@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private lateinit var sharedPref: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        sharedPref = SharedPref(this)
        Handler().postDelayed({
            checkAuth()
            finish()
        }, 1200)
    }

    private fun checkAuth() {
       if (!sharedPref.isSignIn()){
           startActivity(Intent(this, SignInActivity::class.java))
           finish()
        }else{
           startActivity(Intent(this, HomeActivity::class.java))
           finish()
       }
    }
}