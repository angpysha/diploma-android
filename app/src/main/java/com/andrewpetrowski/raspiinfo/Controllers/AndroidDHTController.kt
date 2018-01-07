package com.andrewpetrowski.raspiinfo.Controllers

import com.andrewpetrowski.diploma.bridgelib.Controllers.DhtController
import com.andrewpetrowski.diploma.bridgelib.Models.DHT11_Data
import com.andrewpetrowski.diploma.bridgelib.Models.DhtSearch
import com.andrewpetrowski.raspiinfo.Models.Temperature
import java.util.*

/**
 * Created by andre on 30.12.2017.
 */

class AndroidDHTController {
    fun GetByDate(date: Date): List<DHT11_Data> {
        var calendar = Calendar.getInstance()


        calendar.time = date
        calendar.add(Calendar.DATE,1)
        var dayAfter = calendar.time

        var filter = DhtSearch(date,dayAfter,null,null, null,null)

        var rest: List<DHT11_Data> = DhtController().SearchAsync(filter,DHT11_Data::class.java).get()

        return rest
    }

    data class MaxMin(val maxT:Float,val minT: Float,val maxH:Float,val minH: Float)
    fun GetMaxMin(date: Date) : MaxMin {
        var data = GetByDate(date)

        var max = data.maxBy { it.temperature }!!.temperature
        var min = data.minBy { it.temperature }!!.temperature
        var maxH = data.maxBy { it.humidity }!!.humidity
        var minH = data.minBy { it.humidity }!!.humidity

        return MaxMin(max,min,maxH,minH)
    }




}