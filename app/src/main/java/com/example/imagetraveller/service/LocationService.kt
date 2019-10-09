package com.example.imagetraveller.service

import android.app.Service
import android.content.Intent
import android.location.Location
import android.os.Binder
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.example.imagetraveller.imagetraveller.repository.PermissionsRepository
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices


class LocationService : Service() {

    lateinit var locationUpdate: LocationUpdate
    private val binder = LocalBinder()
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var previousLocation: Location? = null

    inner class LocalBinder : Binder() {
        fun getService(): LocationService = this@LocationService
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            smallestDisplacement = 50f
            interval = 3000
            fastestInterval = 3000
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult == null) {
                    return
                }
                for (location in locationResult.locations) {
                    if (location != null) {
                        if (previousLocation != null) {
                            Toast.makeText(applicationContext, location.distanceTo(previousLocation).toString(), Toast.LENGTH_LONG).show()
                            Log.d("NEW LOCATION", location.distanceTo(previousLocation).toString())
                        }
                        previousLocation = location
                        locationUpdate.onNewLocation(location)
                        break
                    }
                }
            }
        }
    }


    fun startLocationUpdates(locationUpdate: LocationUpdate) {
        this.locationUpdate = locationUpdate
        if (PermissionsRepository().checkLocationPermission(this.applicationContext)) {
            LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        }
    }

    interface LocationUpdate {
        fun onNewLocation(location: Location)
    }

}