package com.haru2036.locman.app

import java.net.URI
import java.util

import android.app.Activity
import android.os.{AsyncTask, Handler, Bundle}
import android.util.Log
import android.widget._
import com.neovisionaries.ws.client._
import macroid._
import scalaz.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global


class WebSocketActivity extends Activity with Contexts[Activity] {
    lazy val arrayAdapter = new ArrayAdapter[String](getBaseContext, R.layout.item_list_websocket_test, R.id.textView)
    lazy val listView : ListView = {
        val view = new ListView(getApplicationContext)
        arrayAdapter.add("hoge")
        arrayAdapter.add("piyo")
        view.setAdapter(arrayAdapter)
        view
    }
    var wsClient: Option[WebSocket] = None
    type WSMessage = String

    override def onCreate(savedInstanceState: Bundle): Unit ={
        super.onCreate(savedInstanceState)
        setContentView(listView)
    }

    override def onResume()={
        super.onResume()
        Future(connect(new URI("ws://192.168.1.22:3000/session/ws/1"))).start
        wsClient.map(x=>{
            x.sendText("""{¥"tag¥":¥"Joined¥",¥"contents¥":{¥"uid¥":¥"hoge¥",¥"name¥":¥"hoge¥"}}""")
        })
    }

    override def onPause()={
        super.onPause()
        wsClient.map(x => x.sendClose())
    }

    override def onStop(): Unit ={
        super.onStop()
    }


    def connect(uri: URI): Unit = {
        arrayAdapter.add("connecting to server")
        arrayAdapter.notifyDataSetChanged()
        Log.d("hoge", "running websockets...")
        wsClient = Option(wsClient.getOrElse {
            new WebSocketFactory().createSocket(uri).addListener(new WebSocketListener{
                override def onPongFrame(websocket: WebSocket, frame: WebSocketFrame): Unit = ???

                override def onFrameSent(websocket: WebSocket, frame: WebSocketFrame): Unit = ???

                override def onCloseFrame(websocket: WebSocket, frame: WebSocketFrame): Unit = ???

                override def handleCallbackError(websocket: WebSocket, cause: Throwable): Unit = ???

                override def onError(websocket: WebSocket, cause: WebSocketException): Unit = ???

                override def onUnexpectedError(websocket: WebSocket, cause: WebSocketException): Unit = ???

                override def onFrame(websocket: WebSocket, frame: WebSocketFrame): Unit = ???

                override def onConnectError(websocket: WebSocket, cause: WebSocketException): Unit = ???

                override def onBinaryFrame(websocket: WebSocket, frame: WebSocketFrame): Unit = ???

                override def onTextFrame(websocket: WebSocket, frame: WebSocketFrame): Unit = {
                    arrayAdapter.add(frame.getPayloadText)
                    arrayAdapter.notifyDataSetChanged()
                    listView.invalidateViews()
                }

                override def onPingFrame(websocket: WebSocket, frame: WebSocketFrame): Unit = ???

                override def onTextMessageError(websocket: WebSocket, cause: WebSocketException, data: Array[Byte]): Unit = ???

                override def onFrameError(websocket: WebSocket, cause: WebSocketException, frame: WebSocketFrame): Unit = ???

                override def onConnected(websocket: WebSocket, headers: util.Map[WSMessage, util.List[WSMessage]]): Unit = ???

                override def onSendError(websocket: WebSocket, cause: WebSocketException, frame: WebSocketFrame): Unit = ???

                override def onStateChanged(websocket: WebSocket, newState: WebSocketState): Unit = ???

                override def onContinuationFrame(websocket: WebSocket, frame: WebSocketFrame): Unit = ???

                override def onFrameUnsent(websocket: WebSocket, frame: WebSocketFrame): Unit = ???

                override def onTextMessage(websocket: WebSocket, text: WSMessage): Unit = ???

                override def onBinaryMessage(websocket: WebSocket, binary: Array[Byte]): Unit = ???

                override def onMessageError(websocket: WebSocket, cause: WebSocketException, frames: util.List[WebSocketFrame]): Unit = ???

                override def onDisconnected(websocket: WebSocket, serverCloseFrame: WebSocketFrame, clientCloseFrame: WebSocketFrame, closedByServer: Boolean): Unit = ???

            })
        }.connect())
    }
}
