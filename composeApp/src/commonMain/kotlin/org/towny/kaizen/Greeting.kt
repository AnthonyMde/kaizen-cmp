package org.towny.kaizen

import org.towny.kaizen.data.os.getPlatform

class Greeting {
    private val platform = getPlatform()

    fun greet(): String {
        return "Hello, ${platform.name}!"
    }
}