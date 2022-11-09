package com.app.trackingqrcode.api

import android.content.Context
import android.content.SharedPreferences

class SharedPref (context : Context){

    val PRIVATE_MODE = 0
    private val PREF_NAME = "SharedPreferences"
    private val IS_LOGIN = "is_login"

    var pref: SharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
    var editor: SharedPreferences.Editor? = pref?.edit()


    fun setName(name: String) {
        editor?.putString("name", name)
        editor?.commit()
    }

    fun setIdUser(iduser: String) {
        editor?.putString("iduser", iduser)
        editor?.commit()
    }

    fun setStation(station: String) {
        editor?.putString("station", station)
        editor?.commit()
    }

    fun setIdPlan(idplan: String) {
        editor?.putString("idplan", idplan)
        editor?.commit()
    }
    fun setIdStation(idstation: String) {
        editor?.putString("idstation", idstation)
        editor?.commit()
    }

    fun setStatus(status: String){
        editor?.putString("status", status)
        editor?.commit()
    }

    fun setPartname(partname: String){
        editor?.putString("partname", partname)
        editor?.commit()
    }


    fun setSignIn(isLogin: Boolean) {
        editor?.putBoolean(IS_LOGIN, isLogin)
        editor?.commit()
    }

    fun getName(): String? {
        return pref?.getString("name", "")
    }

    fun getIdUser(): String? {
        return pref?.getString("iduser", "")
    }

    fun getStation(): String? {
        return pref?.getString("station", "")
    }

    fun getIdPlan(): String? {
        return pref?.getString("idplan", "")
    }

    fun getIdStation(): String? {
        return pref?.getString("idstation", "")
    }

    fun getStatus(): String? {
        return pref?.getString("status", "")
    }

    fun getPartname(): String? {
        return pref?.getString("partname", "")
    }

    fun isSignIn(): Boolean? {
        return pref?.getBoolean(IS_LOGIN, false)
    }

    fun isSignOut() {
        editor?.clear()
        editor?.commit()
    }

}