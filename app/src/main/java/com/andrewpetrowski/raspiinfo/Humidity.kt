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

import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import co.zsmb.materialdrawerkt.builders.drawer
import co.zsmb.materialdrawerkt.draweritems.badgeable.primaryItem
import co.zsmb.materialdrawerkt.draweritems.divider
import com.andrewpetrowski.diploma.bridgelib.Models.DHT11_Data
import com.andrewpetrowski.raspiinfo.Controllers.AndroidDHTController
import com.andrewpetrowski.raspiinfo.Helpers.zeroTime
import com.mikepenz.fontawesome_typeface_library.FontAwesome
import com.mikepenz.materialdrawer.Drawer
import kotlinx.android.synthetic.main.activity_humidity.*
import kotlinx.android.synthetic.main.activity_temperature.*
import java.util.*

class Humidity : AppCompatActivity() {

    private lateinit var result: Drawer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_humidity)

        setSupportActionBar(toolbar_h)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.title = resources.getString(R.string.humidity_title)

        result = drawer {
            savedInstance = savedInstanceState
            closeOnClick = false
            headerViewRes = R.layout.drawer_header
            primaryItem(resources.getString(R.string.drawer_home)) {
                iicon = FontAwesome.Icon.faw_home
                onClick { _ ->
                    startActivity(Intent(this@Humidity, Main::class.java))
                    result?.closeDrawer()
                    // result?.setSelection(1)
                    false
                }
                identifier = 1
            }
            primaryItem(resources.getString(R.string.drawer_temperature)) {
                identifier = 2
                iicon = FontAwesome.Icon.faw_thermometer_half
                selected = true
                onClick { _ ->
                    startActivity(Intent(this@Humidity, TemperatureActivity::class.java))
                    result?.closeDrawer()
                    false
                }
            }

            primaryItem(resources.getString(R.string.drawer_humidity)) {
                identifier = 3
                iicon = FontAwesome.Icon.faw_tint
                onClick { _ ->
                    result?.closeDrawer()
                    false
                }
            }
            divider { }
            toolbar = this@Humidity.toolbar_h
        }

        result!!.setSelection(3)
    }

    inner class LoadAsync: AsyncTask<Date,Void,List<DHT11_Data>>() {
        override fun doInBackground(vararg params: Date?): List<DHT11_Data> {
            val temperatureContorller = AndroidDHTController()

            val date: Date = params[0]!!.zeroTime()
            var data = temperatureContorller.GetByDate(date).sortedBy { it.created_at }
            return data
        }

        override fun onPostExecute(result: List<DHT11_Data>?) {
            super.onPostExecute(result)
        }

    }
}
