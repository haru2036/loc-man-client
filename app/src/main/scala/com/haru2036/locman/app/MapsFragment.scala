package com.haru2036.locman.app

import android.support.v4.app.Fragment
import android.view.{View, ViewGroup, LayoutInflater}
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.{MarkerOptions, LatLng}
import macroid.{Contexts, IdGeneration}
import macroid.akka.AkkaFragment

/**
  * Created by 2036 on 2015/12/18.
  */
class MapsFragment extends SupportMapFragment with AkkaFragment with IdGeneration with Contexts[Fragment]{

    lazy val actor = Some(actorSystem.actorSelection("/user/actor1"))

    def initializeMap() : Unit = getMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("marker"))

    override def onResume() ={
        super.onResume()
        initializeMap()
    }

}
