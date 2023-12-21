package com.example.recyclops.ui.camera

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recyclops.api.ApiConfig
import com.example.recyclops.api.UploadImageConfirmedResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("NullSafeMutableLiveData")
class CameraPreviewViewModel () : ViewModel() {
    val scannedImage2 = MutableLiveData<UploadImageConfirmedResponse?>()

    fun uploadImageConfirmation(token: String, wasteType: String, weight: Int, imageUrl: String, confidence: Float){
        val apiService = ApiConfig().getApiService()
        val uploadImageRequest = apiService.uploadImageConfirmed(token,wasteType,weight,imageUrl,confidence)
        uploadImageRequest.enqueue(object : Callback<UploadImageConfirmedResponse>{
            override fun onResponse(
                call: Call<UploadImageConfirmedResponse>,
                response: Response<UploadImageConfirmedResponse>
            ) {
                val responseBody = response.body()
                if (response.isSuccessful){
                    if (responseBody != null)
                        scannedImage2.postValue(responseBody)
                }
            }
            override fun onFailure(call: Call<UploadImageConfirmedResponse>, t: Throwable) {
                Log.d("Failure", t.message.toString())
            }
        })
    }
}