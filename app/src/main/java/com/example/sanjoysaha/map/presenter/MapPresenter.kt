package com.example.sanjoysaha.map.presenter

import android.support.design.widget.FloatingActionButton
import com.example.sanjoysaha.map.MainActivity

interface MapPresenter {
    fun checkAllPermission(activity: MainActivity?): Boolean
    fun handleLocationPermission(requestCode: Int, permissions: Array<String>, grantResults: IntArray)
    fun isServiceRunning(serviceClass: Class<*>): Boolean
    fun handleServiceButtonClick(view: FloatingActionButton)
}