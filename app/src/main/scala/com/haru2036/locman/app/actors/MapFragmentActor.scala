package com.haru2036.locman.app.actors

import akka.actor.{Actor, ActorLogging, Props}
import android.util.Log
import scala.collection.mutable
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.{Marker, BitmapDescriptorFactory, MarkerOptions, LatLng}
import com.haru2036.locman.app.{R, MapsFragment}
import com.haru2036.locman.app.message._
import macroid.Ui
import macroid.akka.FragmentActor
import scala.reflect.{ClassTag, classTag}
/**
  * Created by 2036 on 2015/12/18.
  */
object MapFragmentActor {
    def props = Props(new MapFragmentActor)
}

class MapFragmentActor extends FragmentActor[MapsFragment] with ActorLogging {
    var markers: mutable.Map[JUser, Marker] = mutable.Map()
    var myselfMarker: Option[Marker] = None


    def receive = receiveUi andThen {
        case x: UpdateLocation => withUi(fragment => Ui{
            try {
                Log.d("MapFragmentActor", "message received (UpdateLocation)")
                implicit val map: GoogleMap = fragment.getMap
                myselfMarker = Option(updateMarker(myselfMarker, new UserSessionEvent[UpdateLocation](x, new JUser("", "Myself"))))
            }catch{
                case x: Throwable => log.error(x, "occured at updatelocation")
            }
        })
            // todo: 型パラメータで分岐したいけど型消去周りの話がこんがらがってうまくいかなかった
        case x: UserSessionEvent[UpdateLocation] if x.event.isInstanceOf[UpdateLocation] =>withUi(fragment=> Ui{
            Log.d("MapFragmentActor", "message received (UserSessionEvent UpdateLocation)")
            implicit val map: GoogleMap = fragment.getMap
            Log.d("Updated: ", x.toString)
            val userMarker = markers.find{case (a, b)=> a.uid.equals(x.author.uid)}.map{case (a, b) => b}
            markers.put (x.author, updateMarker(userMarker, x))
        })
        case x: UserSessionEvent[Exited] if x.event.isInstanceOf[Exited] =>withUi(fragment=> Ui{
            Log.d("MapFragmentActor", "message received (UserSessionEvent Exited)")
            Log.d("Exited: ", x.toString)
            markers.find{case (a, b)=> a.uid.equals(x.author.uid)}.foreach{case (a, b) => b.remove()}
        })
        case x ⇒ Log.d("MapFragmentActor", "message received:" ++ x.toString)
    }


    def getOrCreateMarker(marker: Option[Marker], defaultMarkerOptions: Option[MarkerOptions])(implicit map: GoogleMap): Marker =
        marker.getOrElse{map.addMarker(defaultMarkerOptions.getOrElse(new MarkerOptions()))}

    def updateMarker(marker: Option[Marker], location: UserSessionEvent[UpdateLocation])(implicit map: GoogleMap): Marker={
        val mk = marker.getOrElse{
            map.addMarker(new MarkerOptions()
                    .position(new LatLng(location.event.latitude, location.event.longitude))
                    .title(location.author.name)
                    .anchor(0.5f, 0.5f)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_myself)))
        }
        mk.setPosition(new LatLng(location.event.latitude, location.event.longitude))
        mk
    }

}
