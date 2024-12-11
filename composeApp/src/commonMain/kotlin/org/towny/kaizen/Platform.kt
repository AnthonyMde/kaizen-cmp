package org.towny.kaizen

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform