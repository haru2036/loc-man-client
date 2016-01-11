package com.haru2036.locman.app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import dispatch._
import Defaults._
import macroid.{ActivityContext, Contexts, IdGeneration}
import org.persona.login.android.PersonaLoginActivity

/**
  * Created by 2036 on 2016/01/11.
  */
class LoginActivity extends Activity with Contexts[Activity] with IdGeneration{
    override def onCreate(savedInstanceState: Bundle): Unit ={
        super.onCreate(savedInstanceState)
        startPersonaActivity
    }

    def startPersonaActivity(implicit ctx: ActivityContext) = {
        val i = new Intent(ctx.get, classOf[PersonaLoginActivity])
        startActivityForResult(i,0)
    }

    def startMapActivity(implicit ctx: ActivityContext) = {
        val i = new Intent(ctx.get, classOf[MapsActivity])
        startActivityForResult(i,0)
    }

    override def onActivityResult(reqCode: Int, resultCode: Int, i: Intent) = {

        val assertion = i.getExtras.getString("assertion")
        val req = url(Config.withHttp(Config.browserIDCallbackUrl ++ assertion))
        val res = Http(req)
        Config.appSession = Option(res.apply.getCookies.get(0).getValue)
        startMapActivity
    }


}
