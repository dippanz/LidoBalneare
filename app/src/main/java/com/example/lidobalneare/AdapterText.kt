package com.example.lidobalneare

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lidobalneare.databinding.CardViewTextBinding
import java.text.SimpleDateFormat
import java.util.Locale

class AdapterText(private val mList: List<GestoreDatiTesto>) : RecyclerView.Adapter<AdapterText.ViewHolder>() {

    private val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CardViewTextBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val gestoreDatiTesto = mList[position]
        holder.nome.text = gestoreDatiTesto.nome
        holder.cognome.text = gestoreDatiTesto.cognome
        holder.dataInizio.text = formato.format(gestoreDatiTesto.dataInizio)
        holder.dataFine.text = formato.format(gestoreDatiTesto.dataFine)
        holder.camera.text = gestoreDatiTesto.camera.toString()  // Conversione da Int a String
    }

    inner class ViewHolder(val binding: CardViewTextBinding) : RecyclerView.ViewHolder(binding.root) {
        val nome = binding.nome
        val cognome = binding.cognome
        val dataInizio = binding.dataInizio
        val dataFine = binding.dataFine
        val camera = binding.camera
    }
}
