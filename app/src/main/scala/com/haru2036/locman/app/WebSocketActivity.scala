package com.haru2036.locman.app

import android.app.Activity
import android.os.Bundle
import android.widget._
import macroid._

class WebSocketActivity extends Activity with Contexts[Activity]{
    lazy val arrayAdapter = new ArrayAdapter[String](getBaseContext, R.layout.item_list_websocket_test, R.id.textView)
    lazy val listView : ListView = {
        val view = new ListView(getApplicationContext)
        arrayAdapter.add("hoge")
        arrayAdapter.add("piyo")
        view.setAdapter(arrayAdapter)
        view
    }

    override def onCreate(savedInstanceState: Bundle): Unit ={
        super.onCreate(savedInstanceState)
        setContentView(listView)
    }
}
