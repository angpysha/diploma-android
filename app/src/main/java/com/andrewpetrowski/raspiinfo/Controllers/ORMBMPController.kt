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

import com.andrewpetrowski.raspiinfo.Helpers.*
import com.andrewpetrowski.raspiinfo.Records.BMP180
import com.orm.SugarRecord
import io.github.angpysha.diploma_bridge.Decorators.DateEx
import net.danlew.android.joda.JodaTimeAndroid
import org.joda.time.Days
import java.util.*

/**
 * Created by andre on 09.04.2018.
 */

class ORMBMPController() : ORMBase<BMP180>() {

    public override fun GetMinMaxDate(): Array<Date> {
        val elems = SugarRecord.listAll(BMP180::class.java)
        elems.sortBy { it.date }
        val date1 = elems.first().date.fromSQLiteString()
        val date2 = elems.last().date.fromSQLiteString()
        var dates: Array<Date> = arrayOf(date1,date2)
        return dates
    }

    override fun GetAverage(elems: MutableList<BMP180>?, pos: Int): BMP180 {
        var temperature: Double = 0.0
        var altitude: Double = 0.0
        var pressure: Double = 0.0

        for (elem in elems!!.iterator()) {
            temperature += elem!!.temperature!!
            altitude += elem!!.altitude!!
            pressure += elem!!.pressure!!

        }

        altitude /= elems.count()
        temperature /= elems.count()
        pressure /= elems.count()

        var date1 = Date()

        if (elems.isNotEmpty()) {
            date1 = elems[0]!!.date!!.fromSQLiteString()

        } else {
            date1 = DateEx(date1).AddDate(-pos)
            if (temperature.isNaN())
                temperature = 0.0
            if (altitude.isNaN())
                altitude = 0.0
            if (pressure.isNaN())
                altitude = 0.0
        }

        return BMP180(pressure, temperature, altitude, date1.toSQLiteString())

    }

    fun getDatesCount(): Int {
        val elems = SugarRecord.listAll(BMP180::class.java)
        elems.sortBy { it.date }
        val date1 = elems.first().date.fromSQLiteString()
        val date2 = elems.last().date.fromSQLiteString()
        val days = Additionals.DaysDiff(date1 = date1,date2 = date2 )
        return days
    }
}