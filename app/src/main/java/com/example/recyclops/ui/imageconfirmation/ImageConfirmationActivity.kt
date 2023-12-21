package com.example.recyclops.ui.imageconfirmation

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.recyclops.databinding.ActivityImageConfirmationBinding
import com.example.recyclops.ui.camera.CameraActivity
import com.example.recyclops.ui.camera.CameraPreviewActivity
import com.example.recyclops.ui.utils.reduceFileImage
import com.example.recyclops.ui.utils.rotateFile
import com.google.firebase.auth.FirebaseAuth
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


class ImageConfirmationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityImageConfirmationBinding
    private lateinit var viewModel: ImageConfirmationViewModel
    private var myFile: File? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageConfirmationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[ImageConfirmationViewModel::class.java]

        showLoading(true)
        getPicture()
        showLoading(false)

        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        binding.container.isEnabled = false

        binding.btnCancel.setOnClickListener { cancel() }
        binding.btnConfirm.setOnClickListener {
            showLoading(true)
            if (myFile != null) {
                val file = reduceFileImage(myFile as File)
                val intent = Intent(this, CameraPreviewActivity::class.java)
                val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "image",
                    file.name,
                    requestImageFile
                )
                val mUser = FirebaseAuth.getInstance().currentUser
                mUser!!.getIdToken(true)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val idToken: String? = task.result.token
                            viewModel.uploadImage("Bearer $idToken", imageMultipart)
                            Log.d("token", idToken.toString())
                        } else {
                            showLoading(false)
                            Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT)
                                .show()
                        }
                    }

                viewModel.scannedImage.observe(this) {
                    if (it != null) {
                        intent.putExtra("imageUrl", it.imageUrl)
                        intent.putExtra("uniqueId", it.uniqueId)
                        intent.putExtra("wasteType", it.wasteType)
                        intent.putExtra("confidence", it.confidence.toString())
                        showLoading(false)
                        startActivity(intent)
                    } else {
                        showLoading(false)
                        Toast.makeText(this, "Gagal", Toast.LENGTH_SHORT).show()
                    }
                }

            } else {
                showLoading(false)
                Toast.makeText(this, "Silahkan Foto Sampah Terlebih Dahulu", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun showLoading(state: Boolean) {
        binding.container.isRefreshing = state
    }

    private fun getPicture() {
        myFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("picture", File::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("picture")
        } as? File
        val isBackCamera = intent.getBooleanExtra("isBackCamera", true)
        val isGallery = intent.getBooleanExtra("isGallery", false)

        myFile?.let { file ->
            if (!isGallery) {
                rotateFile(file, isBackCamera)
                binding.ivTrashPhoto.setImageURI(Uri.fromFile(file))
            } else {
                binding.ivTrashPhoto.setImageURI(Uri.fromFile(file))
            }
        }
    }

    private fun cancel() {
        val intent = Intent(this, CameraActivity::class.java)
        startActivity(intent)
        finish()
    }
}