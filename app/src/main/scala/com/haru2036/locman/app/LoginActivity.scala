package com.haru2036.locman.app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import dispatch._
import macroid.{ActivityContext, Contexts, IdGeneration}
import org.persona.login.android.PersonaLoginActivity
import scala.concurrent.ExecutionContext.Implicits.global

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
        startActivityForResult(i,1)
    }

    def startMapActivity(implicit ctx: ActivityContext) = {
        val i = new Intent(ctx.get, classOf[MapsActivity])
        startActivityForResult(i,0)
    }

    override def onActivityResult(reqCode: Int, resultCode: Int, i: Intent) = {
        if(resultCode == Activity.RESULT_FIRST_USER && reqCode == 1) {
            Future{
                import com.ning.http.client.AsyncHttpClientConfig
                import com.ning.http.client.providers.netty.NettyAsyncHttpProvider
                new NettyAsyncHttpProvider(new AsyncHttpClientConfig.Builder().build)

                val assertion = i.getExtras.getString("assertion")
                val req = url(Config.withHttp(Config.browserIDCallbackUrl ++ assertion))
                val res = Http(req)
                Config.appSession = Option(res.apply.getCookies.get(0).getValue)
                startMapActivity
            }.apply()
        }
        finish()
    }


}
