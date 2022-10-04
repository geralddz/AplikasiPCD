package com.app.trackingqrcode.activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.app.trackingqrcode.R
import com.app.trackingqrcode.api.Session


class SplashScreenActivity : AppCompatActivity() {
    private lateinit var session: Session

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        session = Session(this)
        Handler().postDelayed({
            checkAuth()
            finish()
        }, 1200)
    }

    private fun checkAuth() {
       if (session.isSignIn() == false){
           startActivity(Intent(this, SignInActivity::class.java))
           finish()
        }else{
           startActivity(Intent(this, HomeActivity::class.java))
           finish()
       }
    }
}