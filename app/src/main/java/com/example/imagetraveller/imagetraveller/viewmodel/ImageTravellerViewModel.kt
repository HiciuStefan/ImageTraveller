package com.example.imagetraveller.imagetraveller.viewmodel

import android.content.Context
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import com.example.imagetraveller.flickr.PhotosProvider
import javax.inject.Inject

class ImageTravellerViewModel @Inject constructor(
    private var photosProvider: PhotosProvider,
    private var context: Context
) : ViewModel() {

    var currentPhotos: ObservableArrayList<String> = ObservableArrayList()
    var subscribed: ObservableBoolean = ObservableBoolean(false)

    fun onResume() {
        updatePhotos(photosProvider.getCurrentPhotos())
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
        } else {
            startPhotoStream()
        }
    }

    public override fun onCleared() {
        super.onCleared()
        photosProvider.stopPhotoService()
    }

    private fun stopPhotoStream() {
        photosProvider.unsubscribePhotoStream()
        subscribed.set(false)
    }

    private fun startPhotoStream() {
        photosProvider.subscribePhotoStream(getStreamHandler())
        subscribed.set(true)
    }

    private fun getStreamHandler(): PhotosProvider.PhotosStream {
        return object : PhotosProvider.PhotosStream {
            override fun onPhotosUpdate(photos: List<String>) {
                updatePhotos(photos)
            }
        }
    }

    private fun updatePhotos(photos: List<String>) {
        currentPhotos.clear()
        currentPhotos.addAll(photos)
    }


}