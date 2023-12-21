package com.example.recyclops.ui.history

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recyclops.data.UserHistoryItem
import com.example.recyclops.databinding.ActivityHistoryBinding
import com.google.firebase.auth.FirebaseAuth

class HistoryActivity : AppCompatActivity() {

    lateinit var binding: ActivityHistoryBinding
    lateinit var viewModel: HistoryViewModel
    private val list = ArrayList<UserHistoryItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[HistoryViewModel::class.java]

        setUserHistory(viewModel)

        binding.container.isEnabled = false

        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        viewModel.historyTrash.observe(this) { history ->
            if (history != null) {
                showLoading(true)
                val historyReversed = history.reversed()
                Log.d("Logas", history.toString())
                list.addAll(historyReversed)
                showRecyclerList()
                showLoading(false)
            }
        }

        binding.container.setOnRefreshListener {

        }

//        list.addAll(getListSetoran())
        showRecyclerList()
        showLoading(false)

    }

    private fun showLoading(state: Boolean) {
        binding.container.isRefreshing = state
    }

    private fun setUserHistory(HistoryViewModel: HistoryViewModel) {
        val mUser = FirebaseAuth.getInstance().currentUser
        mUser!!.getIdToken(true)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val idToken: String = task.result.token.toString()
                    val token = "Bearer $idToken"
                    HistoryViewModel.setUserHistory(token)
                } else {
                    Log.d("Exception", task.exception.toString())
                }
            }
    }

    private fun showRecyclerList() {
        binding.rvHistory.layoutManager = LinearLayoutManager(this)
        val historyAdapter = HistoryAdapter(list)
        binding.rvHistory.adapter = historyAdapter
    }

}