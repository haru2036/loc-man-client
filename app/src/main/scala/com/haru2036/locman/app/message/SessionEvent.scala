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

    implicit object UserSessionEventFormat extends JsonFormat[UserSessionEvent[SessionEvent]] {
        override def write(obj: UserSessionEvent[SessionEvent]): JsValue = JsObject(
            "event" -> SessionEvent.write(obj.event),
            "author" -> jUserFormat.write(obj.author)
        )

        override def read(json: JsValue): UserSessionEvent[SessionEvent] = {
             json.asJsObject.getFields("event", "author") match {
                case Seq(JsObject(event), JsObject(author)) =>
                    new UserSessionEvent(SessionEvent.read(event.toJson), jUserFormat.read(author.toJson))
                case _ => throw new DeserializationException("ks")
            }
        }
    }
}



object JUserProtocol extends DefaultJsonProtocol{
    implicit object jUserFormat extends JsonFormat[JUser] {
        override def write(obj: JUser): JsValue = JsObject(
            "uid" -> JsString(obj.uid),
            "name" -> JsString(obj.name)
        )

        override def read(json: JsValue): JUser = {
            json.asJsObject.getFields("uid", "name") match {
                case Seq(JsString(uid), JsString(name)) =>
                    new JUser(uid, name)
                case _ => throw new DeserializationException("ks")
            }
        }
    }
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

case class UserSessionEvent[T <: SessionEvent](event: T, author: JUser)

case class Joined() extends SessionEvent

case class Exited() extends SessionEvent

case class UpdateLocation(accuracy: Double, latitude: Double, longitude: Double, altitude: Double) extends SessionEvent

case class JUser(uid: String, name: String) //todo:適当なのでなおす


