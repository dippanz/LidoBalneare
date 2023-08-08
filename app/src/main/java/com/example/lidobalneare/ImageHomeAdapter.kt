package com.example.lidobalneare

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lidobalneare.databinding.ItemImageHomeBinding

class ImageHomeAdapter(private val images: List<Int>) : RecyclerView.Adapter<ImageHomeAdapter.ViewHolder>() {
    class ViewHolder(binding: ItemImageHomeBinding): RecyclerView.ViewHolder(binding.root){
        val image = binding.imageViewItemHome
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemImageHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return images.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = images[position]
        holder.image.setImageResource(item)
    }
}
