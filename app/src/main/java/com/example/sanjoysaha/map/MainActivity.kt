package com.example.sanjoysaha.map

import android.annotation.SuppressLint
import android.content.IntentFilter
import android.location.Location
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.sanjoysaha.map.model.LocationData
import com.example.sanjoysaha.map.presenter.MapCallback
import com.example.sanjoysaha.map.presenter.MapPresenter
import com.example.sanjoysaha.map.presenter.MapPresenterImplementation
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback

import kotlinx.android.synthetic.main.activity_main.*
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.LatLng
import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context
import android.support.design.widget.FloatingActionButton
import com.example.sanjoysaha.map.services.LocationDataService


class MainActivity : AppCompatActivity(), OnMapReadyCallback,MapCallback {

    private var mMap: GoogleMap? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var presenter: MapPresenter?=null
    private var locationData:LocationData?= LocationData(0.0,0.0)
    private var TAG:String=MainActivity::class.java.simpleName
    companion object {
        val ACTION_LOCATION_UPDATE="location update"
        val LOCATION_DATA="location_data"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        presenter=MapPresenterImplementation(this,this)

        var status=presenter!!.isServiceRunning(LocationDataService::class.java)
        if(status){
            fab.setImageResource(android.R.drawable.ic_media_pause)
        }
        else {
            fab.setImageResource(android.R.drawable.ic_media_play)
        }

        fab.setOnClickListener { view ->
            presenter!!.handleServiceButtonClick(view as FloatingActionButton)
        }

        if(  presenter!!.checkAllPermission(this))
        {
            getCurrentLocation()
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,IntentFilter(ACTION_LOCATION_UPDATE))
    }

    private val mMessageReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val data = intent.getParcelableArrayListExtra<LocationData>(LOCATION_DATA)
            if (data!=null) {
                locationData=data[0]
                showLocationOnMap(locationData!!)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation(){
        fusedLocationClient.lastLocation.addOnSuccessListener { location : Location? ->
            locationData=LocationData(location!!.latitude,location.longitude)
            Log.d(TAG, "getCurrentLocation location!!.altitude "+location.latitude)
            Log.d(TAG, "getCurrentLocation location!!.latitude "+location.longitude)

            showLocationOnMap(locationData!!)
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap
        showLocationOnMap(locationData!!)
    }

    override fun showLocationOnMap(locationData: LocationData){
        val location = LatLng(locationData.lat!!, locationData.lng!!)
        mMap!!.addMarker(MarkerOptions().position(location).title("Marker "+locationData.lat+","+locationData.lng))
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(location))
        mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(location,16.0f))
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults)
        presenter!!.handleLocationPermission(requestCode,permissions,grantResults)
    }

    override fun grandLocationPermission(flag:Boolean) {
        if(flag){
            getCurrentLocation()
        }else{
            showLocationOnMap(locationData!!)
        }
    }

}
