package com.andrewpetrowski.raspiinfo.Controllers

import com.andrewpetrowski.raspiinfo.Models.Temperature
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
    fun GetByDate(date: Date): List<DHT11_Data>? {
        var calendar = Calendar.getInstance()


        calendar.time = date
        calendar.add(Calendar.DATE, 1)
        var dayAfter = calendar.time

        var filter = DhtSearch(date, dayAfter, null, null, null, null)

        var rest: List<DHT11_Data>? = DhtController().SearchAsync(filter, DHT11_Data::class.java).get()

        return rest
    }

    fun GetByDate(date: Date, type: Int) : List<DHT11_Data>? {
        var calendar = Calendar.getInstance()


        calendar.time = date
        calendar.add(Calendar.DATE, 1)
        var dayAfter = calendar.time
        var rest: List<DHT11_Data>? = ArrayList()
        when (type) {
            0 -> {
                var filter = DhtSearch(date, dayAfter, null, null, null, null)
                rest = DhtController().SearchAsync(filter, DHT11_Data::class.java).get()
            }

            1-> {
                rest = DhtController().GetByPeriod(date,DisplayPeriod.WEEK,DHT11_Data::class.java,DhtSearch::class.java)
            }

            2-> {
                rest = DhtController().GetByPeriod(date,DisplayPeriod.MONTH,DHT11_Data::class.java,DhtSearch::class.java)
            }

            3 -> {
                rest = DhtController().GetByPeriod(date,DisplayPeriod.YEAR,DHT11_Data::class.java,DhtSearch::class.java)

            }
        }



        return rest
    }


    data class MaxMin(val maxT: Float, val minT: Float, val maxH: Float, val minH: Float)

    fun GetMaxMin(date: Date): MaxMin? {
        var data = GetByDate(date)

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