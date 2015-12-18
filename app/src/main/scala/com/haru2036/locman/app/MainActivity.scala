package com.haru2036.locman.app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.{Button, LinearLayout}
import macroid._
import macroid.FullDsl._


/**
  * Created by haru2036 on 2015/12/13.
  */
class MainActivity extends Activity with Contexts[Activity]{
    override def onCreate(savedInstanceState: Bundle): Unit ={
        super.onCreate(savedInstanceState)
        setContentView{
            getUi{
                l[LinearLayout](
                    w[Button] <~ text("WebSocket") <~ On.click {
                        startActivity(new Intent(this, classOf[WebSocketActivity]))
                        Ui(Unit)
                    },
                    w[Button] <~ text("Maps") <~ On.click {
                        startActivity(new Intent(this, classOf[MapsActivity]))
                        Ui(Unit)
                    }
                ) <~ vertical
            }
        }
    }
}

