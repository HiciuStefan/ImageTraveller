package com.example.imagetraveller.flickr

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.location.Location
import android.os.IBinder
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.imagetraveller.service.LocationService
import com.flickr4java.flickr.photos.Photo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

class PhotosServiceProvider @Inject constructor(
    private var flickrServiceProvider: FlickrServiceProvider,
    private var context: Context
) {

    private var mBound: Boolean = false
    private var photoStream: PhotosStream? = null
    private var photoList: MutableList<String> = mutableListOf()
    private var locationUpdate: LocationService.LocationUpdate
    private lateinit var locationService: LocationService

    init {
        locationUpdate = object : LocationService.LocationUpdate {
            override fun onNewLocation(location: Location) {
                flickrServiceProvider.getImage(location.latitude.toString(), location.longitude.toString())
                    .subscribeOn(Schedulers.io())
                    .subscribeBy(
                        onSuccess = {
                            photoList.add(getPhotoUrl(it))
                            photoStream?.onPhotosUpdate(photoList)
                        },
                        onError = { Log.e("LocationDbService", it.toString()) }
                    )
            }
        }
    }

    fun getCurrentPhotos(): MutableList<String> {
        return photoList
    }

    fun subscribePhotoStream(photosStream: PhotosStream) {
        if (!mBound) {
            ContextCompat.startForegroundService(context, Intent(context, LocationService::class.java).also { intent ->
                context.bindService(intent, connection, Context.BIND_AUTO_CREATE)
            })
        }
        this.photoStream = photosStream
    }

    private fun getPhotoUrl(it: Photo): String {
        var formattedUrl = String.format(
            Locale.US,
            "http://farm%s.staticflickr.com/%s/%s_%s.jpg",
            it.farm,
            it.server,
            it.id,
            it.secret
        )
        return formattedUrl
    }

    fun unsubscribePhotoStream() {
        this.photoStream = null
    }

    fun stopPhotoService() {
        this.photoStream = null
        context.unbindService(connection)
        mBound = false
        val myService = Intent(context, LocationService::class.java)
        context.stopService(myService)
    }

    interface PhotosStream {
        fun onPhotosUpdate(photos: List<String>)
    }

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as LocationService.LocalBinder
            locationService = binder.getService()
            locationService.startLocationUpdates(locationUpdate)
            mBound = true
        }
        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound = false
        }
    }
}


