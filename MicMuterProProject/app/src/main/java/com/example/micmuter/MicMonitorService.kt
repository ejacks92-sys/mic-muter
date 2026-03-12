
package com.example.micmuter

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.AudioRecordingConfiguration
import android.os.Build
import android.os.IBinder

class MicMonitorService: Service(){

 lateinit var audioManager:AudioManager
 lateinit var notificationManager:NotificationManager

 var previous=NotificationManager.INTERRUPTION_FILTER_ALL

 override fun onCreate(){
  super.onCreate()

  audioManager=getSystemService(Context.AUDIO_SERVICE) as AudioManager
  notificationManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

  startForeground(1,notification())
  monitor()
 }

 private fun monitor(){

  val callback=object:AudioManager.AudioRecordingCallback(){
   override fun onRecordingConfigChanged(configs:List<AudioRecordingConfiguration>){

    if(configs.isNotEmpty()){
     enableDND()
    }else{
     restore()
    }
   }
  }

  audioManager.registerAudioRecordingCallback(callback,null)
 }

 private fun enableDND(){
  if(notificationManager.isNotificationPolicyAccessGranted){
   previous=notificationManager.currentInterruptionFilter
   notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_NONE)
  }
 }

 private fun restore(){
  if(notificationManager.isNotificationPolicyAccessGranted){
   notificationManager.setInterruptionFilter(previous)
  }
 }

 private fun notification():Notification{

  val id="micmuter"

  if(Build.VERSION.SDK_INT>=26){
   val channel=NotificationChannel(id,"MicMuter",NotificationManager.IMPORTANCE_LOW)
   notificationManager.createNotificationChannel(channel)
  }

  return Notification.Builder(this,id)
   .setContentTitle("Mic Muter Active")
   .setContentText("Notifications muted when mic used")
   .setSmallIcon(android.R.drawable.ic_btn_speak_now)
   .build()
 }

 override fun onBind(intent:Intent?):IBinder?{ return null }
}
