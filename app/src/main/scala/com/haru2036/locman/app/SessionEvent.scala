package com.haru2036.locman.app

import spray.json._
import DefaultJsonProtocol._


/**
  * Created by 2036 on 2016/01/04.
  */



object UpdateLocationProtocol extends DefaultJsonProtocol{
    implicit object UpdateLocation extends RootJsonFormat[UpdateLocation]{
        override def write(ul: UpdateLocation) = JsObject(
            "error" -> JsNumber(ul.error),
            "latitude" -> JsNumber(ul.latitude),
            "longitude" -> JsNumber(ul.longitude),
            "altitude" -> JsNumber(ul.altitude)
        )

        override def read(json: JsValue): UpdateLocation = {
             json.asJsObject.getFields("error", "latitude", "longitude", "altitude") match {
                case Seq(JsNumber(error), JsNumber(latitude), JsNumber(longitude), JsNumber(altitude)) =>
                    new UpdateLocation(error.toDouble, latitude.toDouble, longitude.toDouble, altitude.toDouble)
                case _ => throw new DeserializationException("ks")
            }
        }
    }
}


object EventRootProtocol extends DefaultJsonProtocol{
    implicit val juserFormat = jsonFormat2(JUser)

    implicit object SessionEvent extends RootJsonFormat[SessionEvent] {
        override def write(obj: SessionEvent): JsValue = obj match {
            case updateLocation: UpdateLocation => JsObject(
                "tag" -> JsString("UpdateLocation"),
                "contents" -> UpdateLocationProtocol.UpdateLocation.write(updateLocation)
            )
            case joined: Joined => JsObject(
                "tag" -> JsString("Joined"),
                "contents" -> joined.user.toJson
            )
            case exited: Exited=> JsObject(
                "tag" -> JsString("Exited"),
                "contents" -> exited.user.toJson
            )
        }

        override def read(json: JsValue) = {
            json.asJsObject.getFields("tag", "contents") match{
                case Seq(JsString(tag), JsObject(contents)) => tag match{
                    case "UpdateLocation" => UpdateLocationProtocol.UpdateLocation.read(contents.toJson)
                    case "Joined" => new Joined(juserFormat.read(contents.toJson))
                    case "Exited" => new Exited(juserFormat.read(contents.toJson))
                }
            }
        }
    }
}


trait SessionEvent

case class Joined(user: JUser) extends SessionEvent

case class Exited(user: JUser) extends SessionEvent

case class UpdateLocation(error: Double, latitude: Double, longitude: Double, altitude: Double) extends SessionEvent

case class JUser(uid: String, name: String) //todo:適当なのでなおす


