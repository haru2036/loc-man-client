package com.haru2036.locman.app.actors

import akka.actor.Props
import android.util.Log
import com.google.android.gms.maps.model.{BitmapDescriptorFactory, MarkerOptions, LatLng}
import com.haru2036.locman.app.{R, MapsFragment}
import com.haru2036.locman.app.message.UpdateLocation
import macroid.Ui
import macroid.akka.FragmentActor
/**
  * Created by 2036 on 2015/12/18.
  */
object MapFragmentActor {
    def props = Props(new MapFragmentActor)
}

class MapFragmentActor extends FragmentActor[MapsFragment]{
    def receive = receiveUi andThen {
        case x: UpdateLocation => withUi(fragment => Ui{
            val map = fragment.getMap
            map.clear()
            map.addMarker(new MarkerOptions().position(new LatLng(x.latitude, x.longitude)).title("marker_myself").anchor(0.5f, 0.5f)).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_myself))
            Log.d("MapFragmentActor", "message received and maps refreshed")
        })
        case x ⇒ Log.d("MapFragmentActor", "message received:" ++ x.toString)
    }
}
