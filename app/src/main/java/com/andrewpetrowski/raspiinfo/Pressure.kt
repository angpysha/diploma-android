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
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import co.zsmb.materialdrawerkt.builders.drawer
import co.zsmb.materialdrawerkt.draweritems.badgeable.primaryItem
import co.zsmb.materialdrawerkt.draweritems.divider
import com.afollestad.materialdialogs.MaterialDialog
import com.mikepenz.fontawesome_typeface_library.FontAwesome
import com.mikepenz.materialdrawer.Drawer
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_pressure.*
import com.andrewpetrowski.raspiinfo.Adapters.PressureFragmentAdapter
import io.github.angpysha.diploma_bridge.Controllers.BmpController
import com.andrewpetrowski.raspiinfo.Helpers.Additionals

class Pressure : AppCompatActivity(),AdapterView.OnItemSelectedListener,
ViewPager.OnPageChangeListener{
    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {
//        progress = MaterialDialog.Builder(this)
//                .title(resources.getString(R.string.progress_title))
//                .content(resources.getString(R.string.progress_content))
//                .progress(true,0)
//                .show()
    }

    lateinit var progress: MaterialDialog

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        progress = MaterialDialog.Builder(this)
                .title(resources.getString(R.string.progress_title))
                .content(resources.getString(R.string.progress_content))
                .progress(true,0)
                .show()
        when (position) {
            0 -> {
                val _size = LoadSize(this).execute(0).get()
//        val adapter = GetAdapter().execute(FragmentAdapterParams(supportFragmentManager,_size)).get()
                val adapter = PressureFragmentAdapter(supportFragmentManager, _size)

                pager_pressure!!.adapter = adapter
                pager_pressure!!.currentItem = _size - 1


            }

            1 -> {
                val _size = LoadSize(this).execute(1).get()
//        val adapter = GetAdapter().execute(FragmentAdapterParams(supportFragmentManager,_size)).get()
                val adapter = PressureFragmentAdapter(supportFragmentManager, _size, 1)

                pager_pressure!!.adapter = adapter
                pager_pressure!!.currentItem = _size - 1


            }

            2 -> {
                val _size = LoadSize(this).execute(2).get()
//        val adapter = GetAdapter().execute(FragmentAdapterParams(supportFragmentManager,_size)).get()
                val adapter = PressureFragmentAdapter(supportFragmentManager, _size, 2)

                pager_pressure!!.adapter = adapter
                pager_pressure!!.currentItem = _size - 1


            }
            3 -> {
                val _size = LoadSize(this).execute(3).get()
//        val adapter = GetAdapter().execute(FragmentAdapterParams(supportFragmentManager,_size)).get()
                val adapter = PressureFragmentAdapter(supportFragmentManager, _size, 3)

                pager_pressure!!.adapter = adapter
                pager_pressure!!.currentItem = _size - 1


            }
        }
    }

    private lateinit var result: Drawer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pressure)

        setSupportActionBar(toolbar_p)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.title = resources.getString(R.string.pressure_title)

        result = drawer {
            savedInstance = savedInstanceState
            closeOnClick = false
            headerViewRes = R.layout.drawer_header

            primaryItem(resources.getString(R.string.drawer_home)) {
                identifier = 1
                selected = true
                iicon = FontAwesome.Icon.faw_home
                onClick { _ ->
                    val intent: Intent = Intent(this@Pressure, Main::class.java)
                    startActivity(intent)
                    result?.closeDrawer()
                    false
                }
            }
            primaryItem(resources.getString(R.string.drawer_temperature)) {
                identifier = 2
                iicon = FontAwesome.Icon.faw_thermometer_half
                onClick { _ ->
                    val intent: Intent = Intent(this@Pressure, TemperatureActivity::class.java)
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
                    val intent: Intent = Intent(this@Pressure,Humidity::class.java)
                    startActivity(intent)
                    result?.closeDrawer()
                    false
                }
            }

            primaryItem(resources.getString(R.string.drawer_pressure)) {
                identifier = 4
                iicon = FontAwesome.Icon.faw_tachometer
                onClick { _ ->
                    result?.closeDrawer()
                    false
                }

            }
            divider { }
            toolbar = this@Pressure.toolbar_p
        }

        result!!.setSelection(4)

        val spinner_adapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(this,
                R.array.spinner_filter, android.R.layout.simple_spinner_item)

        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner_pressure!!.adapter = spinner_adapter
        spinner_pressure!!.onItemSelectedListener = this

        pager_pressure!!.addOnPageChangeListener(this)
    }

    inner class LoadSize(context:Context): AsyncTask<Int,Void,Int>() {
        private lateinit var context: Context

        init {
            this.context = context
        }

        override fun doInBackground(vararg params: Int?): Int {
            var size = 0
            when (params[0]) {
                0 -> {
                    val bmp = BmpController()
                    size = bmp.GetDatesCount()
                }

                1 -> {
                    val bmp = BmpController()
                    val dates = bmp.GetMinMaxDate()

                    size = Additionals.WeeksDiff(dates[0],dates[1])+2
                }

                2 -> {
                    val bmp = BmpController()
                    val dates = bmp.GetMinMaxDate()

                    size = Additionals.MonthDiff(dates[0],dates[1])+2
                }

                3 -> {
                    val bmp = BmpController()
                    val dates = bmp.GetMinMaxDate()

                    size = Additionals.YearsDiff(dates[0],dates[1])+2
                }
            }
            return size
        }

    }

}
