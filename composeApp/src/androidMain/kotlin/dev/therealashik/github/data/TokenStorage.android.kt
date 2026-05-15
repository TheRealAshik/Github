package dev.therealashik.github.data

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

private class AndroidTokenStorage(context: Context) : TokenStorage {
    // TODO: Replace with EncryptedSharedPreferences for production
    private val prefs: SharedPreferences =
        context.getSharedPreferences("github_prefs", Context.MODE_PRIVATE)

    override fun saveToken(token: String) { prefs.edit().putString(KEY, token).apply() }
    override fun getToken(): String? = prefs.getString(KEY, null)
    override fun clearToken() { prefs.edit().remove(KEY).apply() }

    companion object { private const val KEY = "pat_token" }
}

@SuppressLint("StaticFieldLeak")
private var appContext: Context? = null

fun initTokenStorage(context: Context) {
    appContext = context.applicationContext
}

actual fun createTokenStorage(): TokenStorage =
    AndroidTokenStorage(checkNotNull(appContext) { "Call initTokenStorage(context) in MainActivity.onCreate" })
