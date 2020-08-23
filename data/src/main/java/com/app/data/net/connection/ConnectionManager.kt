package com.app.data.net.connection

import android.content.Context

interface ConnectionManager {

    fun isNetworkAvailable(): Boolean
}