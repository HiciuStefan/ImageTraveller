package com.example.imagetraveller.flickr

import com.example.imagetraveller.BuildConfig
import com.flickr4java.flickr.Flickr
import com.flickr4java.flickr.REST
import com.flickr4java.flickr.photos.Photo
import com.flickr4java.flickr.photos.SearchParameters
import io.reactivex.Maybe
import java.util.*
import javax.inject.Inject

class FlickrServiceProvider @Inject constructor() {

    var flickr = Flickr(BuildConfig.FLICKR_KEY, BuildConfig.FLICKR_SECRET, REST())

    fun getImage(lat: String, long: String): Maybe<Photo> {
        return Maybe.defer {
            var searchParams = SearchParameters().apply {
                longitude = long
                latitude = lat
            }
            var results = flickr.photosInterface.search(searchParams, 5, 1)
            if (results.isNullOrEmpty()) {
                Maybe.empty()
            } else {
                Maybe.just(results[0])
            }
        }
    }
}