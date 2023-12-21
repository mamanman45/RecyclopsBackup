package com.example.recyclops.ui.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.recyclops.data.TrashScanned

class TrashScannedDiffCallback : DiffUtil.ItemCallback<TrashScanned>() {
    override fun areItemsTheSame(oldItem: TrashScanned, newItem: TrashScanned): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TrashScanned, newItem: TrashScanned): Boolean {
        return oldItem == newItem
    }
}
