package com.example.imagetraveller.welcomescreen

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.imagetraveller.BuildConfig
import com.example.imagetraveller.ImageTravellerApplication
import com.example.imagetraveller.flickr.FlickrServiceProvider
import com.example.imagetraveller.imagetraveller.repository.PermissionsRepository
import com.example.imagetraveller.imagetraveller.ui.ImageTravellerActivity
import com.flickr4java.flickr.Flickr
import com.flickr4java.flickr.REST
import com.flickr4java.flickr.photos.SearchParameters
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class StartActivity : AppCompatActivity() {
    @Inject
    lateinit var permissionRepository: PermissionsRepository

    companion object {
        var LOCAITON_REQUEST_CODE: Int = 12345
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.imagetraveller.R.layout.activity_start)
        (application as ImageTravellerApplication).appComponent.inject(this)
          findViewById<Button>(com.example.imagetraveller.R.id.buttonStart).setOnClickListener {
              if (permissionRepository.checkLocationPermission(this)) {
                  goToImageTraveller()
              } else {
                  permissionRepository.requestLocationPermission(this)
              }
          }
    }

    private fun goToImageTraveller() {
        startActivity(Intent(this, ImageTravellerActivity::class.java))
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            LOCAITON_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] === PackageManager.PERMISSION_GRANTED) {
                    goToImageTraveller()
                } else {
                    Toast.makeText(
                        this,
                        "Unfortunately witouth location access this app does not work",
                        Toast.LENGTH_LONG
                    ).show()
                }
                return
            }
        }
    }
}
