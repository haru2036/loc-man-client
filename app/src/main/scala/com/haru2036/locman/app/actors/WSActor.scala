package com.haru2036.locman.app.actors

import java.net.URI
import java.util

import akka.actor.{Actor, ActorRef, Props}
import akka.event.Logging
import com.haru2036.locman.app.message.SessionEvent
import com.haru2036.locman.app.message._
import com.neovisionaries.ws.client._
import spray.json._
import EventRootProtocol._

/**
  * Created by 2036 on 2016/01/03.
  */

object WSActor{
    def props (mapActor: ActorRef, uri: URI, cookie: String) = Props(new WSActor(mapActor, uri, cookie))
}

class WSActor(mapActor: ActorRef, uri: URI, cookie: String) extends Actor {

    import UserSessionEventProtocol._
    var log = Logging(context.system, this)
    type WSMessage = String
    lazy val wsclient = new WebSocketFactory().createSocket(uri).addListener(new WebSocketListener{
                override def onPongFrame(websocket: WebSocket, frame: WebSocketFrame): Unit = ???

                override def onFrameSent(websocket: WebSocket, frame: WebSocketFrame): Unit = ???

                override def onCloseFrame(websocket: WebSocket, frame: WebSocketFrame): Unit = ???

                override def handleCallbackError(websocket: WebSocket, cause: Throwable): Unit = ???

                override def onError(websocket: WebSocket, cause: WebSocketException): Unit = log.error(cause, "ws error")

                override def onUnexpectedError(websocket: WebSocket, cause: WebSocketException): Unit = ???

                override def onFrame(websocket: WebSocket, frame: WebSocketFrame): Unit = ???

                override def onConnectError(websocket: WebSocket, cause: WebSocketException): Unit = ???

                override def onBinaryFrame(websocket: WebSocket, frame: WebSocketFrame): Unit = ???

                override def onTextFrame(websocket: WebSocket, frame: WebSocketFrame): Unit = {
                }

                override def onPingFrame(websocket: WebSocket, frame: WebSocketFrame): Unit = ???

                override def onTextMessageError(websocket: WebSocket, cause: WebSocketException, data: Array[Byte]): Unit = ???

                override def onFrameError(websocket: WebSocket, cause: WebSocketException, frame: WebSocketFrame): Unit = ???

                override def onConnected(websocket: WebSocket, headers: util.Map[WSMessage, util.List[WSMessage]]): Unit = log.info("ws connected")

                override def onSendError(websocket: WebSocket, cause: WebSocketException, frame: WebSocketFrame): Unit = ???

                override def onStateChanged(websocket: WebSocket, newState: WebSocketState): Unit = ???

                override def onContinuationFrame(websocket: WebSocket, frame: WebSocketFrame): Unit = ???

                override def onFrameUnsent(websocket: WebSocket, frame: WebSocketFrame): Unit = ???

                override def onTextMessage(websocket: WebSocket, text: WSMessage): Unit = {
                    mapActor ! jsonReader[UserSessionEvent[SessionEvent]].read(JsonParser(text))
                    log.debug("received json :", text)
                }

                override def onBinaryMessage(websocket: WebSocket, binary: Array[Byte]): Unit = ???

                override def onMessageError(websocket: WebSocket, cause: WebSocketException, frames: util.List[WebSocketFrame]): Unit = ???

                override def onDisconnected(websocket: WebSocket, serverCloseFrame: WebSocketFrame, clientCloseFrame: WebSocketFrame, closedByServer: Boolean): Unit = log.info("ws disconnected")

            }).addHeader("Cookie", cookie)

    def receive = {
        case ev: SessionEvent =>
            val json = ev.toJson.compactPrint
            log.info("sending to server: " + json)
            wsclient.sendText(json)
        case x ⇒ log.info("message : " + x.toString)
    }

    override def preStart(): Unit ={
        wsclient.connect()
    }

    override def postStop() = wsclient.disconnect()

}
