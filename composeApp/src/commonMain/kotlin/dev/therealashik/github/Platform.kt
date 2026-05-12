package dev.therealashik.github

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform