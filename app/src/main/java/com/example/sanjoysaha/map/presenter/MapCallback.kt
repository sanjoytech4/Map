package com.example.sanjoysaha.map.presenter

import com.example.sanjoysaha.map.model.LocationData

interface MapCallback {
    fun grandLocationPermission(flag:Boolean)
    fun showLocationOnMap(locationData: LocationData)
}