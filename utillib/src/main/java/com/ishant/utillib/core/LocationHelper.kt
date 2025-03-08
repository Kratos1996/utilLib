package com.ishant.utillib.core


import android.Manifest
import android.app.Activity
import android.location.Location
import android.widget.Toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationHelper @Inject constructor(private val context: Activity) {

    private val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 100
    }

    private fun checkAndRequestLocationPermission(
        onPermissionGranted: () -> Unit,
        onPermissionDenied: () -> Unit
    ) {
        if (PermissionUtil.CheckPermission.hasFineLocationPermission(context)) {
            onPermissionGranted()
        } else {
            if (PermissionUtil.shouldShowRationale(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                // Show rationale to the user if needed
                onPermissionDenied()
            } else {
                // Request permission
                PermissionUtil.requestPermission(
                    context = context,
                    permission = Manifest.permission.ACCESS_FINE_LOCATION,
                    requestCode = LOCATION_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    fun getCurrentLocation(
        onLocationRetrieved: (Location?) -> Unit,
        onLocationError: (message: String) -> Unit,
        failedMessageAllowed: Boolean = false,
        failedMessageToastAllowed: Boolean = false,
        onFailCustomMessage: String = "Failed to get location",
        onPermissionDenied: () -> Unit
    ) {
        checkAndRequestLocationPermission(
            onPermissionGranted = {
                fusedLocationClient.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    null
                ).addOnSuccessListener { location ->
                    onLocationRetrieved(location)
                }.addOnFailureListener {
                    onLocationError(
                        if (failedMessageAllowed) onFailCustomMessage else it.message
                            ?: onFailCustomMessage
                    )
                    if (failedMessageToastAllowed) {
                        Toast.makeText(
                            context,
                            it.message ?: "Failed to get location",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            },
            onPermissionDenied = {
                onPermissionDenied()
            }
        )
    }
}