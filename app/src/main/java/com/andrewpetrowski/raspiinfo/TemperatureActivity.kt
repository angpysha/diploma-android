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

import android.app.FragmentManager
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import co.zsmb.materialdrawerkt.builders.drawer
import co.zsmb.materialdrawerkt.draweritems.badgeable.primaryItem
import co.zsmb.materialdrawerkt.draweritems.divider
import com.andrewpetrowski.diploma.bridgelib.Controllers.DhtController
import com.andrewpetrowski.raspiinfo.Adapters.TemperatureFragmentAdapter
import com.andrewpetrowski.raspiinfo.Models.FragmentAdapterParams
import com.github.pwittchen.swipe.library.rx2.Swipe
import com.mikepenz.materialdrawer.Drawer
import kotlinx.android.synthetic.main.activity_temperature.*
import java.util.*
import com.mikepenz.fontawesome_typeface_library.FontAwesome
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog


class TemperatureActivity : AppCompatActivity()/*, View.OnTouchListener*/, DatePickerDialog.OnDateSetListener {

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        var calendar = Calendar.getInstance()
        calendar.set(year, monthOfYear, dayOfMonth)

        // LoadAsync().execute(calendar.time.zeroTime())
    }

    private lateinit var result: Drawer
    private lateinit var cur_date: Date
    private var x1 = 0f
    private var x2 = 0f
    private var y1 = 0f
    private var y2 = 0f
    private var size = 0
    private lateinit var swipe: Swipe
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temperature)
        setSupportActionBar(toolbar_d)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.title = resources.getString(R.string.temperature_title)
        result = drawer {
            savedInstance = savedInstanceState
            closeOnClick = false
            headerViewRes = R.layout.drawer_header
            primaryItem(resources.getString(R.string.drawer_home)) {
                iicon = FontAwesome.Icon.faw_home
                onClick { _ ->
                    startActivity(Intent(this@TemperatureActivity, Main::class.java))
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
                    result?.closeDrawer()
                    false
                }
            }
            primaryItem(resources.getString(R.string.drawer_humidity)) {
                identifier = 3
                iicon = FontAwesome.Icon.faw_tint
                onClick { _ ->
                    val intent: Intent = Intent(this@TemperatureActivity, Humidity::class.java)
                    startActivity(intent)
                    result?.closeDrawer()
                    false
                }
            }
            divider { }
            toolbar = this@TemperatureActivity.toolbar_d
        }

        result?.setSelection(2)

        val _size = LoadSize(this).execute().get()
//        val adapter = GetAdapter().execute(FragmentAdapterParams(supportFragmentManager,_size)).get()
        val adapter = TemperatureFragmentAdapter(supportFragmentManager, _size)

        pager_temperature!!.adapter = adapter
        pager_temperature!!.currentItem = _size - 1

    }

    fun setChart() {

    }


    inner class LoadSize(context: Context) : AsyncTask<Void, Void, Int>() {
        private lateinit var context: Context

        init {
            this.context = context
        }

        override fun doInBackground(vararg params: Void?): Int {
            val dht = DhtController()
            val size = dht.GetDatesCount()
            return size
        }

    }

    inner class GetAdapter : AsyncTask<FragmentAdapterParams, Void, TemperatureFragmentAdapter>() {
        override fun doInBackground(vararg params: FragmentAdapterParams?): TemperatureFragmentAdapter {
            val param = params[0]

            return TemperatureFragmentAdapter(param!!.manager, param!!.size)
        }

    }
}
