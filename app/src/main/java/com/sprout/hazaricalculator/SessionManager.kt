package com.sprout.hazaricalculator

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson

class SessionManager(private val context: Context) {
    var pref: SharedPreferences
    private val editor: SharedPreferences.Editor

    init {
        pref = context.getSharedPreferences(sharedPreferenceName, Context.MODE_PRIVATE)
        editor = pref.edit()
    }

    var players: Players?
        get() {
            val gson = Gson()
            val data = pref.getString(PLAYER_NAMES, null)
            return if (data != null) {
                gson.fromJson(data, Players::class.java)
            } else null
        }
        set(userInfo) {
            val gson = Gson()
            val json: String = gson.toJson(userInfo)
            editor.putString(PLAYER_NAMES, json)
            editor.apply()
        }

    fun setLogin(isLoggedIn: Boolean) {
        editor.putBoolean(IS_LOGGED_IN, isLoggedIn)
        editor.apply()
    }

    val isLoggedIn: Boolean
        get() = pref.getBoolean(IS_LOGGED_IN, false)

    fun clearUserInfo() {
        editor.putString(PLAYER_NAMES, null)
        editor.apply()
    }

    companion object {
        var sharedPreferenceName = "SESSION_MANAGER_HPM"
        private const val PLAYER_NAMES = "user_info"
        private const val IS_LOGGED_IN = "is_logged_in"
    }
}