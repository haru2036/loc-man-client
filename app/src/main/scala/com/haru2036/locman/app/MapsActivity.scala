package com.haru2036.locman.app

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.{LatLng, MarkerOptions}

/**
  * Created by 2036 on 2015/12/18.
  */
class MapsActivity extends FragmentActivity{
    lazy val mMap = this.getSupportFragmentManager.findFragmentById(R.id.map).asInstanceOf[SupportMapFragment].getMap

    def initializeMap() : Unit = mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("marker"))

    override def onCreate(savedInstanceState: Bundle): Unit = {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
    }

    override def onResume()={
        super.onResume()
        initializeMap()
    }

    override def onPause()={
        super.onPause()
    }

    override def onStop(): Unit ={
        super.onStop()
    }


}
