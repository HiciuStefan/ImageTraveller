package com.example.imagetraveller.flickr

import com.flickr4java.flickr.Flickr
import com.flickr4java.flickr.photos.Photo
import com.flickr4java.flickr.photos.SearchParameters
import io.reactivex.Maybe
import javax.inject.Inject

class FlickrProvider @Inject constructor(private var flickr: Flickr) {


    fun getImage(lat: String, long: String): Maybe<Photo> {
        return Maybe.defer {
            var searchParams = SearchParameters().apply {
                longitude = long
                latitude = lat
            }
            val results = flickr.photosInterface.search(searchParams, 5, 1)
            when (results.isNullOrEmpty()) {
                true -> Maybe.empty()
                false -> Maybe.just(results[0])
            }
        }
    }
}