package dev.therealashik.github.data

import java.util.prefs.Preferences

private class JvmTokenStorage : TokenStorage {
    private val prefs = Preferences.userRoot().node("dev/therealashik/github")

    override fun saveToken(token: String) { prefs.put(KEY, token) }
    override fun getToken(): String? = prefs.get(KEY, null)?.takeIf { it.isNotEmpty() }
    override fun clearToken() { prefs.remove(KEY) }

    companion object { private const val KEY = "pat_token" }
}

actual fun createTokenStorage(): TokenStorage = JvmTokenStorage()
