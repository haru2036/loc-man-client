/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.persona.login.android;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.webkit.*;
import android.widget.Toast;
import com.haru2036.locman.app.Config;

@SuppressLint("SetJavaScriptEnabled")
public class PersonaLoginActivity extends Activity {

    protected WebView mWebView;
    protected final String SIGNIN_URL = "https://login.persona.org/sign_in#NATIVE";

    // This is the name our JS interface becomes on window
    private static final String GLOBAL_OBJECT_NAME = "__personaAndroid";
    private static final String CALLBACK = "function __personaAndroidCallback(assertion) { " + GLOBAL_OBJECT_NAME + ".onAssertion(assertion); }";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupWebView();
    }

    @Override
    public void onResume() {
        super.onResume();
        mWebView.loadUrl(SIGNIN_URL);
    }

    protected void setupWebView() {
        // Let's display the progress in the activity title bar, like the
        // browser app does.
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        mWebView = new WebView(this);
        setContentView(mWebView);

        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);

        mWebView.addJavascriptInterface(new BrowserIDInterface(this), GLOBAL_OBJECT_NAME);

        final Activity activity = this;
        mWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                // Activities and WebViews measure progress with different scales.
                // The progress meter will automatically disappear when we reach 100%
                activity.setProgress(progress * 1000);
            }
        });
        mWebView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(activity, "Oh no! " + description, Toast.LENGTH_SHORT).show();
            }

            public void onPageFinished(WebView view, String url) {
                if (url.equals(SIGNIN_URL)) {
                    Log.d("LoginActivity", GLOBAL_OBJECT_NAME);
                    String cmd = "javascript:BrowserID.internal.get('http://" + Config.serverBaseURL() + "', " + CALLBACK + ");";
                    Log.d("LoginActivity", cmd);
                    mWebView.loadUrl(cmd);
                }
            }
        });
    }

}