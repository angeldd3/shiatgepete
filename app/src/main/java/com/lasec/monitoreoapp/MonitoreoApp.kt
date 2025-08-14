package com.lasec.monitoreoapp

import android.app.Application
import android.util.Log
import com.lasec.monitoreoapp.data.remote.TokenProvider
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@HiltAndroidApp
class MonitoreoApp : Application() {

    override fun onCreate() {
        super.onCreate()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                TokenProvider.initClientToken(applicationContext)
                Log.e("Comprobacion", "Token obtenido ")
            } catch (e: Exception) {
                Log.e("AppInit", "❌ Error al obtener token inicial: ${e.message}")
            }
        }
    }
}

