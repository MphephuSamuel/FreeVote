package com.example.freevote.util

import android.content.Context
import android.content.SharedPreferences

object PreferencesUtil {
    private const val PREFERENCES_NAME = "freevote_preferences"
    private const val KEY_TERMS_ACCEPTED = "terms_accepted"

    fun setTermsAccepted(context: Context, accepted: Boolean) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(KEY_TERMS_ACCEPTED, accepted)
        editor.apply()
    }

    fun isTermsAccepted(context: Context): Boolean {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(KEY_TERMS_ACCEPTED, false)
    }
}
