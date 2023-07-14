package com.example.fetchadds

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class AdsAdapterClass(
    private var itemsList: List<Items?>?,
    private val favoriteClickListener: ((Int) -> Unit?)?
) : RecyclerView.Adapter<AdsAdapterClass.ViewHolder>() {
    
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val photoImageView: ImageView = itemView.findViewById(R.id.photoImageView)
        private val priceTextView: TextView = itemView.findViewById(R.id.priceTextView)
        private val locationTextView: TextView = itemView.findViewById(R.id.locationTextView)
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private val favoriteButton: ImageButton = itemView.findViewById(R.id.favoriteButton)
        
        fun bind(items: Items) {
            Glide.with(itemView)
                .load(items.imageUrl)
                .into(photoImageView)
            
            priceTextView.text = items.price.toString()
            locationTextView.text = items.location
            titleTextView.text = items.id
            
            favoriteButton.setImageResource(if (items.favourite?.isFavorite == true) R.drawable.baseline_favorite else R.drawable.baseline_favorite_border)
            favoriteButton.setOnClickListener { favoriteClickListener?.invoke(bindingAdapterPosition) }
            
        }
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.ads_item, parent, false)
        return ViewHolder(itemView)
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ad = itemsList?.get(position)
        if (ad != null) {
            holder.bind(ad)
        }
    }
    
    override fun getItemCount(): Int {
        return itemsList?.size ?: 0
    }
}


