package com.example.recyclops.ui.profile

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ProfileViewModel : ViewModel() {
    val user = Firebase.auth.currentUser
}