package com.example.proyectodonapawfinal

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyectodonapawfinal.R
import com.example.proyectodonapawfinal.NeedModel

class NeedAdapter :
    ListAdapter<NeedModel, NeedAdapter.ViewHolder>(Companion) {

    companion object : DiffUtil.ItemCallback<NeedModel>() {
        override fun areItemsTheSame(oldItem: NeedModel, newItem: NeedModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: NeedModel, newItem: NeedModel): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.items_need, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data: NeedModel = getItem(position)
        holder.tvType.text = data.cloth
        holder.tvPet.text = data.pet
        Glide.with(holder.itemView.context).load(data.imgURL).centerCrop()
            .into(holder.ivImage)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivImage: ImageView = itemView.findViewById(R.id.ivImage)
        var tvType: TextView = itemView.findViewById(R.id.tvType)
        var tvPet: TextView = itemView.findViewById(R.id.tvPet)
    }
}
