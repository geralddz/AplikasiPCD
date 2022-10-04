package com.app.trackingqrcode.api

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences

class Session (context : Context){

    val PRIVATE_MODE = 0
    private val PREF_NAME = "SharedPreferences"
    private val IS_LOGIN = "is_login"

    var pref: SharedPreferences? = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
    var editor: SharedPreferences.Editor? = pref?.edit()

    fun setName(name: String) {
        editor?.putString("name", name)
        editor?.commit()
    }

    fun setSignIn(isLogin: Boolean) {
        editor?.putBoolean(IS_LOGIN, isLogin)
        editor?.commit()
    }

    fun getName(): String? {
        return pref?.getString("name", "")
    }

    fun isSignIn(): Boolean? {
        return pref?.getBoolean(IS_LOGIN, false)
    }

    fun isSignOut() {
        editor?.clear()
        editor?.commit()
    }

}