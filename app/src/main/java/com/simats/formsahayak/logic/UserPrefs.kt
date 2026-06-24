package com.simats.formsahayak.logic

import android.content.Context
import android.content.SharedPreferences

object UserPrefs {
    private const val PREFS_NAME = "FormSahayakUserPrefs"
    private const val KEY_IS_LOGGED_IN = "is_logged_in"
    private const val KEY_EMAIL = "email"
    private const val KEY_IS_DARK_MODE = "is_dark_mode"
    private const val KEY_IS_HIGH_CONTRAST = "is_high_contrast"
    private const val KEY_LANGUAGE_CODE = "language_code"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun isLoggedIn(context: Context): Boolean {
        return getPrefs(context).getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun setLoggedIn(context: Context, loggedIn: Boolean) {
        getPrefs(context).edit().putBoolean(KEY_IS_LOGGED_IN, loggedIn).apply()
    }

    fun getEmail(context: Context): String {
        return getPrefs(context).getString(KEY_EMAIL, "") ?: ""
    }

    fun setEmail(context: Context, email: String) {
        getPrefs(context).edit().putString(KEY_EMAIL, email).apply()
    }

    fun isDarkMode(context: Context): Boolean {
        return getPrefs(context).getBoolean(KEY_IS_DARK_MODE, false)
    }

    fun setDarkMode(context: Context, isDark: Boolean) {
        getPrefs(context).edit().putBoolean(KEY_IS_DARK_MODE, isDark).apply()
    }

    fun isHighContrast(context: Context): Boolean {
        return getPrefs(context).getBoolean(KEY_IS_HIGH_CONTRAST, false)
    }

    fun setHighContrast(context: Context, isHigh: Boolean) {
        getPrefs(context).edit().putBoolean(KEY_IS_HIGH_CONTRAST, isHigh).apply()
    }

    fun getLanguageCode(context: Context): String? {
        return getPrefs(context).getString(KEY_LANGUAGE_CODE, null)
    }

    fun setLanguageCode(context: Context, code: String?) {
        getPrefs(context).edit().putString(KEY_LANGUAGE_CODE, code).apply()
    }

    fun clear(context: Context) {
        getPrefs(context).edit().clear().apply()
    }
}
