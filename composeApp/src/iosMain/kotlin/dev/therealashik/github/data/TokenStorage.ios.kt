package dev.therealashik.github.data

import platform.Foundation.NSUserDefaults

private class IosTokenStorage : TokenStorage {
    // TODO: Replace with Keychain (Security framework) for production
    private val defaults = NSUserDefaults.standardUserDefaults

    override fun saveToken(token: String) { defaults.setObject(token, KEY) }
    override fun getToken(): String? = defaults.stringForKey(KEY)
    override fun clearToken() { defaults.removeObjectForKey(KEY) }

    companion object { private const val KEY = "pat_token" }
}

actual fun createTokenStorage(): TokenStorage = IosTokenStorage()
