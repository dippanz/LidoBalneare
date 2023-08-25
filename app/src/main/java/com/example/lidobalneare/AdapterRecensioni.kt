package com.example.lidobalneare

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lidobalneare.databinding.CardViewRecensioniBinding



class AdapterRecensioni(val context: Context, private val mList: List<ModelRecensioni>) : RecyclerView.Adapter<AdapterRecensioni.ViewHolder>() {

    inner class ViewHolder(val binding: CardViewRecensioniBinding) : RecyclerView.ViewHolder(binding.root) {
        val title = binding.titoloRecensione
        val desc = binding.textRecensione
        val nomeP = binding.nomePublicatore
        val rateBar = binding.ratingBarRecensioniFatte
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CardViewRecensioniBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val modelRecensioni = mList[position]

        holder.title.text = modelRecensioni.titolo
        holder.desc.text = modelRecensioni.desc
        holder.nomeP.text = context.getString(R.string.pubblicata_da_1s, modelRecensioni.nomeP)
        holder.rateBar.rating = modelRecensioni.valutazione
    }


}