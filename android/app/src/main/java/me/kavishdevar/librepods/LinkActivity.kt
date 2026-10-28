/*
    LibrePods - AirPods liberated from Apple’s ecosystem
    Copyright (C) 2025 LibrePods contributors

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.
*/

package me.kavishdevar.librepods

import android.app.Activity
import android.os.Bundle
import android.util.Log
import me.kavishdevar.librepods.bluetooth.AACPManager
import me.kavishdevar.librepods.services.ServiceManager

/**
 * Handles librepods://anc/<off|anc|transparency|adaptive> links, so launchers and other apps can
 * switch the listening mode without opening the app. Shows no UI.
 */
class LinkActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val uri = intent?.data
        if (uri?.scheme == "librepods" && uri.host == "anc") {
            val mode = when (uri.lastPathSegment?.lowercase()) {
                "off" -> 1
                "anc", "noise-cancellation" -> 2
                "transparency" -> 3
                "adaptive" -> 4
                else -> null
            }
            val service = ServiceManager.getService()
            if (mode != null && service != null) {
                service.aacpManager.sendControlCommand(
                    AACPManager.Companion.ControlCommandIdentifiers.LISTENING_MODE.value,
                    mode.toByte()
                )
            } else {
                Log.w("LinkActivity", "Can't handle $uri (service running: ${service != null})")
            }
        }
        finish()
    }
}
