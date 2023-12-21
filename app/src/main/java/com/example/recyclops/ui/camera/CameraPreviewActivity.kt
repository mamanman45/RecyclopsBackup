package com.example.recyclops.ui.camera

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.text.LineBreaker.JUSTIFICATION_MODE_INTER_WORD
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Window
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.recyclops.databinding.ActivityCameraPreviewBinding
import com.example.recyclops.databinding.LayoutTriviaBinding
import com.example.recyclops.databinding.LayoutYesnoDialogBinding
import com.example.recyclops.ui.main.MainActivity
import com.example.recyclops.ui.utils.getTrivia
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import kotlin.math.roundToInt

class CameraPreviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraPreviewBinding
    private lateinit var bindingDialog: LayoutYesnoDialogBinding
    private lateinit var bindingBottomSheetDialog: LayoutTriviaBinding
    private lateinit var wasteType: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraPreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel = ViewModelProvider(this)[CameraPreviewViewModel::class.java]

        val imageUrl = intent.getStringExtra("imageUrl").toString()
        wasteType = intent.getStringExtra("wasteType").toString()
        val confidence = intent.getStringExtra("confidence").toString().toFloat()
        val confidencePercentage = (confidence * 100).roundToInt()


        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@CameraPreviewActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        })

        binding.btnPreviewSend.setOnClickListener {
            if (binding.tfPreviewWeight.text!!.isEmpty()) {

                binding.tfPreviewWeight.error = "Silahkan Masukkan Berat Sampah Terlebih Dahulu"
                binding.tfPreviewWeight.requestFocus()

            } else if (binding.tfPreviewWeight.text!!.toString().toInt() <= 0) {

                binding.tfPreviewWeight.error = "Berat Tidak Boleh Kurang Dari Sama Dengan 0"
                binding.tfPreviewWeight.requestFocus()

            } else {
                showLoading(true)
                val weight = binding.tfPreviewWeight.text.toString().toInt()
                val mUser = FirebaseAuth.getInstance().currentUser
                mUser!!.getIdToken(true)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            showLoading(false)
                            val idToken: String? = task.result.token
                            val status = "Berhasil"
                            val message = "Scan Lagi ?"
                            viewModel.uploadImageConfirmation(
                                "Bearer $idToken",
                                wasteType,
                                weight,
                                imageUrl,
                                confidence
                            )
                            showYesNoDialog(status, message)
                            Log.d("upload", "$idToken,$wasteType,$weight")
                            Log.d("token", idToken.toString())
                        } else {
                            showLoading(false)
                            val status = "Gagal ${task.exception}"
                            val message = "Scan Lagi ?"
                            showYesNoDialog(status, message)
                            Log.d("Exception", task.exception.toString())
                        }
                    }
            }
        }

        binding.btnTrivia.setOnClickListener {
            showTrivia(wasteType)
        }

        binding.apply {
            Glide.with(this@CameraPreviewActivity)
                .load(imageUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .centerCrop()
                .into(ivPreviewTrash)
            val previewType = "$wasteType ($confidencePercentage%)"
            tvPreviewType.text = previewType
        }
    }

    private fun showLoading(state: Boolean) {
        binding.container.isRefreshing = state
    }

    private fun showYesNoDialog(status: String, message: String) {
        val dialog = Dialog(this)
        bindingDialog = LayoutYesnoDialogBinding.inflate(layoutInflater)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)
        dialog.setContentView(bindingDialog.root)

        bindingDialog.apply {
            tvDialog.text = message
            tvStatus.text = status

            btnYes.setOnClickListener {
                startActivity(Intent(this@CameraPreviewActivity, CameraActivity::class.java))
            }

            btnNo.setOnClickListener {
                startActivity(Intent(this@CameraPreviewActivity, MainActivity::class.java))
            }
        }
        dialog.show()
    }

    private fun showTrivia(wasteType: String) {
        val bottomSheetDialog = BottomSheetDialog(this)
        bindingBottomSheetDialog = LayoutTriviaBinding.inflate(layoutInflater)
        bottomSheetDialog.setCancelable(true)
        bottomSheetDialog.setContentView(bindingBottomSheetDialog.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            bindingBottomSheetDialog.tvTrivia.justificationMode = JUSTIFICATION_MODE_INTER_WORD
        }

        bindingBottomSheetDialog.apply {
            tvWasteType.text = wasteType
            tvTrivia.text = getTrivia(wasteType)
        }

        bottomSheetDialog.show()
    }
}