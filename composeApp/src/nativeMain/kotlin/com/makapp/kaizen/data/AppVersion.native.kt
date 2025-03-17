package com.makapp.kaizen.data

import platform.Foundation.NSBundle
import kotlin.experimental.ExperimentalNativeApi

@OptIn(ExperimentalNativeApi::class)
actual val appVersion: String
    get() {
        val version = NSBundle.mainBundle.infoDictionary?.get("CFBundleShortVersionString") as? String ?: "Unknown"
        val build = NSBundle.mainBundle.infoDictionary?.get("CFBundleVersion") as? String ?: "Unknown"
        val buildType = if (Platform.isDebugBinary) "(d)" else ""
        return "v$version-$build$buildType"
    }
