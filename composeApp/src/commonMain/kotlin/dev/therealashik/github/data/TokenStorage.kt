package dev.therealashik.github.data

interface TokenStorage {
    fun saveToken(token: String)
    fun getToken(): String?
    fun clearToken()
}

expect fun createTokenStorage(): TokenStorage
