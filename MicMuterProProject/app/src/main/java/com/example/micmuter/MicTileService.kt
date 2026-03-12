
package com.example.micmuter

import android.content.Intent
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService

class MicTileService:TileService(){

 override fun onClick(){

  val tile=qsTile

  if(tile.state==Tile.STATE_ACTIVE){
   stopService(Intent(this,MicMonitorService::class.java))
   tile.state=Tile.STATE_INACTIVE
  }else{
   startService(Intent(this,MicMonitorService::class.java))
   tile.state=Tile.STATE_ACTIVE
  }

  tile.updateTile()
 }
}
