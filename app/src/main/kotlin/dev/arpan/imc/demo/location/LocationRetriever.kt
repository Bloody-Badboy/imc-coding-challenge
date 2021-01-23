package dev.arpan.imc.demo.location

import android.content.Context
import android.content.IntentSender
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import dev.arpan.imc.demo.utils.hasLocationPermission
import timber.log.Timber

class LocationRetriever(
    private val context: Context,
    private val onLocationResult: (result: Result) -> Unit,
    private val onResultResolutionRequired: (ResolvableApiException) -> Unit,
    private val onResultSettingsChangeUnavailable: () -> Unit
) {
    companion object {
        private const val UPDATE_INTERVAL: Long = 5 * 1000 /* 5 sec */
        private const val FASTEST_INTERVAL: Long = 2 * 1000 /* 2 sec */
    }

    sealed class Result {
        object RequestingLocationUpdate : Result()
        data class LocationUpdate(
            val latitude: Double,
            val longitude: Double
        ) : Result()

        object StoppedLocationUpdate : Result()
    }

    private val fusedLocationProviderClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }
    private val highAccuracyLocationRequest = LocationRequest().apply {
        interval = UPDATE_INTERVAL
        fastestInterval = FASTEST_INTERVAL
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }
    private var isRequestingLocationUpdates = false

    private var callback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult?.lastLocation?.let { location ->
                onLocationResult.invoke(
                    Result.LocationUpdate(
                        latitude = location.latitude,
                        longitude = location.longitude
                    )
                )
            }
        }
    }

    fun start() {
        if (isRequestingLocationUpdates) {
            Timber.d("Already requesting location update.")
            return
        }

        if (!context.hasLocationPermission) {
            Timber.e("Location permission not granted.")
            return
        }

        checkDeviceSettingsAndRequest()
    }

    fun stop() {
        if (!isRequestingLocationUpdates) return
        try {
            fusedLocationProviderClient.removeLocationUpdates(callback)
            onLocationResult.invoke(Result.StoppedLocationUpdate)
            isRequestingLocationUpdates = false
        } catch (e: SecurityException) {
            Timber.e(e)
        }
    }

    private fun checkDeviceSettingsAndRequest() {
        val locationSettingsRequest = LocationSettingsRequest.Builder()
            .addLocationRequest(highAccuracyLocationRequest)
            .setAlwaysShow(true)
            .build()
        LocationServices.getSettingsClient(context).checkLocationSettings(locationSettingsRequest)
            .addOnCompleteListener { task ->
                try {
                    task.getResult(ApiException::class.java)
                    try {
                        fusedLocationProviderClient.requestLocationUpdates(
                            highAccuracyLocationRequest,
                            callback,
                            null
                        )
                        onLocationResult.invoke(Result.RequestingLocationUpdate)
                        isRequestingLocationUpdates = true
                    } catch (e: SecurityException) {
                        Timber.e(e)
                    }
                } catch (e: ApiException) {
                    Timber.e("ApiException -> statusCode:${e.statusCode} message:${e.message}")
                    if (e.statusCode == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                        try {
                            onResultResolutionRequired.invoke(e as ResolvableApiException)
                        } catch (ignored: IntentSender.SendIntentException) {
                        } catch (ignored: ClassCastException) {
                        }
                    } else {
                        onResultSettingsChangeUnavailable.invoke()
                    }
                }
            }
    }
}
