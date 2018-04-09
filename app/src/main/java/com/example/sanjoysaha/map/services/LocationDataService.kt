package com.example.sanjoysaha.map.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.sanjoysaha.map.api.APIClient
import com.example.sanjoysaha.map.api.APIService
import com.example.sanjoysaha.map.model.LocationData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import android.support.v4.content.LocalBroadcastManager
import com.example.sanjoysaha.map.MainActivity


class LocationDataService : Service() {

    private var timer:Timer?=null
    private var locationData:LocationData=LocationData(0.0,0.0)
    private var context:Context?=null
    private val TAG:String?=LocationDataService::class.java.simpleName

    override fun onBind(intent: Intent?): IBinder {
        return null!!
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        context=this
        startTimer()
        return START_STICKY;
    }

    private fun startTimer() {
        if(timer!=null){
            return
        }
        timer =  Timer();
        timer!!.scheduleAtFixedRate( ScheduleTimer(),0,15000);
    }

  inner class ScheduleTimer: TimerTask() {
       private val TAG:String?=ScheduleTimer::class.java.simpleName

       override fun run() {
           fetchLocation()
       }

       private fun fetchLocation(){
           val retrofit = APIClient.getClient()
           val service = retrofit.create(APIService::class.java)
           val call = service.getLocation()
           call.enqueue(object : Callback<LocationData> {
               override fun onResponse(call: Call<LocationData>, response: Response<LocationData>?) {
                   val locationData = response!!.body()
                   Log.d(TAG,"fetchLocation :: location "+locationData!!.lat +" location "+locationData.lng)
                   sendLocation(locationData)
               }

               override fun onFailure(call: Call<LocationData>, t: Throwable) {
                   Log.i(TAG, "onFailure: " + t.message)
               }
           })
       }
   }

    private fun sendLocation(locationData: LocationData){
        val intent = Intent(MainActivity.ACTION_LOCATION_UPDATE)
        intent.putParcelableArrayListExtra(MainActivity.LOCATION_DATA, arrayListOf(locationData))
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }




    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG,"onDestroy")
        timer!!.cancel()
        timer=null
    }
}