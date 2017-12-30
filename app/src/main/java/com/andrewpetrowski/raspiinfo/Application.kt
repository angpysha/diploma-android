package com.andrewpetrowski.raspiinfo

import android.content.Context
import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication

/**
 * Created by andre on 28.12.2017.
 */

class Application : MultiDexApplication() {
    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        MultiDex.install(this)
    }
}