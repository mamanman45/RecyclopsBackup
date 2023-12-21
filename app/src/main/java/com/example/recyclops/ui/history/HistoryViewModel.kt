package com.example.recyclops.ui.history

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recyclops.api.ApiConfig
import com.example.recyclops.api.GetUserHistoryResponse
import com.example.recyclops.data.UserHistoryItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryViewModel : ViewModel() {

    private val _historyTrash = MutableLiveData<ArrayList<UserHistoryItem>>()
    val historyTrash
        get() = _historyTrash

    fun setUserHistory(token: String) {
        val apiService = ApiConfig().getApiService()
        val uploadImageRequest = apiService.getUserHistory(token)
        uploadImageRequest.enqueue(object : Callback<GetUserHistoryResponse> {
            override fun onResponse(
                call: Call<GetUserHistoryResponse>,
                response: Response<GetUserHistoryResponse>
            ) {
                val responseBody = response.body()
                if (response.isSuccessful) {
                    if (responseBody != null) {
                        _historyTrash.value = responseBody.userHistory
                    }
                }
            }

            override fun onFailure(call: Call<GetUserHistoryResponse>, t: Throwable) {
                Log.d("Failure", t.message.toString())
            }
        })
    }
}