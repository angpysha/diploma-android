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

package com.andrewpetrowski.raspiinfo.Controllers

import android.content.Context
import com.andrewpetrowski.raspiinfo.Enums.EORMFilter
import com.andrewpetrowski.raspiinfo.Helpers.*
import com.andrewpetrowski.raspiinfo.Models.PressureDataClass
import com.andrewpetrowski.raspiinfo.Records.BMP180
import com.orm.SugarRecord
import io.github.angpysha.diploma_bridge.Controllers.BmpController
import io.github.angpysha.diploma_bridge.Models.Bmp180_Data
import io.github.angpysha.diploma_bridge.Models.BmpSearch
import io.github.angpysha.diploma_bridge.Models.DisplayPeriod
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by andre on 23.01.2018.
 */

class AndroidBMPController {

    //TODO: CHECK FOR NULL
    fun GetByDate(date: Date, internetAccess: Boolean = true): List<Bmp180_Data>? {
        var calendar = Calendar.getInstance()


        calendar.time = date.zeroTime()
        calendar.add(Calendar.DATE, 1)
        var dayAfter = calendar.time

        val filter = BmpSearch(date.zeroTime(), dayAfter, null, null,
                null, null, null, null)
        val bmp = BmpController()

        bmp.baseUrl = BASE_URL
        var data: MutableList<Bmp180_Data>?
        try {
            val localData = SugarRecord.find(BMP180::class.java, "date > ? and date < ?", date.toSQLiteString(),
                    dayAfter.toSQLiteString())
            //val localData2= localData.filter { it -> it.date!! >  date && it.date!! < dayAfter.time }
            val all = SugarRecord.listAll(BMP180::class.java)
            val tmpDate = Date()
            val tmpCalendar = Calendar.getInstance()
            tmpCalendar.time = tmpDate
            val hours = tmpCalendar.get(Calendar.HOUR_OF_DAY)
            if (localData.isEmpty() || internetAccess) {
                data = bmp.SearchAsync(filter, Bmp180_Data::class.java).get()
                data.forEach { x ->
                    var bmpp = BMP180(x!!.pressure.toDouble(), x!!.temperature.toDouble(),
                            x!!.altitude.toDouble(), x!!.created_at.toSQLiteString())
                    if (!localData.any { x -> x.date == bmpp.date })
                        bmpp.save()
                }

            } else {
                data = ArrayList<Bmp180_Data>()

                localData.forEach { x ->
                    val date = x.date!!.fromSQLiteString()
                    val bmp180_elem = Bmp180_Data(date, null, x!!.temperature!!.toFloat(),
                            x!!.altitude!!.toFloat(), x!!.pressure!!.toFloat())
                    data!!.add(bmp180_elem)
                }
            }
        } catch (ex: Exception) {
//            data = bmp.SearchAsync(filter, Bmp180_Data::class.java).get()
//            data.forEach { x ->
//                var bmpp = BMP180(x!!.pressure.toDouble(), x!!.temperature.toDouble(),
//                        x!!.altitude.toDouble(), x!!.created_at.toSQLiteString())
//                bmpp.save()
//            }
            return null
        }
        return data
    }

    fun GetByDate(date: Date, type: Int, internetAccess: Boolean = true): List<Bmp180_Data>? {
        var calendar = Calendar.getInstance()


        calendar.time = date
        calendar.add(Calendar.DATE, 1)
        var dayAfter = calendar.time
        var rest: MutableList<Bmp180_Data>? = ArrayList()
        val bmp = BmpController()
        bmp.baseUrl = BASE_URL
        when (type) {
            0 -> {
                val filer = BmpSearch(date, dayAfter, null, null, null, null, null, null)
                val localData = SugarRecord.find(BMP180::class.java, "date >= ${date.toFormatedString()} and date <= ${dayAfter.toFormatedString()}")
                if (localData.isEmpty() || internetAccess) {
                    rest = bmp.SearchAsync(filer, Bmp180_Data::class.java).get()
                    rest.forEach { x ->
                        var bmpp = BMP180(x!!.pressure.toDouble(), x!!.temperature.toDouble(),
                                x!!.altitude.toDouble(), x!!.created_at.toSQLiteString())
                        if (!localData.any { x -> x.date == bmpp.date })
                            bmpp.save()

                    }
                } else {
                    rest = ArrayList<Bmp180_Data>()

                    localData.forEach { x ->
                        val date = x!!.date.fromSQLiteString()
                        val bmp180_elem = Bmp180_Data(date, null, x!!.temperature!!.toFloat(),
                                x!!.altitude!!.toFloat(), x!!.pressure!!.toFloat())
                        rest!!.add(bmp180_elem)
                    }
                }

            }

            1 -> {
                val ormbmp = ORMBMPController()
                val localdata = ormbmp.GetByPeriod(date, EORMFilter.Week, BMP180::class.java);
                if (localdata!!.isEmpty() || internetAccess) {
                    rest = bmp.GetByPeriod(date, DisplayPeriod.WEEK, Bmp180_Data::class.java, BmpSearch::class.java)
                    rest.forEach { x ->
                        var bmpp = BMP180(x!!.pressure.toDouble(), x!!.temperature.toDouble(),
                                x!!.altitude.toDouble(), x!!.created_at.toSQLiteString())
                        if (!localdata.any { x -> x.date == bmpp.date })
                            bmpp.save()

                    }
                } else {
                    rest = ArrayList()
                    localdata.forEach { x ->
                        val date = x!!.date.fromSQLiteString()
                        val bmp180_elem = Bmp180_Data(date, null, x!!.temperature!!.toFloat(),
                                x!!.altitude!!.toFloat(), x!!.pressure!!.toFloat())
                        rest!!.add(bmp180_elem)
                    }
                }
            }

            2 -> {
                val ormbmp = ORMBMPController()
                val localdata = ormbmp.GetByPeriod(date, EORMFilter.Month, BMP180::class.java)
                if (localdata!!.isEmpty()) {
                    rest = bmp.GetByPeriod(date, DisplayPeriod.MONTH, Bmp180_Data::class.java, BmpSearch::class.java)
                    rest.forEach { x ->
                        var bmpp = BMP180(x!!.pressure.toDouble(), x!!.temperature.toDouble(),
                                x!!.altitude.toDouble(), x!!.created_at.toSQLiteString())
                        if (!localdata.any { x -> x.date == bmpp.date })
                            bmpp.save()

                    }
                } else {
                    rest = ArrayList()
                    localdata.forEach { x ->
                        val date = x!!.date.fromSQLiteString()
                        val bmp180_elem = Bmp180_Data(date, null, x!!.temperature!!.toFloat(),
                                x!!.altitude!!.toFloat(), x!!.pressure!!.toFloat())
                        rest!!.add(bmp180_elem)
                    }
                }
            }

            3 -> {
                val ormbmp = ORMBMPController()
                val localdata = ormbmp.GetByPeriod(date, EORMFilter.Year, BMP180::class.java)
                if (localdata!!.isEmpty()) {
                    rest = bmp.GetByPeriod(date, DisplayPeriod.YEAR, Bmp180_Data::class.java, BmpSearch::class.java)
                    rest.forEach { x ->
                        var bmpp = BMP180(x!!.pressure.toDouble(), x!!.temperature.toDouble(),
                                x!!.altitude.toDouble(), x!!.created_at.toSQLiteString())
                        if (!localdata.any { x -> x.date == bmpp.date })
                            bmpp.save()

                    }
                } else {
                    rest = ArrayList()
                    localdata.forEach { x ->
                        val date = x!!.date.fromSQLiteString()
                        val bmp180_elem = Bmp180_Data(date, null, x!!.temperature!!.toFloat(),
                                x!!.altitude!!.toFloat(), x!!.pressure!!.toFloat())
                        rest!!.add(bmp180_elem)
                    }
                }
            }
        }

        return rest
    }

    fun GetMaxMinLast(date: Date,context: Context): PressureDataClass? {
        val isInternet = Additionals.IsInternetConnection(context)
        var data = GetByDate(date,isInternet)
        data?.let {
            val min = data!!.minBy { it.pressure }!!.pressure

            val max = data.maxBy { it.pressure }!!.pressure

            val now = data.sortedBy { it.created_at }.last().pressure

            return PressureDataClass(now, max, min)
        }
        return PressureDataClass(0f, 0f, 0f)
    }
}