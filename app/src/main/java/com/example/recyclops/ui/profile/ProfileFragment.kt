package com.example.recyclops.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.recyclops.databinding.FragmentProfileBinding
import com.example.recyclops.ui.login.LoginActivity
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val profileViewModel =
            ViewModelProvider(this)[ProfileViewModel::class.java]

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val user = profileViewModel.user

        binding.container.isEnabled = false

        showLoading(true)

        if (user != null) {
            user.let {
                val name = it.displayName
                val email = it.email
                val photoUrl = it.photoUrl

                Glide.with(requireContext())
                    .load(photoUrl)
                    .into(binding.ivProfile)
                binding.tvProfileNama.text = name
                binding.tvProfileEmail.text = email
            }
        } else {
            Toast.makeText(requireContext(), "Anda Belum Login", Toast.LENGTH_SHORT).show()
        }

        showLoading(false)

        binding.btnLogout.setOnClickListener {
            Firebase.auth.signOut()
            AuthUI.getInstance().signOut(requireContext()).addOnCompleteListener {
                LoginActivity().updateUI(null)
                val intent = Intent(requireContext(), LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading(state: Boolean) {
        binding.container.isRefreshing = state
    }
}