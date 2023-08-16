package com.example.lidobalneare

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lidobalneare.databinding.ServiziHomeCardViewBinding


class MyAdapterHomePage(private val serviceList: List<ViewModelHomePage>): RecyclerView.Adapter<MyAdapterHomePage.ViewHolder>() {
    class ViewHolder(binding: ServiziHomeCardViewBinding): RecyclerView.ViewHolder(binding.root){
        val image = binding.imageCardView
        val title = binding.titleCardView
        val desc = binding.descCardView

    }

    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ServiziHomeCardViewBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return serviceList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = serviceList[position]

        //setto vari dati
        holder.image.setImageResource(item.image)
        holder.title.text = item.title
        holder.desc.text = item.desc

        //setto il listener per ogni cardview
        holder.itemView.setOnClickListener{
            onClickListener?.onClick(position, item)
        }
    }

    interface OnClickListener{
        fun onClick(position: Int, model: ViewModelHomePage)
    }

    fun setOnClickListener(l: OnClickListener){
        this.onClickListener = l
    }
}