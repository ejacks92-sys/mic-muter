
package com.example.micmuter

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity

class MainActivity: AppCompatActivity(){

 override fun onCreate(savedInstanceState: Bundle?){
  super.onCreate(savedInstanceState)
  setContentView(R.layout.activity_main)

  val toggle=findViewById<Switch>(R.id.toggle)

  val nm=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
  if(!nm.isNotificationPolicyAccessGranted){
   startActivity(Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS))
  }

  requestBatteryIgnore()

  toggle.setOnCheckedChangeListener { _,on ->
   if(on){
    startService(Intent(this,MicMonitorService::class.java))
   }else{
    stopService(Intent(this,MicMonitorService::class.java))
   }
  }
 }

 private fun requestBatteryIgnore(){
  val pm=getSystemService(Context.POWER_SERVICE) as PowerManager
  val pkg=packageName

  if(!pm.isIgnoringBatteryOptimizations(pkg)){
   val intent=Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
   intent.data=android.net.Uri.parse("package:$pkg")
   startActivity(intent)
  }
 }
}
