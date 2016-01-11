/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.persona.login.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;

public class BrowserIDInterface {
    Activity activity;

    public BrowserIDInterface(Activity context){
        activity = context;
    }

    @JavascriptInterface
    public void onAssertion(String assertion) {
        Log.d("BrowserIDInterface", "we got an assetion!");
        Intent i = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("assertion", assertion);
        i.putExtras(bundle);
        activity.setResult(Activity.RESULT_OK, i);
        activity.finish();
    }
}
