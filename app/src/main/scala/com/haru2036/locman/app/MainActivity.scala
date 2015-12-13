package com.haru2036.locman.app

import org.scaloid.common._

/**
  * Created by haru2036 on 2015/12/13.
  */
class MainActivity extends SActivity {
  onCreate {
    contentView = new SLinearLayout {
      SButton(R.string.app_name, startActivity(SIntent[MapsActivity]))
    }
  }
}

