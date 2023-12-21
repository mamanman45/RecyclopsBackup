package com.example.recyclops.ui.poin

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.recyclops.databinding.FragmentPoinBinding
import com.google.firebase.auth.FirebaseAuth

class PoinFragment : Fragment() {

    private var _binding: FragmentPoinBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private lateinit var pointViewModel: PoinViewModel
    private lateinit var pointtv: TextView
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        pointViewModel = ViewModelProvider(this)[PoinViewModel::class.java]

        _binding = FragmentPoinBinding.inflate(inflater, container, false)
        val root: View = binding.root

        pointtv = binding.tvPoin

        binding.container.setOnRefreshListener {
            setPoints(pointViewModel)
            getPoints(pointtv, pointViewModel)
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setPoints(pointViewModel)
        getPoints(pointtv, pointViewModel)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading(state: Boolean) {
        binding.container.isRefreshing = state
    }

    private fun setPoints(homeViewModel: PoinViewModel) {
        val mUser = FirebaseAuth.getInstance().currentUser
        mUser!!.getIdToken(true)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showLoading(true)
                    val idToken: String = task.result.token.toString()
                    val token = "Bearer $idToken"
                    homeViewModel.getUserPoint(token)
                } else {
                    Log.d("Exception", task.exception.toString())
                }
            }
    }

    private fun getPoints(textView: TextView, poinViewModel: PoinViewModel) {
        poinViewModel.point.observe(requireActivity()) {
            if (it != null) {
                textView.text = it.totalPoints.toString()
                if (_binding != null) {
                    showLoading(false)
                }
            } else {
                if (_binding != null) {
                    showLoading(false)
                }
                Toast.makeText(requireContext(), "Gagal Mendapatkan Poin", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}