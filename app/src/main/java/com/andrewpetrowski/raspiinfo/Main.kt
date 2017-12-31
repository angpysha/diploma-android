package com.andrewpetrowski.raspiinfo

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.multidex.MultiDex
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import co.zsmb.materialdrawerkt.builders.DrawerBuilderKt
import co.zsmb.materialdrawerkt.builders.drawer
import co.zsmb.materialdrawerkt.draweritems.badgeable.primaryItem
import co.zsmb.materialdrawerkt.draweritems.divider
import com.andrewpetrowski.diploma.bridgelib.Controllers.DhtController
import com.andrewpetrowski.diploma.bridgelib.Models.DHT11_Data
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem
import com.mikepenz.materialdrawer.model.DividerDrawerItem


class Main : AppCompatActivity() {

    private lateinit var result: Drawer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
            primaryItem("Home") {
                identifier = 1
                selected = true
                onClick { _ ->
                    result?.closeDrawer()
                    false
                }
            }
            primaryItem("Temperature") {
                identifier = 2
                onClick { _ ->
                    val intent: Intent = Intent(this@Main, TemperatureActivity::class.java)
                    startActivity(intent)
                    result?.closeDrawer()
                    //result?.setSelection(2)
                    false

                }
            }
            divider { }
            toolbar = this@Main.toolbar
        }
        
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
                dateText.text = String.format(resources.getString(R.string.last_date), df.format(result.created_at))
                swiperefresh.isRefreshing = false
            }
        }


    }
}
