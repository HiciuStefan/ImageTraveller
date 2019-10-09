package com.example.imagetraveller.imagetraveller.viewmodel

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imagetraveller.R
import com.example.imagetraveller.imagetraveller.ui.RecyclerViewPhotosAdapter
import com.squareup.picasso.Picasso


object ImageTravellerBindingAdapters {

    @JvmStatic
    @BindingAdapter("photos")
    fun setPhotos(recyclerView: RecyclerView, photos: List<String>) {
        if (recyclerView.adapter == null) {
            val adapter = RecyclerViewPhotosAdapter(photos)
            recyclerView.adapter = adapter
            recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL))
            recyclerView.layoutManager = LinearLayoutManager(recyclerView.context, RecyclerView.VERTICAL, false)
        } else {
            (recyclerView.adapter as RecyclerViewPhotosAdapter).setPhotos(photos.reversed())

//        (recyclerView.adapter as RecyclerViewPhotosAdapter).setPhotos( photos)
            recyclerView.adapter!!.notifyDataSetChanged()
        }
    }

    @JvmStatic
    @BindingAdapter("photoUrl")
    fun setPhotos(imageView: ImageView, photoUrl: String) {
        Picasso.get().load(photoUrl).placeholder(R.drawable.photo_placeholder).into(imageView)

    }
}