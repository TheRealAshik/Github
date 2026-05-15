package dev.therealashik.github.repository

data class RepositoryListUiState(
    val repositories: List<RepositoryItem> = listOf(
        RepositoryItem(
            id = "1",
            name = "Jules",
            description = "A client of Jules (end-to-end coding agent by Google Labs) based on KMP",
            forkedFrom = null,
            stars = 3,
            language = "Kotlin"
        ),
        RepositoryItem(
            id = "2",
            name = "awsome-pc",
            description = "Awsome personal computer configuration combination for best value-for-money purchase.",
            forkedFrom = null,
            stars = 0,
            language = null
        ),
        RepositoryItem(
            id = "3",
            name = "Projectivy-Launcher",
            description = "Fork of Projectivy Launcher in Material 3 Design",
            forkedFrom = "spocky/miproja1",
            stars = 0,
            language = null
        ),
        RepositoryItem(
            id = "4",
            name = "gh-actions",
            description = null,
            forkedFrom = null,
            stars = 0,
            language = "Shell"
        ),
        RepositoryItem(
            id = "5",
            name = "TizenTubeCobalt",
            description = "Experience TizenTube on other devices that are not Tizen.",
            forkedFrom = "reisxd/TizenTubeCobalt",
            stars = 0,
            language = null
        )
    )
)

data class RepositoryItem(
    val id: String,
    val name: String,
    val description: String?,
    val forkedFrom: String?,
    val stars: Int,
    val language: String?
)
