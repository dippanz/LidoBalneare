package com.example.lidobalneare

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lidobalneare.databinding.CardViewNotificaBinding


class AdapterNotifica(private val mList: MutableList<ModelNotifica>) : RecyclerView.Adapter<AdapterNotifica.ViewHolder>() {



    inner class ViewHolder(val binding: CardViewNotificaBinding) : RecyclerView.ViewHolder(binding.root) {
        val descrizione = binding.descNotifica
        val titolo = binding.titleNotifica
        val buttonCancella = binding.buttonCancella
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterNotifica.ViewHolder {
        val binding = CardViewNotificaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: AdapterNotifica.ViewHolder, position: Int) {
        val modelNotifica = mList[position]

        holder.titolo.text = modelNotifica.title
        holder.descrizione.text = modelNotifica.desc
        holder.buttonCancella.setOnClickListener {
            // Rimuovi l'elemento dalla lista e dal database
            val removedItem = mList.removeAt(position)
            DBMSboundary().removeNotifica(removedItem.id)

            // Notifica all'adapter dei cambiamenti
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, mList.size)
        }


    }


}