package com.example.movies_task30.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.movies_task30.R
import com.example.movies_task30.databinding.ContianerSliderImageBinding


class ImageSliderAdapter(private val images: List<String>) :
    RecyclerView.Adapter<ImageSliderAdapter.ImageSliderViewHolder>() {
    private var layoutInflater: LayoutInflater? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageSliderViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val binding: ContianerSliderImageBinding = DataBindingUtil.inflate(
            layoutInflater as LayoutInflater, R.layout.contianer_slider_image, parent, false
        )
        return ImageSliderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageSliderViewHolder, position: Int) {
        holder.binding.setImageURL(images[position])
    }

    override fun getItemCount(): Int {
        return images.size
    }

    class ImageSliderViewHolder(binding: ContianerSliderImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var binding: ContianerSliderImageBinding

        init {
            this.binding = binding
        }

        fun sliderImageView(imageURL: String?) {
            binding.setImageURL(imageURL)
        }
    }
}