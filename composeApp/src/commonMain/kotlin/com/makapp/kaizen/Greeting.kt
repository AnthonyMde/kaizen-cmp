package com.makapp.kaizen

import com.makapp.kaizen.data.os.getPlatform

class Greeting {
    private val platform = getPlatform()

    fun greet(): String {
        return "Hello, ${platform.name}!"
    }
}