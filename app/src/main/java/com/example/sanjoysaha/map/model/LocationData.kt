package com.example.sanjoysaha.map.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LocationData() : Parcelable {
    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("latitude")
    @Expose
    var lat: Double? = 0.0

    @SerializedName("longitude")
    @Expose
    var lng: Double? = 0.0

    constructor(latitude: Double, longitude: Double):this() {
        lat = latitude
        lng = longitude
    }

    constructor(source: Parcel) : this(
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {}

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<LocationData> = object : Parcelable.Creator<LocationData> {
            override fun createFromParcel(source: Parcel): LocationData = LocationData(source)
            override fun newArray(size: Int): Array<LocationData?> = arrayOfNulls(size)
        }
    }
}

