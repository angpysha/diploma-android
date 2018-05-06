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

import com.andrewpetrowski.raspiinfo.Helpers.Additionals
import com.andrewpetrowski.raspiinfo.Helpers.ORMBase
import com.andrewpetrowski.raspiinfo.Helpers.fromSQLiteString
import com.andrewpetrowski.raspiinfo.Helpers.toSQLiteString
import com.andrewpetrowski.raspiinfo.Records.DHT11
import com.orm.SugarRecord
import io.github.angpysha.diploma_bridge.Decorators.DateEx
import io.github.angpysha.diploma_bridge.Models.DHT11_Data
import java.util.*

/**
 * Created by andre on 09.04.2018.
 */

class ORMDHTController : ORMBase<DHT11>() {
    public override fun GetMinMaxDate(): Array<Date> {
        val elems = SugarRecord.listAll(DHT11::class.java)
        elems.sortBy { it.date }
        val date1 = elems.first().date!!.fromSQLiteString()
        val date2 = elems.last().date!!.fromSQLiteString()
        var dates: Array<Date> = arrayOf(date1,date2)
        return dates
    }

    override fun GetAverage(elems: MutableList<DHT11>?, pos: Int): DHT11 {
        var temperature: Double = 0.0
        var humidity: Double = 0.0

        for (elem in elems!!.iterator()) {
            temperature += elem!!.temperature!!
            humidity += elem!!.humidity!!

        }

        temperature /= elems.count()
        humidity /= elems.count()

        var date1 = Date()

        if (elems.isNotEmpty()) {
            date1 = elems[0]!!.date!!.fromSQLiteString()

        } else {
            date1 = DateEx(date1).AddDate(-pos)
            if (temperature.isNaN())
                temperature = 0.0
            if (humidity.isNaN())
                humidity = 0.0
        }

        return DHT11(temperature,humidity,date1.toSQLiteString())
    }

    fun getDatesCount(): Int {
        val elems = SugarRecord.listAll(DHT11::class.java)
        elems.sortBy { it.date }
        val date1 = elems.first().date!!.fromSQLiteString()
        val date2 = elems.last().date!!.fromSQLiteString()
        val days = Additionals.DaysDiff(date1 = date1,date2 = date2 )
        return days
    }

}