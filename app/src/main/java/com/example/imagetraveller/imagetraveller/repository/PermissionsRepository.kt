package com.example.imagetraveller.imagetraveller.repository

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.example.imagetraveller.welcomescreen.StartActivity
import javax.inject.Inject

class PermissionsRepository @Inject constructor() {

    fun checkLocationPermission(context: Context): Boolean {
        val permission = Manifest.permission.ACCESS_FINE_LOCATION
        val permission2 = Manifest.permission.ACCESS_COARSE_LOCATION
        val res = context.checkCallingOrSelfPermission(permission)
        val res2 = context.checkCallingOrSelfPermission(permission2)
        return res == PackageManager.PERMISSION_GRANTED && res2 == PackageManager.PERMISSION_GRANTED
    }

    fun requestLocationPermission(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
            StartActivity.LOCAITON_REQUEST_CODE
        )
    }
}