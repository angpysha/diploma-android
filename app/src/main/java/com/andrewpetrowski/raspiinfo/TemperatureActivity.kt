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
import android.support.design.widget.Snackbar
import android.support.v7.widget.Toolbar
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import co.zsmb.materialdrawerkt.builders.drawer
import co.zsmb.materialdrawerkt.draweritems.badgeable.primaryItem
import co.zsmb.materialdrawerkt.draweritems.divider
import com.andrewpetrowski.diploma.bridgelib.Models.DHT11_Data
import com.andrewpetrowski.raspiinfo.Controllers.AndroidDHTController
import com.andrewpetrowski.raspiinfo.Helpers.Decrement
import com.andrewpetrowski.raspiinfo.Helpers.Increment
import com.andrewpetrowski.raspiinfo.Helpers.zeroTime
import com.github.pwittchen.swipe.library.rx2.Swipe
import com.github.pwittchen.swipe.library.rx2.SwipeListener
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter
import com.jjoe64.graphview.series.DataPoint
import com.mikepenz.materialdrawer.Drawer
import kotlinx.android.synthetic.main.activity_temperature.*
import java.util.*
import com.jjoe64.graphview.series.LineGraphSeries
import com.mikepenz.fontawesome_typeface_library.FontAwesome
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList


class TemperatureActivity : AppCompatActivity(), View.OnTouchListener, DatePickerDialog.OnDateSetListener {

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        var calendar = Calendar.getInstance()
        calendar.set(year, monthOfYear, dayOfMonth)

        LoadAsync().execute(calendar.time.zeroTime())
    }

    private lateinit var result: Drawer
    private lateinit var cur_date: Date
    private var x1 = 0f
    private var x2 = 0f
    private var y1 = 0f
    private var y2 = 0f
    private lateinit var swipe: Swipe
    // private val temperatures: List<Temperature> = LinkedList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temperature)
        // val intent = Intent(this,TemperatureActivity::class.java)
        // toolbar.setNavigationIcon(R.drawable.ic_menu_black_24dp)
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
        val sdf = DateAsXAxisLabelFormatter(this, SimpleDateFormat("HH:mm"))
        temperature_graph!!.gridLabelRenderer.labelFormatter = sdf
        temperature_scroll!!.setOnTouchListener(this)
        val params = arrayOf(Date())
        LoadAsync().execute(Date())

        sel_date_temp_but!!.setOnClickListener {
            var calendar = Calendar.getInstance()
            calendar.time = cur_date

            val datepickerDialog = DatePickerDialog.newInstance(
                    this@TemperatureActivity,
                    calendar
            )

            datepickerDialog.show(fragmentManager, "Chage date")
        }

        swiperefresh_temperature!!.setOnRefreshListener {
            LoadAsync().execute(cur_date ?: Date().zeroTime())
        }


//        val series = LineGraphSeries(arrayOf<DataPoint>(DataPoint(0.0, 1.0), DataPoint(1.0, 5.0), DataPoint(2.0, 3.0)))
//        temperature_graph!!.addSeries(series)
    }

    fun setChart() {

    }

    override fun onTouch(v: View, event: MotionEvent?): Boolean {
        //    return super.onTouchEvent(event)
        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> {
                x1 = event!!.x
                y1 = event!!.y
                return super.onTouchEvent(event)
            }
            MotionEvent.ACTION_UP -> {
                x2 = event!!.x
                y2 = event!!.y
                val delta = x2 - x1
                val anim = AlphaAnimation(1.0f,0.0f)
                anim.duration = 600
                anim.repeatCount = 1
                anim.repeatMode = Animation.REVERSE
                temperature_scroll!!.startAnimation(anim)
                if (x2 > x1 && Math.abs(delta) > 150) {
                    val animIn = AnimationUtils.loadAnimation(this,R.anim.slide_in_right)
                  //  val animOut = AnimationUtils.loadAnimation(this,R.anim.slide_out_left)
                    Log.d("Swipe:", "Right")
                    //  Snackbar.make(temperature_main_layout,"Swipe right",Snackbar.LENGTH_SHORT)
                    // Toast.makeText(this,"Swipe right",Toast.LENGTH_SHORT)
                    //this.overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right)
                    temperature_scroll!!.startAnimation(animIn)
                  //  temperature_scroll!!.startAnimation(animOut)
                  //  temperature_scroll!!.startAnimation(animOut)
                    LoadAsync().execute(cur_date!!.Decrement())
                } else if (x2 < x1 && Math.abs(delta) > 150) {
                    Log.d("Swipe:", "Left")
                    val animIn = AnimationUtils.loadAnimation(this,R.anim.slide_in_left)
                 //   val animOut = AnimationUtils.loadAnimation(this,R.anim.slide_out_right)
                    //   Snackbar.make(temperature_main_layout,"Swipe left",Snackbar.LENGTH_SHORT)
                    //   Toast.makeText(this,"Swipe left",Toast.LENGTH_SHORT)
                   // this.overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left)
                    temperature_scroll!!.startAnimation(animIn)
                  //  temperature_scroll!!.startAnimation(animOut)
                    LoadAsync().execute(cur_date!!.Increment())
                }
                return super.onTouchEvent(event)
            }

        }
        return super.onTouchEvent(event)

        // return super.onTouchEvent(event)
    }

    inner class LoadAsync : AsyncTask<Date, Void, List<DHT11_Data>>() {
        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg params: Date?): List<DHT11_Data>? {
            val temperatureContorller = AndroidDHTController()

            val date: Date = params[0]!!.zeroTime()
            var data = temperatureContorller.GetByDate(date).sortedBy { it.created_at }
            return data
        }

        override fun onPostExecute(result: List<DHT11_Data>?) {
            super.onPostExecute(result)
            result!!.let {

                var list: MutableList<DataPoint> = ArrayList()

//                var series = LineGraphSeries<DataPoint>()
                result.mapTo(list) { DataPoint(it.created_at, it.temperature.toDouble()) }

                var aas = list.toTypedArray()
                var series = LineGraphSeries(aas)
                temperature_graph!!.removeAllSeries()
                temperature_graph!!.addSeries(series)
                temperature_graph!!.viewport.setMinX(aas!!.get(0)!!.x ?: 0.0)
                if (resources.getInteger(R.integer.num_axis) < list.size - 1)
                    temperature_graph!!.viewport.setMaxX(aas.get(resources.getInteger(R.integer.num_axis)).x ?: 1.0)
                else
                    temperature_graph!!.viewport.setMaxX(aas.get(list.size - 1).x ?: 1.0)
                temperature_graph!!.viewport.setMinY(aas.minBy { it.y }!!.y - 2)
                temperature_graph!!.viewport.setMaxY(aas.maxBy { it.y }!!.y + 2)
                temperature_graph!!.viewport.isXAxisBoundsManual = true
                temperature_graph!!.viewport.isYAxisBoundsManual = true
                //temperature_graph!!.viewport.maxXAxisSize = 1.0
                temperature_graph!!.gridLabelRenderer.numHorizontalLabels = resources.getInteger(R.integer.num_axis) + 1

                temperature_graph!!.viewport.isScalable = true
                //    temperature_graph!!.viewport.isScrollable = true

                val ddf = SimpleDateFormat("MM\\dd\\yyyy")
                temperature_header!!.text = String.format(resources
                        .getString(R.string.temperature_header), ddf.format(result.get(0).created_at))

                cur_date = result.get(0).created_at.zeroTime()
                swiperefresh_temperature!!.isRefreshing = false
            }
        }
    }
}
