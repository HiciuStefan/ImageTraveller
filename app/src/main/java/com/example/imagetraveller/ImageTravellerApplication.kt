package com.example.imagetraveller

import android.app.Application
import com.example.imagetraveller.di.AppComponent
import com.example.imagetraveller.di.AppModule
import com.example.imagetraveller.di.DaggerAppComponent

class ImageTravellerApplication : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }
}