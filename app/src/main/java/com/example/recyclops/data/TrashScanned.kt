package com.example.recyclops.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TrashScanned (
    var id : Int,
    var name : String,
    var quantity : Int,
    var point : Int,
    var imageSampah : Int
) : Parcelable