package com.makapp.kaizen.data.os

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform