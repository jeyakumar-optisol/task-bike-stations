package com.mvvm.basic.ui.main

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.example.easywaylocation.EasyWayLocation
import com.example.easywaylocation.EasyWayLocation.LOCATION_SETTING_REQUEST_CODE
import com.example.easywaylocation.Listener
import com.mvvm.basic.databinding.ActivityMainBinding
import com.mvvm.basic.domain.model.bike_station.ResponseBikeStations
import com.mvvm.basic.support.CommonUtility.selfCheckPermission
import com.mvvm.basic.ui.main.adapter.MainAdapter
import com.mvvm.basic.ui.view_station.ViewStationActivity
import dagger.hilt.android.AndroidEntryPoint
import org.jetbrains.anko.toast

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var easyWayLocation: EasyWayLocation? = null
    private var progressDialog: ProgressDialog? = null
    private val viewModel: MainViewModel by lazy { ViewModelProvider(this)[MainViewModel::class.java] }
    private lateinit var binding: ActivityMainBinding
    private var mainAdapter: MainAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.onCreate()

        initPermission()
        initGps()
        initData()
        initObserver()
        initListener()
        initPreview()
    }

    private fun initPermission() {
        if (selfCheckPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            Log.i("initPermission", "have permission")

        } else if (ActivityCompat.shouldShowRequestPermissionRationale(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            toast("Location permission required")
        } else {
            ActivityCompat.requestPermissions(
                this@MainActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 101
            );
        }
    }

    private fun initGps() {
        easyWayLocation = EasyWayLocation(this, false, false, object : Listener {
            override fun locationOn() {
                Log.e("MainScreen", "locationOn")
            }

            override fun currentLocation(location: Location?) {
                Log.e(
                    "MainScreen",
                    "currentLocation: lat.${location?.latitude} lng.${location?.longitude}"
                )
                location?.let {
                    MainAdapter.CURRENT_GPS_LOCATION = it
                    mainAdapter?.notifyDataSetChanged()
                }
            }

            override fun locationCancelled() {
                Log.e("MainScreen", "locationCancelled")
            }
        })
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
            if (mainAdapter == null) {
                mainAdapter = MainAdapter(itemListener)
                binding.bikeStationsRecyclerView.adapter = mainAdapter
            }
            mainAdapter?.submitList(it)
        }
    }

    private fun initListener() {
        //noop
    }

    private fun initPreview() {
    }

    private val itemListener = object : MainAdapter.ItemListener {
        override fun onItemSelected(position: Int, item: ResponseBikeStations.Feature) {
            ViewStationActivity.start(this@MainActivity, item.parcelize())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            LOCATION_SETTING_REQUEST_CODE -> {
                easyWayLocation?.onActivityResult(resultCode)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            101 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (selfCheckPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
                        easyWayLocation?.startLocation()
                    }
                } else {
                    toast("Permission required")
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        easyWayLocation?.startLocation()
    }

    override fun onPause() {
        super.onPause()
        easyWayLocation?.endUpdates()
    }
}