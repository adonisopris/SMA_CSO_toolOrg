package ro.upt.ac.tooler.location

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

@SuppressLint("MissingPermission")
class LocationHandler(context: Context) {

        private val client: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(context)

        fun registerLocationListener(locationCallback: LocationCallback) {
            val locationRequest = LocationRequest.Builder(100)
                .setMinUpdateDistanceMeters(5F)
                .setPriority(Priority.PRIORITY_LOW_POWER)
                .build()

            client.requestLocationUpdates(locationRequest,locationCallback, Looper.getMainLooper())

        }

        fun unregisterLocationListener(locationCallback: LocationCallback) {
            client.removeLocationUpdates(locationCallback)
        }

}