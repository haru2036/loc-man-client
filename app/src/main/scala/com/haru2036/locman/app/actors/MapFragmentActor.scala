package com.haru2036.locman.app.actors

import akka.actor.Props
import android.util.Log
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.{Marker, BitmapDescriptorFactory, MarkerOptions, LatLng}
import com.haru2036.locman.app.{R, MapsFragment}
import com.haru2036.locman.app.message.{JUser, UserSessionEvent, UpdateLocation}
import macroid.Ui
import macroid.akka.FragmentActor
/**
  * Created by 2036 on 2015/12/18.
  */
object MapFragmentActor {
    def props = Props(new MapFragmentActor)
}

class MapFragmentActor extends FragmentActor[MapsFragment]{
    var markers: Map[JUser, Marker] = Map()
    var myselfMarker: Option[Marker] = None

    def receive = receiveUi andThen {
        case x: UpdateLocation => withUi(fragment => Ui{
            implicit val map: GoogleMap = fragment.getMap
            myselfMarker = Option(updateMarker(myselfMarker, x))
            Log.d("MapFragmentActor", "message received and maps refreshed")
        })
        case x ⇒ Log.d("MapFragmentActor", "message received:" ++ x.toString)
    }


    def updateMarker(marker: Option[Marker], location: UpdateLocation)(implicit map: GoogleMap): Marker={
        val marker = myselfMarker.getOrElse{
            map.addMarker(new MarkerOptions()
                    .position(new LatLng(location.latitude, location.longitude))
                    .title("marker_myself")
                    .anchor(0.5f, 0.5f)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_myself)))
        }
        marker.setPosition(new LatLng(location.latitude, location.longitude))
        marker
    }
}
