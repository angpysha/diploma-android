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

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.os.AsyncTask
import android.widget.RemoteViews
import com.andrewpetrowski.raspiinfo.Helpers.BASE_URL
import com.andrewpetrowski.raspiinfo.Helpers.ToLocal
import com.andrewpetrowski.raspiinfo.Models.WidgetResult
import io.github.angpysha.diploma_bridge.Controllers.BmpController
import io.github.angpysha.diploma_bridge.Controllers.DhtController
import io.github.angpysha.diploma_bridge.Models.Bmp180_Data
import io.github.angpysha.diploma_bridge.Models.DHT11_Data
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat

/**
 * Created by andre on 24.02.2018.
 */

class StateWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(context: Context?, appWidgetManager: AppWidgetManager?, appWidgetIds: IntArray?) {
        val size = appWidgetIds!!.size

        for (i in 0..(size-1)) {
            val widgetId = i

            var view: RemoteViews = RemoteViews(context!!.packageName, R.layout.widget_layout)

            view.setTextViewText(R.id.temperature_text, "fdasfsa")
            val data = GetData(view, context, appWidgetManager, appWidgetIds[i]).execute().get()
            val temperature = String.format(context!!.resources.getString(R.string.temperature), data!!.temperature)
            val humidity = String.format(context!!.resources.getString(R.string.humidity), data!!.humidity)
            val pressure = String.format(context!!.resources.getString(R.string.pressure), data!!.pressure/1000f)


            view.setTextViewText(R.id.temperature_text, temperature)
            view.setTextViewText(R.id.humidity_text, humidity)
            view.setTextViewText(R.id.pressure_text, pressure)
            view.setTextViewText(R.id.widget_date,data!!.time)
            appWidgetManager!!.updateAppWidget(appWidgetIds[i], view)

        }

        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }

    inner class GetData(views: RemoteViews, context: Context?, widgetManager: AppWidgetManager?, widgetId: Int) : AsyncTask<Void, Void, WidgetResult>() {
        private lateinit var _views: RemoteViews
        private var _context: Context?
        private var _widgetManager: AppWidgetManager?
        private var _widgetId: Int

        init {
            this._views = views
            this._context = context
            this._widgetManager = widgetManager
            this._widgetId = widgetId
        }

        override fun doInBackground(vararg params: Void?): WidgetResult {
            val dhtController: DhtController = DhtController()
            dhtController.baseUrl = BASE_URL
            val dhtdata = dhtController.GetLast(DHT11_Data::class.java)

            val bmpController = BmpController()
            bmpController.baseUrl = BASE_URL
            val bmpdata = bmpController.GetLast(Bmp180_Data::class.java)


            val ttime = dhtdata!!.created_at.ToLocal()
            val data = WidgetResult(dhtdata!!.temperature, dhtdata!!.humidity, bmpdata!!.pressure,ttime)


            return data
        }



//        override fun onPostExecute(result: WidgetResult?) {
//            super.onPostExecute(result)
//
//            result?.let {
//                val temperature = String.format(_context!!.resources.getString(R.string.temperature),result!!.temperature)
//                val humidity = String.format(_context!!.resources.getString(R.string.humidity),result!!.humidity)
//                val pressure = String.format(_context!!.resources.getString(R.string.pressure),result!!.pressure)
//
//                _views.setTextViewText(R.id.temperature_text,temperature)
//                _views.setTextViewText(R.id.humidity_text,humidity)
//                _views.setTextViewText(R.id.pressure_text,pressure)
//
//                _widgetManager!!.updateAppWidget(_widgetId,_views)
//            }
//        }

    }
}