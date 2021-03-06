/*
 * Copyright 2018 Andrew Petrowsky
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.andrewpetrowski.raspiinfo

import android.content.Context
import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication
import com.orm.SugarApp
import io.socket.client.IO
import io.socket.client.Socket
import net.danlew.android.joda.JodaTimeAndroid

/**
 * Created by andre on 28.12.2017.
 */

class Application : SugarApp() {
    private lateinit var socket : Socket

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        MultiDex.install(this)

    }

    override fun onCreate() {
        super.onCreate()
        JodaTimeAndroid.init(this)
        socket = IO.socket("https://raspi-info-bot.herokuapp.com/").connect()
    }

    fun getSocket() : Socket {
        return this.socket
    }
}