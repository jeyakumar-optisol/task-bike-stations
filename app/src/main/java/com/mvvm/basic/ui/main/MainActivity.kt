package com.mvvm.basic.ui.main

import android.app.ProgressDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.mvvm.basic.databinding.ActivityMainBinding
import com.mvvm.basic.domain.model.bike_station.ResponseBikeStations
import com.mvvm.basic.ui.main.adapter.MainAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var progressDialog: ProgressDialog? = null
    private val viewModel: MainViewModel by lazy { ViewModelProvider(this)[MainViewModel::class.java] }
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainAdapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.onCreate()

        initData()
        initObserver()
        initListener()
        initPreview()
    }

    private fun initData() {
        //noop
    }

    private fun initObserver() {
        viewModel.liveDataLoader.observe(this) {
            if (it) {
                if (progressDialog != null) {
                    return@observe
                }
                progressDialog = ProgressDialog(this)
                progressDialog?.setCancelable(false)
                progressDialog?.setOnDismissListener {
                    progressDialog = null
                }
                progressDialog?.setMessage("loading")
                progressDialog?.show()
            } else {
                progressDialog?.dismiss()
                progressDialog = null
            }
        }
        viewModel.liveDataBikeStations.observe(this) {
            if (!::mainAdapter.isInitialized) {
                mainAdapter = MainAdapter(itemListener)
                binding.bikeStationsRecyclerView.adapter = mainAdapter
            }
            mainAdapter.submitList(it)
        }
    }

    private fun initListener() {
    }

    private fun initPreview() {
    }

    val itemListener = object : MainAdapter.ItemListener {
        override fun onItemSelected(position: Int, item: ResponseBikeStations.Feature) {
        }
    }
}