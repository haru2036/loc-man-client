package com.haru2036.locman.app.actors

import akka.actor.{Actor, Props}
import android.util.Log

object LocationActor {
    def props = Props(new LocationActor)
}

class LocationActor extends Actor{
    def receive = {
        case _ ⇒ Log.d("LocationActor", "message received")
    }

    override def preStart(): Unit ={

    }
}
