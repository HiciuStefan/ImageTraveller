package com.example.imagetraveller.di

import com.example.imagetraveller.imagetraveller.ui.ImageTravellerActivity
import com.example.imagetraveller.welcomescreen.StartActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AppModule::class,
        ViewModelModule::class,
        FlickrModule::class
    ]
)
interface AppComponent {

    fun inject(target: ImageTravellerActivity)
    fun inject(target: StartActivity)
}