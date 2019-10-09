package com.example.imagetraveller.imagetraveller.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.imagetraveller.ImageTravellerApplication
import com.example.imagetraveller.R
import com.example.imagetraveller.databinding.ActivityImageTravellerBinding
import com.example.imagetraveller.di.ViewModelFactory
import com.example.imagetraveller.imagetraveller.viewmodel.ImageTravellerViewModel
import javax.inject.Inject

class ImageTravellerActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var model: ImageTravellerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding: ActivityImageTravellerBinding = DataBindingUtil.setContentView(this, R.layout.activity_image_traveller)
        setSupportActionBar(binding.toolbar)
        (application as ImageTravellerApplication).appComponent.inject(this)
        model = ViewModelProviders.of(this, viewModelFactory)[ImageTravellerViewModel::class.java]
        binding.viewModel = model
    }

    override fun onResume() {
        super.onResume()
        model.onResume()
    }

    override fun onPause() {
        super.onPause()
        model.onPause()
    }
}
