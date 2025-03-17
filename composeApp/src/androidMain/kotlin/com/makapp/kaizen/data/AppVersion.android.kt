package com.makapp.kaizen.data

import com.makapp.kaizen.BuildConfig

actual val appVersion: String
    get() {
        val buildType = if (BuildConfig.BUILD_TYPE === "debug") "(d)" else ""
        return "v${BuildConfig.VERSION_NAME} - ${BuildConfig.VERSION_CODE}$buildType"
    }
