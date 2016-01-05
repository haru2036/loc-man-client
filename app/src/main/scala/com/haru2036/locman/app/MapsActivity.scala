package com.haru2036.locman.app

import java.net.URI

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import com.haru2036.locman.app.actors.{WSActor, MapFragmentActor, LocationActor}
import macroid.{Contexts, IdGeneration}
import macroid.akka.AkkaActivity
import macroid.FullDsl._

/**
  * Created by 2036 on 2015/12/18.
  */
class MapsActivity extends FragmentActivity with AkkaActivity with IdGeneration with Contexts[FragmentActivity]{

    override def onCreate(savedInstanceState: Bundle): Unit = {
        super.onCreate(savedInstanceState)
        mapActor
        locationActor
        wsActor
        setContentView{
            getUi{
                f[MapsFragment].framed(Id.mapsfragment, Tag.map)
            }
        }
    }
    override def onResume()={
        super.onResume()
    }

    override def onPause()={
        super.onPause()
    }

    override def onStop(): Unit ={
        super.onStop()
        actorSystem.shutdown()
    }

    override val actorSystemName: String = "locman-map-system"

    lazy val mapActor = actorSystem.actorOf(MapFragmentActor.props, "mapActor")

    lazy val locationActor = actorSystem.actorOf(LocationActor.props(this, List(mapActor, wsActor)), "locationActor")

    lazy val wsActor = actorSystem.actorOf(WSActor.props(mapActor, new URI("ws://192.168.1.22:3000/session/ws/1")), "wsActor")


}
