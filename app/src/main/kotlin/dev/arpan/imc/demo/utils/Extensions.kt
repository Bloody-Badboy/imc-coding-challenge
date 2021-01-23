package dev.arpan.imc.demo.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat

val Context.hasLocationPermission: Boolean
    get() {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

fun String.toToast(context: Context) {
    Toast.makeText(context, this, Toast.LENGTH_LONG).show()
}
