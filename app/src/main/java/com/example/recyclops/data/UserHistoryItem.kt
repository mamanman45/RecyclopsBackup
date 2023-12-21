package com.example.recyclops.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


data class UserHistoryItem(
    @field: SerializedName("imageUrl")
    val imageUrl : String? ,

    @field: SerializedName("wasteType")
    val wasteType : String? ,

    @field: SerializedName("weight")
    val weight : Int?,

    @field: SerializedName("points")
    val points : Int?
)

