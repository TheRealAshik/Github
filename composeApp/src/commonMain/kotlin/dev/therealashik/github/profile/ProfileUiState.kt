package dev.therealashik.github.profile

data class ProfileUiState(
    val followerCount: Int = 20,
    val followingCount: Int = 3,
    val popularRepos: List<PopularRepo> = listOf(
        PopularRepo(
            id = "1",
            owner = "TheRealAshik",
            name = "Jules",
            description = "A client of Jules (end-to-end coding agent by Google Labs) based on KMP",
            stars = 3,
            language = "Kotlin"
        ),
        PopularRepo(
            id = "2",
            owner = "TheRealAshik",
            name = "awsome-pc",
            description = "Awsome personal computer configuration combination for best value-for-money purchase.",
            stars = 0,
            language = null
        )
    ),
    val repoCount: Int = 10,
    val orgCount: Int = 2,
    val starredCount: Int = 25,
    val projectCount: Int = 2
)

data class PopularRepo(
    val id: String,
    val owner: String,
    val name: String,
    val description: String?,
    val stars: Int,
    val language: String?
)
