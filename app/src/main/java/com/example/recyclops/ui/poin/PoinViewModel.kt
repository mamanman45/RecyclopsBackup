package com.example.recyclops.ui.poin

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recyclops.api.ApiConfig
import com.example.recyclops.api.GetUserPointResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PoinViewModel : ViewModel() {

    private val _point = MutableLiveData<GetUserPointResponse?>()
    val point
        get() = _point

    fun getUserPoint(token: String) {
        val apiService = ApiConfig().getApiService()
        val uploadImageRequest = apiService.getUserPoint(token)
        uploadImageRequest.enqueue(object : Callback<GetUserPointResponse> {
            override fun onResponse(
                call: Call<GetUserPointResponse>,
                response: Response<GetUserPointResponse>
            ) {
                val responseBody = response.body()
                if (response.isSuccessful) {
                    if (responseBody != null) {
                        _point.value = responseBody
                    }
                }
            }

            override fun onFailure(call: Call<GetUserPointResponse>, t: Throwable) {
                Log.d("Failure", t.message.toString())
            }
        })
    }
}