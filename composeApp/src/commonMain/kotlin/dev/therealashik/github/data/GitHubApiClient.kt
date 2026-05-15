package dev.therealashik.github.data

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class GitHubUser(
    val login: String,
    val name: String? = null,
    @SerialName("avatar_url") val avatarUrl: String,
    val bio: String? = null,
    val followers: Int = 0,
    val following: Int = 0,
    @SerialName("public_repos") val publicRepos: Int = 0
)

class GitHubApiClient(private val tokenStorage: TokenStorage) {

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    private fun HttpRequestBuilder.withAuth() {
        tokenStorage.getToken()?.let { header(HttpHeaders.Authorization, "Bearer $it") }
        header(HttpHeaders.Accept, "application/vnd.github+json")
        header("X-GitHub-Api-Version", "2022-11-28")
    }

    suspend fun getAuthenticatedUser(): Result<GitHubUser> = runCatching {
        client.get("https://api.github.com/user") { withAuth() }.body()
    }

    fun close() = client.close()
}
