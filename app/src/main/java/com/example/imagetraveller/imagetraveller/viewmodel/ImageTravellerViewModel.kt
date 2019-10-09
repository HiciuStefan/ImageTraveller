package com.example.imagetraveller.imagetraveller.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import com.example.imagetraveller.flickr.PhotosServiceProvider
import javax.inject.Inject

class ImageTravellerViewModel @Inject constructor(
    var photosServiceProvider: PhotosServiceProvider,
    var context: Context
) : ViewModel() {

    var currentPhotos: ObservableArrayList<String> = ObservableArrayList()
    var subscribed: ObservableBoolean = ObservableBoolean(false)

    fun onResume() {
        updatePhotos(photosServiceProvider.getCurrentPhotos())
        if (!subscribed.get()) {
            startPhotoStream()
        }
    }

    fun onPause() {
        if (subscribed.get()) {
            stopPhotoStream()
        }
    }

    fun updatePhotoStream() {
        if (subscribed.get()) {
            stopPhotoStream()
            Toast.makeText(context, "Photo stream has now stopped", Toast.LENGTH_LONG).show()
        } else {
            startPhotoStream()
            Toast.makeText(context, "Photo stream has now started", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCleared() {
        super.onCleared()
        photosServiceProvider.stopPhotoService()
    }

    private fun stopPhotoStream() {
        photosServiceProvider.unsubscribePhotoStream()
        subscribed.set(false)
    }

    private fun startPhotoStream() {
        photosServiceProvider.subscribePhotoStream(object : PhotosServiceProvider.PhotosStream {
            override fun onPhotosUpdate(photos: List<String>) {
                updatePhotos(photos)
            }
        })
        subscribed.set(true)
    }

    private fun updatePhotos(photos: List<String>) {
        currentPhotos.clear()
        currentPhotos.addAll(photos)
    }


}