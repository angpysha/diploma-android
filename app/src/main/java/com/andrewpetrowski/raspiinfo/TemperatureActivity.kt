package com.andrewpetrowski.raspiinfo

import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View
import co.zsmb.materialdrawerkt.builders.drawer
import co.zsmb.materialdrawerkt.draweritems.badgeable.primaryItem
import co.zsmb.materialdrawerkt.draweritems.divider
import com.andrewpetrowski.diploma.bridgelib.Models.DHT11_Data
import com.andrewpetrowski.raspiinfo.Controllers.AndroidDHTController
import com.andrewpetrowski.raspiinfo.Helpers.zeroTime
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter
import com.jjoe64.graphview.series.DataPoint
import com.mikepenz.materialdrawer.Drawer
import kotlinx.android.synthetic.main.activity_temperature.*
import java.util.*
import com.jjoe64.graphview.series.LineGraphSeries
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList


class TemperatureActivity : AppCompatActivity() {
    private lateinit var result: Drawer
    // private val temperatures: List<Temperature> = LinkedList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temperature)

        val dtoolbar = findViewById<View>(R.id.toolbar_d) as Toolbar
        // val intent = Intent(this,TemperatureActivity::class.java)
        // toolbar.setNavigationIcon(R.drawable.ic_menu_black_24dp)
        setSupportActionBar(toolbar_d)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)
        result = drawer {
            savedInstance = savedInstanceState
            closeOnClick = false
            primaryItem("Home") {
                onClick { _ ->
                    startActivity(Intent(this@TemperatureActivity, Main::class.java))
                    result?.closeDrawer()
                    // result?.setSelection(1)
                    false
                }
                identifier = 1
            }
            primaryItem("Temperature") {
                identifier = 2
                selected = true
                onClick { _ ->
                    result?.closeDrawer()
                    false
                }
            }
            divider { }
            toolbar = this@TemperatureActivity.toolbar_d
        }

        result?.setSelection(2)
        val df: DateAsXAxisLabelFormatter = DateAsXAxisLabelFormatter(this, SimpleDateFormat("HH:mm"))
        // temperature_graph!!.viewport.setMinY(0.0)
        // temperature_graph!!.viewport.isScalable = true
        // temperature_graph!!.gridLabelRenderer.numHorizontalLabels=5
        // temperature_graph!!.viewport.isScrollable = true
        temperature_graph!!.gridLabelRenderer.labelFormatter = df

        LoadAsync().execute()

//        val series = LineGraphSeries(arrayOf<DataPoint>(DataPoint(0.0, 1.0), DataPoint(1.0, 5.0), DataPoint(2.0, 3.0)))
//        temperature_graph!!.addSeries(series)
    }

    fun setChart() {

    }

    inner class LoadAsync : AsyncTask<Void, Void, List<DHT11_Data>>() {
        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg params: Void?): List<DHT11_Data>? {
            val temperatureContorller = AndroidDHTController()

            val date: Date = Date().zeroTime()

            var calendar = Calendar.getInstance()

            calendar.time = date
            calendar.add(Calendar.DATE, -2)
            var newDate = calendar.time
            var data = temperatureContorller.GetByDate(calendar.time)
            return data
        }

        override fun onPostExecute(result: List<DHT11_Data>) {
            super.onPostExecute(result)
            result?.let {
                var list: MutableList<DataPoint> = ArrayList()

//                var series = LineGraphSeries<DataPoint>()
                result.mapTo(list) { DataPoint(it.created_at, it.temperature.toDouble()) }

                var aas = list.toTypedArray()
                var series = LineGraphSeries(aas)

//                var datee = result[0].created_at.zeroTime();
//
//                var calendar = Calendar.getInstance()
//
//                calendar.time = datee
//
//                calendar.set(Calendar.HOUR_OF_DAY,23)
//                calendar.set(Calendar.MINUTE,59)
//                calendar.set(Calendar.SECOND,59)
//                var maxX = calendar.time


                temperature_graph!!.addSeries(series)
                temperature_graph!!.viewport.setMinX(aas.get(0).x)
                temperature_graph!!.viewport.setMaxX(aas.get(resources.getInteger(R.integer.num_axis)).x)
                temperature_graph!!.viewport.setMinY(aas.minBy { it.y }!!.y - 2)
                temperature_graph!!.viewport.setMaxY(aas.maxBy { it.y }!!.y + 2)
                temperature_graph!!.viewport.isXAxisBoundsManual = true
                //temperature_graph!!.viewport.maxXAxisSize = 1.0
                temperature_graph!!.gridLabelRenderer.numHorizontalLabels = resources.getInteger(R.integer.num_axis) + 1

                temperature_graph!!.viewport.isScalable = true
                //    temperature_graph!!.viewport.isScrollable = true

                val ddf = SimpleDateFormat("MM\\dd\\yyyy")
                temperature_header!!.text = String.format(resources
                        .getString(R.string.temperature_header), ddf.format(result.get(0).created_at))
            }
        }
    }
}
