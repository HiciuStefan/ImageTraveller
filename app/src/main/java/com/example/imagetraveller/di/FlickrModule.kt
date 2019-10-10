package com.example.imagetraveller.di

import com.example.imagetraveller.BuildConfig
import com.flickr4java.flickr.Flickr
import com.flickr4java.flickr.REST
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class FlickrModule {

    @Provides
    @Singleton
    fun provideFlickr(): Flickr {
        return Flickr(BuildConfig.FLICKR_KEY, BuildConfig.FLICKR_SECRET, REST())
    }
}