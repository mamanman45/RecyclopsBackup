package com.example.recyclops.api

import com.example.recyclops.data.UserHistoryItem
import com.google.gson.annotations.SerializedName
import java.lang.Error

data class GetUserHistoryResponse(

    @field: SerializedName("userHistory")
    val userHistory: ArrayList<UserHistoryItem>,

    @field: SerializedName("error")
    val error: String?,

)
