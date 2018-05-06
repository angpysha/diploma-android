package com.andrewpetrowski.raspiinfo.Controllers

import com.andrewpetrowski.raspiinfo.Enums.EORMFilter
import com.andrewpetrowski.raspiinfo.Helpers.*
import com.andrewpetrowski.raspiinfo.Models.Temperature
import com.andrewpetrowski.raspiinfo.Records.BMP180
import com.andrewpetrowski.raspiinfo.Records.DHT11
import com.orm.SugarRecord
import io.github.angpysha.diploma_bridge.Controllers.DhtController
import io.github.angpysha.diploma_bridge.Models.DHT11_Data
import io.github.angpysha.diploma_bridge.Models.DhtSearch
import io.github.angpysha.diploma_bridge.Models.DisplayPeriod
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by andre on 30.12.2017.
 */

class AndroidDHTController {
    fun GetByDate(date: Date,internetAccess: Boolean = true): List<DHT11_Data>? {
        var calendar = Calendar.getInstance()


        calendar.time = date
        calendar.add(Calendar.DATE, 1)
        var dayAfter = calendar.time

        var filter = DhtSearch(date, dayAfter, null, null, null, null)

        val dht = DhtController()
        dht.baseUrl = BASE_URL
        var data: MutableList<DHT11_Data>?
        try {
            val localData = SugarRecord.find(DHT11::class.java, "date > ? and date < ?", date.toSQLiteString(),
                    dayAfter.toSQLiteString())
            val tmpDate = Date()
            val tmpCalendar = Calendar.getInstance()
            tmpCalendar.time = tmpDate
            if (localData.isEmpty() || internetAccess) {
                data = dht.SearchAsync(filter, DHT11_Data::class.java).get()
                data!!.forEach { x->
                    var dhtt = DHT11(x.temperature.toDouble(),x.humidity.toDouble(),
                            x.created_at.toSQLiteString())
                    if (!localData.any { xx -> xx.date == dhtt.date })
                        dhtt.save()
                }
            } else {
                data = ArrayList()
                localData.forEach { x ->
                    val date = x.date!!.fromSQLiteString()
                    val dht11_elem = DHT11_Data(date,null,x.temperature!!.toFloat(),
                            x.humidity!!.toFloat())
                    data!!.add(dht11_elem)
                }
            }
        } catch (ex: Exception) {
            return null
        }

        return data
    }

    fun GetByDate(date: Date, type: Int,isInternet : Boolean = true) : List<DHT11_Data>? {
        var calendar = Calendar.getInstance()


        calendar.time = date
        calendar.add(Calendar.DATE, 1)
        var dayAfter = calendar.time
        var rest: MutableList<DHT11_Data>? = ArrayList()
        val dht = DhtController()
        dht.baseUrl = BASE_URL
        when (type) {
            0 -> {
                var filter = DhtSearch(date, dayAfter, null, null, null, null)
                val localData = SugarRecord.find(DHT11::class.java, "date >= ${date.toFormatedString()} and date <= ${dayAfter.toFormatedString()}")
                if (localData.isEmpty() || isInternet) {
                    rest = dht.SearchAsync(filter, DHT11_Data::class.java).get()
                    rest!!.forEach { x ->
                        val dhtt = DHT11(x!!.temperature.toDouble(),x!!.humidity.toDouble(),x!!.created_at.toSQLiteString())

                        if (!localData.any { xx -> xx.date == dhtt.date })
                            dhtt.save()
                    }
                } else {
                    rest = ArrayList()

                    localData.forEach { x ->
                        val date = x!!.date!!.fromSQLiteString()
                        var dht11_elem = DHT11_Data(date,null,x!!.temperature!!.toFloat(),
                                x!!.humidity!!.toFloat())
                        rest!!.add(dht11_elem)
                    }
                }
            }

            1-> {
                val ormdht = ORMDHTController()
                val localdata = ormdht.GetByPeriod(date,EORMFilter.Week,DHT11::class.java)
                if (localdata!!.isEmpty() || isInternet) {
                    rest = dht.GetByPeriod(date, DisplayPeriod.WEEK, DHT11_Data::class.java, DhtSearch::class.java)
                    rest!!.forEach { x ->
                        val dhtt = DHT11(x!!.temperature.toDouble(),x!!.humidity.toDouble(),x!!.created_at.toSQLiteString())

                        if (!localdata.any { xx -> xx.date == dhtt.date })
                            dhtt.save()
                    }
                } else {
                    rest = ArrayList()

                    localdata.forEach { x ->
                        val date = x!!.date!!.fromSQLiteString()
                        var dht11_elem = DHT11_Data(date,null,x!!.temperature!!.toFloat(),
                                x!!.humidity!!.toFloat())
                        rest!!.add(dht11_elem)
                    }
                }
            }

            2-> {
                val ormdht = ORMDHTController()
                val localdata = ormdht.GetByPeriod(date,EORMFilter.Month,DHT11::class.java)
                if (localdata!!.isEmpty() || isInternet) {
                    rest = dht.GetByPeriod(date, DisplayPeriod.MONTH, DHT11_Data::class.java, DhtSearch::class.java)
                    rest!!.forEach { x ->
                        val dhtt = DHT11(x!!.temperature.toDouble(),x!!.humidity.toDouble(),x!!.created_at.toSQLiteString())

                        if (!localdata.any { xx -> xx.date == dhtt.date })
                            dhtt.save()
                    }
                } else {
                    rest = ArrayList()

                    localdata.forEach { x ->
                        val date = x!!.date!!.fromSQLiteString()
                        var dht11_elem = DHT11_Data(date,null,x!!.temperature!!.toFloat(),
                                x!!.humidity!!.toFloat())
                        rest!!.add(dht11_elem)
                    }
                }
            }

            3 -> {
                rest = dht.GetByPeriod(date,DisplayPeriod.YEAR,DHT11_Data::class.java,DhtSearch::class.java)

            }
        }



        return rest
    }


    data class MaxMin(val maxT: Float, val minT: Float, val maxH: Float, val minH: Float)

    fun GetMaxMin(date: Date): MaxMin? {
        val isInetrnet = Additionals.IsInternetConnection()
        var data = GetByDate(date,isInetrnet)

        if (data == null || data.isEmpty()) {
            return MaxMin(0f,0f,0f,0f)
        }

        var max = data!!.maxBy { it.temperature }!!.temperature
        var min = data!!.minBy { it.temperature }!!.temperature
        var maxH = data!!.maxBy { it.humidity }!!.humidity
        var minH = data!!.minBy { it.humidity }!!.humidity

        return MaxMin(max, min, maxH, minH)
    }
}