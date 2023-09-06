package com.example.lidobalneare

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lidobalneare.databinding.ItemImageHomeBinding

class ImageHomeAdapter(private val images: MutableList<Bitmap>) : RecyclerView.Adapter<ImageHomeAdapter.ViewHolder>() {


    fun addImage(bitmap: Bitmap) {
        images.add(bitmap)
        notifyItemInserted(images.size - 1) // Notifica l'adapter dell'inserimento dell'immagine
    }

    inner class ViewHolder(binding: ItemImageHomeBinding): RecyclerView.ViewHolder(binding.root){
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
        holder.image.setImageBitmap(item)
    }
}
