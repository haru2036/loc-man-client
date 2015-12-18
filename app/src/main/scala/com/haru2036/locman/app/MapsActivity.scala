package com.haru2036.locman.app

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import com.google.android.gms.maps.model.{LatLng}
import com.haru2036.locman.app.actors.{LocationActor}
import macroid.{Contexts, IdGeneration}
import macroid.akka.AkkaActivity
import macroid.FullDsl._

/**
  * Created by 2036 on 2015/12/18.
  */
class MapsActivity extends FragmentActivity with AkkaActivity with IdGeneration with Contexts[FragmentActivity]{

    override def onCreate(savedInstanceState: Bundle): Unit = {
        super.onCreate(savedInstanceState)
        actor1
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

    lazy val actor1 = actorSystem.actorOf(LocationActor.props, "maps")


}
