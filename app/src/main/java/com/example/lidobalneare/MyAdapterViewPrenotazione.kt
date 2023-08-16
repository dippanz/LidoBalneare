package com.example.lidobalneare

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lidobalneare.databinding.CardViewCamereBinding

class MyAdapterViewPrenotazione(private val serviceList: List<ViewModelHomePage>): RecyclerView.Adapter<MyAdapterViewPrenotazione.ViewHolder>() {

    inner class ViewHolder(binding: CardViewCamereBinding): RecyclerView.ViewHolder(binding.root){
       val image = binding.imageCamera
       val title = binding.titleCamera
       val desc = binding.descCamera
       val radioButton = binding.radioButton
    }

    private var selectedItemPosition: Int = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(CardViewCamereBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return serviceList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = serviceList[position]

        //setto ogni cardview
        holder.image.setImageResource(item.image)
        holder.title.text = item.title
        holder.desc.text = item.desc

        holder.radioButton.isChecked = (selectedItemPosition == holder.adapterPosition)
        holder.radioButton.setOnClickListener {
            if (selectedItemPosition != holder.adapterPosition) {
                val previousSelectedPosition = selectedItemPosition
                selectedItemPosition = holder.adapterPosition
                notifyItemChanged(previousSelectedPosition)
                notifyItemChanged(holder.adapterPosition)
            }
        }

    }


/**
 * restituisce l'oggetto selezionato sotto forma di ViewModel
 * */
    fun getSelectedItem(): ViewModelHomePage? {
        return if (selectedItemPosition != RecyclerView.NO_POSITION) {
            serviceList[selectedItemPosition]
        } else {
            null
        }
    }

}