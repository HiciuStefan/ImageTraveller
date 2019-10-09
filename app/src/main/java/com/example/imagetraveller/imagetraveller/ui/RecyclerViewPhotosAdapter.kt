package com.example.imagetraveller.imagetraveller.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.imagetraveller.R
import com.example.imagetraveller.databinding.PhotoItemBinding


class RecyclerViewPhotosAdapter constructor(var photoList: List<String>) : RecyclerView.Adapter<RecyclerViewPhotosAdapter.PhotoView>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewPhotosAdapter.PhotoView {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: PhotoItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.photo_item, parent, false)
        return PhotoView(binding)
    }

    override fun getItemCount() = photoList.size


    override fun onBindViewHolder(holder: RecyclerViewPhotosAdapter.PhotoView, position: Int) {
        holder.setBinding(photoList[position])
    }

    fun setPhotos(photos: List<String>) {
        photoList = photos
    }

    class PhotoView(private val binding: PhotoItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun setBinding(photoUrl: String) {
            binding.photoUrl = photoUrl
        }
    }
}



