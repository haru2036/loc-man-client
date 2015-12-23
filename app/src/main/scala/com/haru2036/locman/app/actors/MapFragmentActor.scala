package com.haru2036.locman.app.actors

import akka.actor.Props
import android.util.Log
import com.google.android.gms.maps.model.{MarkerOptions, LatLng}
import com.haru2036.locman.app.MapsFragment
import macroid.Ui
import macroid.akka.{FragmentActor}
/**
  * Created by 2036 on 2015/12/18.
  */
object MapFragmentActor {
    def props = Props(new MapFragmentActor)
}

class MapFragmentActor extends FragmentActor[MapsFragment]{
    def receive = receiveUi andThen {
        case x: LatLng ⇒ withUi(fragment => Ui{
            val map = fragment.getMap
            map.clear()
            map.addMarker(new MarkerOptions().position(x).title("marker"))
            Log.d("MapFragmentActor", "message received and maps refreshed")
        })
        case x ⇒ Log.d("MapFragmentActor", "message received:" ++ x.toString)
    }
}
