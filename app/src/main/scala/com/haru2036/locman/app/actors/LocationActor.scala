package com.haru2036.locman.app.actors

import akka.actor.{ActorRef, Actor, Props}
import android.content.Context
import android.location.Location
import android.os.Bundle
import android.util.Log
import com.google.android.gms.common.{ConnectionResult, GooglePlayServicesUtil}
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.GoogleApiClient.{OnConnectionFailedListener, ConnectionCallbacks}
import com.google.android.gms.location.{LocationListener, LocationRequest, LocationServices}
import com.haru2036.locman.app.message.UpdateLocation

object LocationActor {
    def props (cnt: Context, recipients: List[ActorRef]) = Props(new LocationActor(cnt, recipients))
}

class LocationActor(cnt : Context, recipients: List[ActorRef]) extends Actor with ConnectionCallbacks with OnConnectionFailedListener{
    lazy val apiclient = new GoogleApiClient.Builder(cnt).addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build()

    lazy val locationListener = new LocationListener {override def onLocationChanged(l: Location): Unit = recipients.foreach(x => x ! new UpdateLocation(l.getAccuracy, l.getLatitude, l.getLongitude, l.getAltitude))}

    def receive = {
        case x ⇒ Log.d("LocationActor", "message received" ++ x.toString)
    }

    override def preStart(): Unit ={
        GooglePlayServicesUtil.isGooglePlayServicesAvailable(cnt)
        apiclient.connect()
    }

    override def postStop() = LocationServices.FusedLocationApi.removeLocationUpdates(apiclient, locationListener)

    override def onConnectionSuspended(i: Int): Unit = ???

    override def onConnected(bundle: Bundle): Unit = {
        val locationRequest = LocationRequest.create().setInterval(1000).setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

        LocationServices.FusedLocationApi.requestLocationUpdates(apiclient, locationRequest, locationListener)
    }

    //if the connection failed, the actor will kill itself
    override def onConnectionFailed(connectionResult: ConnectionResult): Unit = ???
}
