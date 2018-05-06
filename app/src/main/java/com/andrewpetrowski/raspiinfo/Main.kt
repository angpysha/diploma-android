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
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import android.os.AsyncTask
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.multidex.MultiDex
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.support.v4.view.LayoutInflaterCompat
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import co.zsmb.materialdrawerkt.builders.DrawerBuilderKt
import co.zsmb.materialdrawerkt.builders.drawer
import co.zsmb.materialdrawerkt.builders.footer
import co.zsmb.materialdrawerkt.draweritems.badgeable.primaryItem
import co.zsmb.materialdrawerkt.draweritems.divider
import com.afollestad.materialdialogs.MaterialDialog
import com.andrewpetrowski.raspiinfo.Controllers.AndroidBMPController
import com.andrewpetrowski.raspiinfo.Controllers.AndroidDHTController
import com.andrewpetrowski.raspiinfo.Helpers.*
import com.andrewpetrowski.raspiinfo.Models.AllData
import com.andrewpetrowski.raspiinfo.Models.PressureDataClass
import com.andrewpetrowski.raspiinfo.Records.BMP180
import com.andrewpetrowski.raspiinfo.Records.DHT11
import com.mikepenz.fontawesome_typeface_library.FontAwesome
import com.mikepenz.fontawesome_typeface_library.FontAwesome.Icon.faw_home
import com.mikepenz.iconics.Iconics
import com.mikepenz.iconics.context.IconicsLayoutInflater
import com.mikepenz.iconics.context.IconicsLayoutInflater2
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.orm.SugarRecord
import io.github.angpysha.diploma_bridge.Controllers.BmpController
import io.github.angpysha.diploma_bridge.Controllers.DhtController
import io.github.angpysha.diploma_bridge.Models.Bmp180_Data
import io.github.angpysha.diploma_bridge.Models.BmpSearch
import io.github.angpysha.diploma_bridge.Models.DHT11_Data
import io.github.angpysha.diploma_bridge.Models.DhtSearch
import io.socket.emitter.Emitter


class Main : AppCompatActivity() {

    private lateinit var result: Drawer
    private var dhtLoaded = false
    private var dhtmaxminLoaded = false
    private var bmploaded = false
    private lateinit var prefs: SharedPreferences

    private lateinit var progress: MaterialDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        LayoutInflaterCompat.setFactory2(layoutInflater, IconicsLayoutInflater2(delegate))
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        try {
            progress = MaterialDialog.Builder(parent)
                    .title(resources.getString(R.string.progress_title))
                    .content(resources.getString(R.string.progress_content))
                    .progress(true, 0)
                    .show()
        } catch (ex: Exception) {

        }

        LoadAsync().execute()

        swiperefresh?.setOnRefreshListener {
            LoadAsync().execute()
        }

        // Handle Toolbar
        val dtoolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(dtoolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)
        result = drawer {
            savedInstance = savedInstanceState
            closeOnClick = false
            headerViewRes = R.layout.drawer_header

            primaryItem(resources.getString(R.string.drawer_home)) {
                identifier = 1
                selected = true
                iicon = FontAwesome.Icon.faw_home
                onClick { _ ->
                    result?.closeDrawer()
                    false
                }
            }
            primaryItem(resources.getString(R.string.drawer_temperature)) {
                identifier = 2
                iicon = FontAwesome.Icon.faw_thermometer_half
                onClick { _ ->
                    val intent: Intent = Intent(this@Main, TemperatureActivity::class.java)
                    startActivity(intent)
                    result?.closeDrawer()

                    //result?.setSelection(2)
                    false

                }
            }

            primaryItem(resources.getString(R.string.drawer_humidity)) {
                identifier = 3
                iicon = FontAwesome.Icon.faw_tint
                onClick { _ ->
                    val intent: Intent = Intent(this@Main, Humidity::class.java)
                    startActivity(intent)
                    result?.closeDrawer()
                    false
                }
            }

            primaryItem(resources.getString(R.string.drawer_pressure)) {
                identifier = 4
                iicon = FontAwesome.Icon.faw_tachometer
                onClick { _ ->
                    val intent: Intent = Intent(this@Main, Pressure::class.java)
                    startActivity(intent)
                    result?.closeDrawer()
                    false
                }

            }
            divider { }

            primaryItem(resources.getString(R.string.about)) {
                identifier = 5
                iicon = FontAwesome.Icon.faw_info
            }
            toolbar = this@Main.toolbar
        }

        result!!.setSelection(1)

        LoadMaxMin().execute()
        LoadPressure(applicationContext).execute()

        val app = application as Application

        val socket = app.getSocket()

        socket.on("dataupdated", DataUpdated)
        val top_anim = AnimationUtils.loadAnimation(applicationContext,R.anim.slide_in_top)
        val bottom_anim = AnimationUtils.loadAnimation(applicationContext,R.anim.slide_out_bottom)

        get_now_item!!.setOnClickListener {
            fab2!!.close(true)
            socket.emit("updatedata", "all")
            swiperefresh!!.isRefreshing = true
            Snackbar.make(fab2,resources.getString(R.string.data_updated),Snackbar.LENGTH_LONG).show()
        }

        get_last_item!!.setOnClickListener {
            fab2!!.close(true)
            LoadAsync().execute()
            LoadPressure(applicationContext).execute()
            swiperefresh!!.isRefreshing = true
        }
        fab!!.setOnClickListener {
            //            socket.emit("updatedata", "all")
//            swiperefresh!!.isRefreshing = true
            if (float_menu!!.visibility == View.INVISIBLE) {
                float_menu!!.visibility = View.VISIBLE
//                float_menu!!.startAnimation(top_anim)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    fab!!.setImageDrawable(ContextCompat.getDrawable(this,  R.drawable.ic_clear_black_24dp))
                } else {
                    fab!!.setImageDrawable(resources.getDrawable(R.drawable.ic_clear_black_24dp))

                }

            } else
                float_menu!!.visibility = View.INVISIBLE
                fab!!.setImageResource(R.drawable.update)
//                float_menu!!.startAnimation(bottom_anim)
        }

        fab_from_server!!.setOnClickListener {
            float_menu!!.visibility = View.INVISIBLE
            LoadAsync().execute()
            LoadPressure(applicationContext).execute()
            swiperefresh!!.isRefreshing = true

        }

        fab_now!!.setOnClickListener {
            float_menu!!.visibility = View.INVISIBLE
            socket.emit("updatedata", "all")
            swiperefresh!!.isRefreshing = true
            Snackbar.make(constrait_main,"Data updated",Snackbar.LENGTH_LONG).show()
        }
        prefs = getSharedPreferences(SHARED_PREFSNAME,0)
        val isFirstRun = prefs!!.getBoolean(IS_FIRST_RUN,true)
        if (isFirstRun) {
            FirstLoad().execute()
        }
    }


    private val DataUpdated: Emitter.Listener = Emitter.Listener {
        LoadAsync().execute()
        LoadPressure(applicationContext).execute()
    }


    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {

        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }


    inner class LoadAsync : AsyncTask<Void, Void, DHT11_Data>() {
        override fun onPreExecute() {
            super.onPreExecute()

        }

        override fun doInBackground(vararg p0: Void?): DHT11_Data {
            try {
                val dhtController: DhtController = DhtController()
                dhtController.baseUrl = BASE_URL
                var dhtData: DHT11_Data = DHT11_Data(25f, 11f)

                dhtData = dhtController.GetLast(DHT11_Data::class.java)


                //      val dhtData: DHT11_Data = DHT11_Data(23f, 48f)

                return dhtData
            } catch (ex: Exception) {
                val i = 0
                Log.d("Error:", ex.message.toString())
                return DHT11_Data()
            }
        }

        override fun onPostExecute(result: DHT11_Data?) {
            super.onPostExecute(result)

            result?.let {
                lastHumidity.text = String.format(resources.getString(R.string.humidity), result.humidity)
                lastTemperature.text = String.format(resources.getString(R.string.temperature), result.temperature)
                val df = SimpleDateFormat("MM/dd/yyyy HH:mm")
                dateText.text = String.format(resources.getString(R.string.last_date), result.created_at.ToLocal())
                swiperefresh.isRefreshing = false

                dhtLoaded = true
            }
        }
    }

    inner class LoadMaxMin : AsyncTask<Void, Void, AndroidDHTController.MaxMin?>() {
        override fun doInBackground(vararg params: Void): AndroidDHTController.MaxMin? {
            val controller = AndroidDHTController()

            var data:AndroidDHTController.MaxMin? = controller.GetMaxMin(Date().zeroTime())

            return data
        }

        override fun onPostExecute(result: AndroidDHTController.MaxMin?) {
            super.onPostExecute(result)

            result!!.let {
                val maxT = result!!.maxT
                val minT = result!!.minT

                val maxH = result!!.maxH
                val minH = result!!.minH

                maxTemperature!!.text = String.format(resources.getString(R.string.maximum_temperature), maxT)
                minTemperature!!.text = String.format(resources.getString(R.string.minimum_temperature), minT)

                maxHumidity!!.text = String.format(resources.getString(R.string.maximum_humidity), maxH)
                minHumidity!!.text = String.format(resources.getString(R.string.minimum_humidity), minH)

                dhtmaxminLoaded = true
            }
        }
    }

    inner class LoadPressure(context: Context) : AsyncTask<Void, Void, PressureDataClass?>() {
        private lateinit var _context: Context

        init {
            _context = context
        }
        override fun doInBackground(vararg params: Void?): PressureDataClass? {
            val controller = AndroidBMPController()

            return controller.GetMaxMinLast(Date().zeroTime(),_context)
        }

        override fun onPostExecute(result: PressureDataClass?) {
            super.onPostExecute(result)
            result!!.let {
                lastPressure!!.text = String.format(resources.getString(R.string.pressure), result!!.pressure / 1000f)
                minPressure!!.text = String.format(resources.getString(R.string.min_pressure), result.minPressure / 1000f)
                maxPressure!!.text = String.format(resources.getString(R.string.max_pressure), result.minPressure / 1000f)
            }
            bmploaded = true

            if (bmploaded && dhtLoaded) {
//                progress_main!!.visibility = View.GONE
                try {
                    progress.hide()
                } catch (ex: Exception) {
                    Log.d("Error", ex.message)
                }
            }

        }
    }

    inner class FirstLoad : AsyncTask<Boolean,Void,AllData?>() {
        override fun doInBackground(vararg p0: Boolean?): AllData? {
            val dhtcontroller = DhtController()
            dhtcontroller.baseUrl = BASE_URL
            val dhtDates = dhtcontroller.GetMinMaxDate()
            val dhtFIlter = DhtSearch(dhtDates[0],dhtDates[1])
            val bmpController = BmpController()
            bmpController.baseUrl = BASE_URL
            val bmpDates = bmpController.GetMinMaxDate()
            var bmpSearchFIlter = BmpSearch(bmpDates[0],bmpDates[1])
            val dht11 = dhtcontroller.Search(dhtFIlter,DHT11_Data::class.java)
            val bmp180 = bmpController.Search(bmpSearchFIlter,Bmp180_Data::class.java)

           val dat = AllData(bmp180,dht11)
            dat!!.bmp!!.forEach { x ->
                var bmp = BMP180(x!!.pressure.toDouble(),x!!.temperature.toDouble(),
                        x!!.altitude.toDouble(),x!!.created_at.toSQLiteString())
                bmp.save()
            }

            dat!!.dht!!.forEach { x ->
                var dht = DHT11(x!!.temperature.toDouble(), x!!.humidity.toDouble(),
                        x!!.created_at.toSQLiteString())
                dht.save()
            }
            return dat
        }

        override fun onPostExecute(result: AllData?) {
            super.onPostExecute(result)
            val edit = prefs!!.edit()
            edit.putBoolean(IS_FIRST_RUN,false)
            edit.apply()
        }

    }
}
