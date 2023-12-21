package com.example.recyclops.data

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.parcelize.Parcelize

@Parcelize
data class TrashBankLocation(
    val name : String,
    val latLng : LatLng,
): Parcelable

@Parcelize
data class LatLng(
    val latitude: Double,
    val longitude: Double,
): Parcelable
