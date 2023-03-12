package com.ontech.com.abgec.fcm

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception



const val TOPIC = "/topics/myTopic3"
class topic {
    private val TAG="MainActivity"

    fun noti(name:String,phone:String,key:String){

        //please add this line

        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)

        if(name.isNotEmpty() && phone.isNotEmpty()){
            PushNotification(
                NotificationData(name, phone),
                TOPIC
            ).also {
                sendNotification(it)
            }
        }
    }

    private fun sendNotification(notification: PushNotification)= CoroutineScope(Dispatchers.IO).launch {

        try{
            val response= RetrofirInstance.api.postNotification(notification)
            if(response.isSuccessful){
                Log.d(TAG,"Response:${Gson().toJson(response)}")
            }else{
                Log.e(TAG,response.errorBody().toString())
            }

        }catch (e: Exception){
            Log.e(TAG,e.toString())
        }

    }

}
