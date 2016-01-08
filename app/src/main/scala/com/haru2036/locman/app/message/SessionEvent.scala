package com.haru2036.locman.app.message

import spray.json._


/**
  * Created by 2036 on 2016/01/04.
  */



object UpdateLocationProtocol extends DefaultJsonProtocol{
    implicit object UpdateLocation extends RootJsonFormat[UpdateLocation]{
        override def write(ul: UpdateLocation) = JsObject(
            "accuracy" -> JsNumber(ul.accuracy),
            "latitude" -> JsNumber(ul.latitude),
            "longitude" -> JsNumber(ul.longitude),
            "altitude" -> JsNumber(ul.altitude)
        )

        override def read(json: JsValue): UpdateLocation = {
             json.asJsObject.getFields("accuracy", "latitude", "longitude", "altitude") match {
                case Seq(JsNumber(accuracy), JsNumber(latitude), JsNumber(longitude), JsNumber(altitude)) =>
                    new UpdateLocation(accuracy.toDouble, latitude.toDouble, longitude.toDouble, altitude.toDouble)
                case _ => throw new DeserializationException("ks")
            }
        }
    }
}

object UserSessionEventProtocol extends DefaultJsonProtocol{
    import EventRootProtocol._
    import JUserProtocol._

    implicit object UserSessionEventFormat extends JsonFormat[UserSessionEvent] {
        override def write(obj: UserSessionEvent): JsValue = JsObject(
            "event" -> SessionEvent.write(obj.event),
            "author" -> juserFormat.write(obj.author)
        )

        override def read(json: JsValue): UserSessionEvent = {
             json.asJsObject.getFields("event", "author") match {
                case Seq(JsObject(event), JsObject(author)) =>
                    new UserSessionEvent(SessionEvent.read(event.toJson), juserFormat.read(author.toJson))
                case _ => throw new DeserializationException("ks")
            }
        }
    }
}



object JUserProtocol extends DefaultJsonProtocol{
    implicit val juserFormat = jsonFormat2(JUser)
}


object EventRootProtocol extends DefaultJsonProtocol{
    implicit object SessionEvent extends JsonFormat[SessionEvent] {
        override def write(obj: SessionEvent): JsValue = obj match {
            case updateLocation: UpdateLocation => JsObject(
                "tag" -> JsString("UpdateLocation"),
                "contents" -> UpdateLocationProtocol.UpdateLocation.write(updateLocation)
            )
            case joined: Joined => JsObject(
                "tag" -> JsString("Joined"),
                "contents" -> JsArray()
            )
            case exited: Exited=> JsObject(
                "tag" -> JsString("Exited"),
                "contents" -> JsArray()
            )
        }

        override def read(json: JsValue) = {
            json.asJsObject.getFields("tag", "contents") match{
                case Seq(JsString(tag), JsObject(contents)) => tag match{
                    case "UpdateLocation" => UpdateLocationProtocol.UpdateLocation.read(contents.toJson)
                    case "Joined" => new Joined()
                    case "Exited" => new Exited()
                }
            }
        }
    }
}


trait SessionEvent

case class UserSessionEvent(event: SessionEvent, author: JUser)

case class Joined() extends SessionEvent

case class Exited() extends SessionEvent

case class UpdateLocation(accuracy: Double, latitude: Double, longitude: Double, altitude: Double) extends SessionEvent

case class JUser(uid: String, name: String) //todo:適当なのでなおす


