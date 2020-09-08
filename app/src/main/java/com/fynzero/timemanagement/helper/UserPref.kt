package com.fynzero.timemanagement.helper

import android.content.Context

class UserPref(context: Context) {
    companion object {
        private const val PREFS_NAME = "user_pref"
        const val IS_LOGIN = "is_login"
        const val PREF_USERNAME = "pref_username"
    }

    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val editor = preferences.edit()

    fun setName(key: String, value: String) {
        editor.putString(key, value).apply()
    }

    fun getName(key: String): String? = preferences.getString(key, null)

    fun isLogin(key: String, value: Boolean) {
        editor.putBoolean(key, value).apply()
    }

    fun getLogin(key: String): Boolean = preferences.getBoolean(key, false)
}