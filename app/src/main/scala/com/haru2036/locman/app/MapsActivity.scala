package com.haru2036.locman.app

import java.net.URI

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.view.ViewGroup.LayoutParams._
import android.widget.RelativeLayout
import com.haru2036.locman.app.actors.{LocationActor, MapFragmentActor, WSActor}
import macroid.FullDsl._
import macroid.akka.AkkaActivity
import macroid.{Contexts, IdGeneration}

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
                l[RelativeLayout] {
                    f[MapsFragment].framed(Id.mapsfragment, Tag.map) <~ lp[RelativeLayout](MATCH_PARENT, MATCH_PARENT)
                }
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
    }

    override def onDestroy()= {
        super.onStop()
        actorSystem.shutdown()
    }

    override val actorSystemName: String = "locman-map-system"

    lazy val mapActor = actorSystem.actorOf(MapFragmentActor.props, "mapActor")

    lazy val locationActor = actorSystem.actorOf(LocationActor.props(this, List(mapActor, wsActor)), "locationActor")

    //todo: use getOrElse
    lazy val wsActor = actorSystem.actorOf(WSActor.props(mapActor, new URI(Config.wsEndPoint("1")), "_SESSION=" ++ Config.appSession.get), "wsActor")


}
