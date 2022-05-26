package com.mvvm.basic.ui.view_station

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.example.easywaylocation.EasyWayLocation
import com.example.easywaylocation.EasyWayLocation.LOCATION_SETTING_REQUEST_CODE
import com.example.easywaylocation.Listener
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.mvvm.basic.R
import com.mvvm.basic.databinding.ActivityViewStationBinding
import com.mvvm.basic.domain.model.bike_station.ResponseBikeStations
import com.mvvm.basic.support.CommonUtility.selfCheckPermission
import com.mvvm.basic.support.inline.orElse
import dagger.hilt.android.AndroidEntryPoint
import org.jetbrains.anko.toast


@AndroidEntryPoint
class ViewStationActivity : AppCompatActivity() {
    private val viewModel: ViewStationViewModel by lazy { ViewModelProvider(this)[ViewStationViewModel::class.java] }
    private lateinit var binding: ActivityViewStationBinding

    private var easyWayLocation: EasyWayLocation? = null
    private var progressDialog: ProgressDialog? = null
    private lateinit var selectedBikeStation: ResponseBikeStations.Parcel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityViewStationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.onCreate()

        initIntent()
        initPermission()
        initGps()
        initMap()
        initData()
        initObserver()
        initListener()
        initPreview()
    }

    private fun initIntent() {
        intent.getParcelableExtra<ResponseBikeStations.Parcel>(INTENT_SELECTED_BIKE_STATION)?.let {
            selectedBikeStation = it
        }.orElse {
            Log.e("ViewStationActivity", "invalid call")
            finish()
        }
    }

    @SuppressLint("MissingPermission")
    private fun initMap() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync { googleMap ->

            /*Add marker into map*/
            val latLng = LatLng(selectedBikeStation.GeometryLat, selectedBikeStation.geometryLng)
            googleMap.addMarker(
                MarkerOptions().position(latLng)
                    .title(selectedBikeStation.properties?.label ?: "Bike Station")
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_bike_marker))
            ).showInfoWindow()
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom((latLng), 11.0F));
            googleMap.isMyLocationEnabled = true
        }
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
                this@ViewStationActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 101
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
                location?.let { location ->
                    /*Update distance in UI*/
                    val endLocation = Location("LocationB")
                    endLocation.latitude = selectedBikeStation.GeometryLat
                    endLocation.longitude = selectedBikeStation.geometryLng

                    val distance = EasyWayLocation.calculateDistance(
                        location.latitude,
                        location.latitude,
                        endLocation.latitude,
                        endLocation.longitude
                    )

                    binding.distance = "${distance.toInt()}m"
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
    }

    private fun initListener() {
        //noop
    }

    private fun initPreview() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = selectedBikeStation.properties?.label ?: "Bike Station"
        binding.feature = selectedBikeStation
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        easyWayLocation?.startLocation()
    }

    override fun onPause() {
        super.onPause()
        easyWayLocation?.endUpdates()
    }

    companion object {
        private const val INTENT_SELECTED_BIKE_STATION = "INTENT_SELECTED_BIKE_STATION"

        fun start(activity: Activity, feature: ResponseBikeStations.Parcel) {
            activity.startActivity(
                Intent(activity, ViewStationActivity::class.java).putExtra(
                    INTENT_SELECTED_BIKE_STATION, feature
                )
            )
        }
    }
}