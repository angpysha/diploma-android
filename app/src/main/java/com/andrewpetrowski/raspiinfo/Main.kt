package com.andrewpetrowski.raspiinfo

import android.content.Context
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.multidex.MultiDex
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Example of a call to a native method
        sample_text.text = stringFromJNI()
        LoadAsync().execute()

        swiperefresh?.setOnRefreshListener {
            LoadAsync().execute()
        }

        DrawerBuilder().withActivity(this).build()

        // Handle Toolbar
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
       // toolbar.setNavigationIcon(R.drawable.ic_menu_black_24dp)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)
    //    supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        val item1 = PrimaryDrawerItem().withIdentifier(1).withName("Home")

        val result = DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .addDrawerItems(
                        item1
                )
                .build()
       // result.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);

        //layoutInflater: LayoutInflater
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
                sample_text.text = String.format(resources.getString(R.string.last_date), df.format(result.created_at))
                swiperefresh.isRefreshing = false
            }
        }


    }
}
