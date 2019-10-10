package com.example.imagetraveller.service

import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.location.Location
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import com.example.imagetraveller.imagetraveller.repository.PermissionsRepository
import com.example.imagetraveller.imagetraveller.ui.ImageTravellerActivity
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices


class LocationService : Service() {
    val CHANNEL_ID = "ForegroundServiceChannel"
    val HUNDRED_METERS = 100
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
            smallestDisplacement = 100f
            interval = 5000
            fastestInterval = 5000
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult == null) {
                    return
                }
                locationResult.lastLocation?.let {
                    if (distance100Meter(it)) {
                        locationUpdate.onNewLocation(it)
                        previousLocation = it
                    }
                }
            }
        }
    }

    private fun distance100Meter(location: Location): Boolean {
        return previousLocation?.let { location.distanceTo(it) >= HUNDRED_METERS } ?: true
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        val notificationIntent = Intent(this, ImageTravellerActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)
        createNotificationChannel()
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("We are tracking your location to get the photos")
            .setSmallIcon(R.drawable.sym_def_app_icon)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(1, notification)
        return START_NOT_STICKY
    }

    fun startLocationUpdates(locationUpdate: LocationUpdate) {
        this.locationUpdate = locationUpdate
        if (PermissionsRepository().checkLocationPermission(this.applicationContext)) {
            LocationServices.getFusedLocationProviderClient(this)
                .requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        }
    }

    interface LocationUpdate {
        fun onNewLocation(location: Location)
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )

            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }

    override fun onDestroy() {
        LocationServices.getFusedLocationProviderClient(this).removeLocationUpdates(locationCallback)
        stopForeground(true)
        stopSelf()
        super.onDestroy()
    }
}