package dev.therealashik.github.data

private class WebTokenStorage : TokenStorage {
    private var token: String? = null
    override fun saveToken(token: String) { this.token = token }
    override fun getToken(): String? = token
    override fun clearToken() { token = null }
}

actual fun createTokenStorage(): TokenStorage = WebTokenStorage()
