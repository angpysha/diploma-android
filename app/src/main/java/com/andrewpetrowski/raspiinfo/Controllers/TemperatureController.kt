package com.andrewpetrowski.raspiinfo.Controllers

import com.andrewpetrowski.diploma.bridgelib.Controllers.DhtController
import com.andrewpetrowski.diploma.bridgelib.Models.DHT11_Data
import com.andrewpetrowski.diploma.bridgelib.Models.DhtSearch
import com.andrewpetrowski.raspiinfo.Models.Temperature
import java.util.*

/**
 * Created by andre on 30.12.2017.
 */

class TemperatureController() {
    fun GetByDate(date: Date): List<Temperature>? {
        var calendar = Calendar.getInstance()


        calendar.time = date
        calendar.add(Calendar.DATE,1)
        var dayAfter = calendar.time

        var filter = DhtSearch(date,dayAfter,null,null, null,null)

        var result = DhtController().SearchAsync(filter,DHT11_Data::class.java).get()

        val data = result.mapTo(LinkedList<Temperature>()) { it -> Temperature(it.temperature,it.created_at) }

        return data
    }


}