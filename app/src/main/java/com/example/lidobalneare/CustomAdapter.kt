package com.example.lidobalneare

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lidobalneare.databinding.CardViewBinding

class CustomAdapter(private val mList: List<GestoreDati>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CardViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val gestoreDati = mList[position]

        holder.binding.textView.text = gestoreDati.text

        holder.itemView.setOnClickListener {
            onClickListener?.onClick(position, gestoreDati)
        }

    }

    inner class ViewHolder(val binding: CardViewBinding) : RecyclerView.ViewHolder(binding.root){
        val switch= binding.mySwitch

    }

    interface OnClickListener {
        fun onClick(position: Int, model: GestoreDati)
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }
}