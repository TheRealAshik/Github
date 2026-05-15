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

@Serializable
data class GitHubRepo(
    val id: Long,
    val name: String,
    @SerialName("full_name") val fullName: String,
    val description: String? = null,
    val private: Boolean = false,
    @SerialName("stargazers_count") val stars: Int = 0,
    val language: String? = null,
    @SerialName("updated_at") val updatedAt: String? = null
)

@Serializable
data class GitHubOrg(
    val id: Long,
    val login: String,
    @SerialName("avatar_url") val avatarUrl: String,
    val description: String? = null
)

@Serializable
data class GitHubNotification(
    val id: String,
    val unread: Boolean,
    val reason: String,
    @SerialName("updated_at") val updatedAt: String,
    val subject: NotificationSubject,
    val repository: NotificationRepo
)

@Serializable
data class NotificationSubject(
    val title: String,
    val type: String,
    val url: String? = null
)

@Serializable
data class NotificationRepo(
    @SerialName("full_name") val fullName: String
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

    suspend fun getUserRepos(perPage: Int = 10): Result<List<GitHubRepo>> = runCatching {
        client.get("https://api.github.com/user/repos") {
            withAuth()
            parameter("sort", "updated")
            parameter("per_page", perPage)
        }.body()
    }

    suspend fun getUserOrgs(): Result<List<GitHubOrg>> = runCatching {
        client.get("https://api.github.com/user/orgs") { withAuth() }.body()
    }

    suspend fun getNotifications(perPage: Int = 20): Result<List<GitHubNotification>> = runCatching {
        client.get("https://api.github.com/notifications") {
            withAuth()
            parameter("per_page", perPage)
        }.body()
    }

    suspend fun getStarredRepos(perPage: Int = 5): Result<List<GitHubRepo>> = runCatching {
        client.get("https://api.github.com/user/starred") {
            withAuth()
            parameter("per_page", perPage)
        }.body()
    }

    fun close() = client.close()
}
