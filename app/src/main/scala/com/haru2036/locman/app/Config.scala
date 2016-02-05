package com.haru2036.locman.app

/**
  * Created by 2036 on 2016/01/11.
  */
object Config{
    val serverBaseURL = "locman.haru2036.com"
    val loginUrl = serverBaseURL ++ "/auth/login"
    val browserIDCallbackUrl = serverBaseURL ++ "/auth/page/browserid/"
    var appSession: Option[String] = None
    def withHttp(url: String) = "http://" ++ url
    def withWS(url: String) = "ws://" ++ url
    def wsEndPoint (sessionId: String) = withWS(serverBaseURL ++ "/api/session/ws/" ++ sessionId)
}
