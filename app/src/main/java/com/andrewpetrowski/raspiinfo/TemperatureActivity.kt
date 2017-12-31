package com.andrewpetrowski.raspiinfo

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View
import co.zsmb.materialdrawerkt.builders.drawer
import co.zsmb.materialdrawerkt.draweritems.badgeable.primaryItem
import co.zsmb.materialdrawerkt.draweritems.divider
import com.mikepenz.materialdrawer.Drawer
import kotlinx.android.synthetic.main.activity_main.*

class TemperatureActivity : AppCompatActivity() {
    private lateinit var result: Drawer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temperature)

        val dtoolbar = findViewById<View>(R.id.toolbar) as Toolbar
        // val intent = Intent(this,TemperatureActivity::class.java)
        // toolbar.setNavigationIcon(R.drawable.ic_menu_black_24dp)
        setSupportActionBar(dtoolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)
        result = drawer {
            savedInstance = savedInstanceState
            closeOnClick = false
            primaryItem("Home") {
                onClick { _ ->
                    startActivity(Intent(this@TemperatureActivity,Main::class.java))
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
            divider {  }
            toolbar = this@TemperatureActivity.toolbar
        }

        result?.setSelection(2)
    }
}
