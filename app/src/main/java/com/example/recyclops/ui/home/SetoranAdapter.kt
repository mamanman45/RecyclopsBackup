package com.example.recyclops.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.recyclops.data.UserHistoryItem
import com.example.recyclops.databinding.ItemSetoranHomeBinding

class SetoranAdapter : RecyclerView.Adapter<SetoranAdapter.UserViewHolder>() {

    private val list = ArrayList<UserHistoryItem>()

    fun setList(userHistoryItem: List<UserHistoryItem>) {
        list.clear()
        list.addAll(userHistoryItem)
        notifyItemChanged(userHistoryItem.size)
    }

    inner class UserViewHolder(private val binding: ItemSetoranHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(setoran: UserHistoryItem) {
            binding.apply {
                val berat = setoran.weight
                val quantity = "$berat KG"
                val point = setoran.points.toString()
                val _point = "$point Point"
                Glide.with(itemView)
                    .load(setoran.imageUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .centerCrop()
                    .into(imgItemSetoran)
                tvItemNama.text = setoran.wasteType
                tvItemQuantity.text = quantity
                tvItemPoin.text = _point
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view =
            ItemSetoranHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size
}