package com.haru2036.locman.app.actors

import akka.actor.{PoisonPill, ActorRef, Actor, Props}
import android.content.Context
import android.location.Location
import android.os.Bundle
import android.util.Log
import com.google.android.gms.common.{ConnectionResult, GooglePlayServicesUtil}
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.GoogleApiClient.{OnConnectionFailedListener, ConnectionCallbacks}
import com.google.android.gms.location.{LocationListener, LocationRequest, LocationServices}

object LocationActor {
    def props (cnt: Context, mapActor: ActorRef) = Props(new LocationActor(cnt, mapActor))
}

class LocationActor(cnt : Context, mapActor: ActorRef) extends Actor with ConnectionCallbacks with OnConnectionFailedListener{
    lazy val apiclient = new GoogleApiClient.Builder(cnt).addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build()

    def receive = {
        case _ ⇒ Log.d("LocationActor", "message received")
    }

    override def preStart(): Unit ={
        GooglePlayServicesUtil.isGooglePlayServicesAvailable(cnt)
        apiclient.connect()

    }

    override def onConnectionSuspended(i: Int): Unit = ???

    override def onConnected(bundle: Bundle): Unit = {
        LocationServices.FusedLocationApi.requestLocationUpdates(apiclient, LocationRequest.create(), new LocationListener {
            override def onLocationChanged(location: Location): Unit = mapActor ! location
        })
        val lastLocation = Option(LocationServices.FusedLocationApi.getLastLocation(
            apiclient))
        lastLocation.foreach (mapActor !)
    }

    //if the connection failed, the actor will kill itself
    override def onConnectionFailed(connectionResult: ConnectionResult): Unit = ???
}
