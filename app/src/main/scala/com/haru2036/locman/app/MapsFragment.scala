package com.haru2036.locman.app

import android.support.v4.app.Fragment
import com.google.android.gms.maps.SupportMapFragment
import com.haru2036.locman.app.actors.MapFragmentActor
import macroid.akka.AkkaFragment
import macroid.{Contexts, IdGeneration}

/**
  * Created by 2036 on 2015/12/18.
  */
class MapsFragment extends SupportMapFragment with AkkaFragment with IdGeneration with Contexts[Fragment]{

    lazy val actor = Some(actorSystem.actorSelection("/user/mapActor"))

    override def onResume() ={
        super.onResume()
        assert(actor.isInstanceOf[Option[MapFragmentActor]])
    }

}
