package com.app.trackingqrcode.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.trackingqrcode.R
import com.app.trackingqrcode.api.ApiUtils
import com.app.trackingqrcode.api.SharedPref
import com.app.trackingqrcode.request.UserRequest
import com.app.trackingqrcode.response.UserResponse
import kotlinx.android.synthetic.main.activity_sign_in.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInActivity : AppCompatActivity() {
    private lateinit var sharedPref: SharedPref
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        sharedPref = SharedPref(this)

        btnSignIn.setOnClickListener {
            val user = etUserSignIn.text.toString().trim()
            val pass = etPassSignIn.text.toString().trim()
           if (checkvalidation(user, pass)){
               login()
           }
        }
    }


    private fun checkvalidation(user: String, pass: String): Boolean {
        if (user.isEmpty()) {
            etUserSignIn.error = "Masukkan Username Anda"
            etUserSignIn.requestFocus()
        } else if (pass.isEmpty()) {
            etPassSignIn.error = "Masukkan Password Anda"
            etPassSignIn.requestFocus()
        } else {
            return true
        }
        return false
    }

    private fun login() {
        val user = etUserSignIn.text.toString().trim()
        val passw = etPassSignIn.text.toString().trim()
        val stid = "Administrator"
        val retro = ApiUtils().getUserService()
        retro.login(stid,user,passw).enqueue(object : Callback<UserResponse>{
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                val user = response.body()
                if (user != null) {
                    Toast.makeText(applicationContext, "Login Berhasil , Selamat Datang ", Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent (applicationContext,HomeActivity::class.java)
                    val iduser = user.data?.id.toString()
                    val name = user.data?.name.toString()
                    sharedPref.setIdUser(iduser)
                    sharedPref.setName(name)
                    sharedPref.setSignIn(true)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(applicationContext, "Login gagal, Data tidak terdaftar", Toast.LENGTH_SHORT
                    ).show()
                }
            }
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
               Log.e("Error", t.message!!)
            }
        })
    }
}