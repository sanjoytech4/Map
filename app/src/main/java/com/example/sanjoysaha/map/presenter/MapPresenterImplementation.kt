package com.example.sanjoysaha.map.presenter

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.example.sanjoysaha.map.MainActivity
import android.app.ActivityManager
import android.content.Intent
import android.support.design.widget.FloatingActionButton
import android.view.View
import com.example.sanjoysaha.map.services.LocationDataService
import kotlinx.android.synthetic.main.activity_main.*


class MapPresenterImplementation: MapPresenter {
    private val TAG = MapPresenterImplementation::class.java.simpleName
    private var context: Context? = null
    private var mapCallback: MapCallback? = null
    private var LOCATION_PERMISSIONS:Int=123
    private fun constructor(){}


     constructor(context: Context, mapCallback: MapCallback) {
        this.context = context
        this.mapCallback = mapCallback
    }

    override fun handleLocationPermission(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            LOCATION_PERMISSIONS -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    mapCallback!!.grandLocationPermission(true)
                }
                else{
                    mapCallback!!.grandLocationPermission(false)
                }
            }
        }
    }

    override fun checkAllPermission(activity: MainActivity?):Boolean {
        if (ContextCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSIONS)
            return false
        }else
            return true
    }


    override fun isServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = context!!.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

    override fun handleServiceButtonClick(view: FloatingActionButton) {
        val status=isServiceRunning(LocationDataService::class.java)

        var intent= Intent(context, LocationDataService::class.java)
        if(status){
            context!!.stopService(intent)
            view.setImageResource(android.R.drawable.ic_media_play)
        }else{
            context!!.startService(intent)
            view.setImageResource(android.R.drawable.ic_media_pause)
        }
    }


}