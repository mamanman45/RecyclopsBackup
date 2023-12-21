package com.example.recyclops.ui.imageconfirmation

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recyclops.api.ApiConfig
import com.example.recyclops.api.FileUploadResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ImageConfirmationViewModel : ViewModel() {

    private val _scannedImage = MutableLiveData<FileUploadResponse?>()
    val scannedImage get() = _scannedImage

    fun uploadImage(
        token: String,
        imageMultipartBody: MultipartBody.Part,
    ) {
        val apiService = ApiConfig().getApiService()
        val uploadImageRequest = apiService.uploadImage(token, imageMultipartBody)

        uploadImageRequest.enqueue(object : Callback<FileUploadResponse> {
            override fun onResponse(
                call: Call<FileUploadResponse>,
                response: Response<FileUploadResponse>
            ) {
                val responseBody = response.body()
                if (response.isSuccessful) {
                    if (responseBody != null) {
                        Log.d("Success", responseBody.error.toString())
                        Log.d("uniqueId", responseBody.imageUrl.toString())
                        Log.d("WasteType", responseBody.wasteType.toString())
                        Log.d("Confidence", responseBody.confidence.toString())
                        //ResponseInterface.fileUploadResponseAdded(responseBody)
                        scannedImage.value = responseBody
                    }
                } else {
                    Log.d("Failure", responseBody?.error.toString())
                }
            }

            override fun onFailure(call: Call<FileUploadResponse>, t: Throwable) {
                Log.d("Failure", t.message.toString())
            }
        })
    }
}