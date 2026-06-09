package dev.therealashik.github.data

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.request.setBody
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@Serializable
data class GitHubUser(
    val login: String,
    val name: String? = null,
    @SerialName("avatar_url") val avatarUrl: String,
    val bio: String? = null,
    val company: String? = null,
    val location: String? = null,
    val followers: Int = 0,
    val following: Int = 0,
    @SerialName("public_repos") val publicRepos: Int = 0
)

data class GitHubUserStatus(
    val emoji: String?,
    val message: String?
)


@Serializable
data class GitHubRepoOwner(
    val login: String,
    @SerialName("avatar_url") val avatarUrl: String
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
    val owner: GitHubRepoOwner? = null,
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

@Serializable
data class SearchResult<T>(val items: List<T>)

@Serializable
data class GitHubEvent(
    val id: String,
    val type: String,
    val actor: GitHubActor,
    val repo: EventRepo,
    val payload: EventPayload? = null,
    @SerialName("created_at") val createdAt: String
)

@Serializable
data class GitHubActor(val login: String, @SerialName("avatar_url") val avatarUrl: String)

@Serializable
data class EventRepo(val name: String)

@Serializable
data class EventPayload(
    val action: String? = null,
    @SerialName("pull_request") val pullRequest: EventPullRequest? = null,
    val release: EventRelease? = null
)

@Serializable
data class EventPullRequest(
    val title: String, val body: String? = null, val state: String, val head: EventHead
)

@Serializable
data class EventHead(val ref: String)

@Serializable
data class EventRelease(val name: String? = null, @SerialName("tag_name") val tagName: String)

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

    suspend fun getUserStatus(): Result<GitHubUserStatus> = runCatching {
        val query = """{"query":"{ viewer { status { emoji message } } }"}"""
        val response: JsonObject = client.post("https://api.github.com/graphql") {
            withAuth()
            contentType(ContentType.Application.Json)
            setBody(query)
        }.body()
        val status = response["data"]?.jsonObject
            ?.get("viewer")?.jsonObject
            ?.get("status")?.jsonObject
        GitHubUserStatus(
            emoji = status?.get("emoji")?.jsonPrimitive?.content,
            message = status?.get("message")?.jsonPrimitive?.content
        )
    }

    suspend fun getTrendingRepos(): Result<SearchResult<GitHubRepo>> = runCatching {
        client.get("https://api.github.com/search/repositories") {
            withAuth()
            parameter("q", "stars:>1000")
            parameter("sort", "stars")
            parameter("order", "desc")
            parameter("per_page", 10)
        }.body()
    }

    suspend fun getReceivedEvents(username: String, perPage: Int = 20): Result<List<GitHubEvent>> = runCatching {
        client.get("https://api.github.com/users/${username}/received_events") {
            withAuth()
            parameter("per_page", perPage)
        }.body()
    }

    fun close() = client.close()
}
