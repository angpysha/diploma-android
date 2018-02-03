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

import com.andrewpetrowski.raspiinfo.Helpers.zeroTime
import com.andrewpetrowski.raspiinfo.Models.PressureDataClass
import io.github.angpysha.diploma_bridge.Controllers.BmpController
import io.github.angpysha.diploma_bridge.Models.Bmp180_Data
import io.github.angpysha.diploma_bridge.Models.BmpSearch
import io.github.angpysha.diploma_bridge.Models.DisplayPeriod
import java.util.*

/**
 * Created by andre on 23.01.2018.
 */

class AndroidBMPController {

    //TODO: CHECK FOR NULL
    fun GetByDate(date: Date): List<Bmp180_Data>? {
        var calendar = Calendar.getInstance()


        calendar.time = date.zeroTime()
        calendar.add(Calendar.DATE, 1)
        var dayAfter = calendar.time

        val filter = BmpSearch(date.zeroTime(), dayAfter, null, null,
                null, null, null, null)

        val data = BmpController().SearchAsync(filter, Bmp180_Data::class.java).get()

        return data
    }

    fun GetByDate(date: Date,type: Int): List<Bmp180_Data> {
        var calendar = Calendar.getInstance()


        calendar.time = date
        calendar.add(Calendar.DATE, 1)
        var dayAfter = calendar.time
        var rest: List<Bmp180_Data> = ArrayList()

        when(type) {
            0-> {
                val filer = BmpSearch(date,dayAfter,null,null,null,null,null,null)

               rest = BmpController().SearchAsync(filer,Bmp180_Data::class.java).get()
            }

            1 -> {
                rest = BmpController().GetByPeriod(date,DisplayPeriod.WEEK,Bmp180_Data::class.java,BmpSearch::class.java)
            }

            2 -> {
                rest = BmpController().GetByPeriod(date,DisplayPeriod.MONTH,Bmp180_Data::class.java,BmpSearch::class.java)
            }

            3 -> {
                rest = BmpController().GetByPeriod(date,DisplayPeriod.YEAR,Bmp180_Data::class.java,BmpSearch::class.java)
            }
        }

        return rest
    }

    fun GetMaxMinLast(date: Date): PressureDataClass {
        var data = GetByDate(date)
      //  data?.let {
            val min = data!!.minBy { it.pressure }!!.pressure

            val max = data.maxBy { it.pressure }!!.pressure

            val now = data.sortedBy { it.created_at }.last().pressure

            return PressureDataClass(now, max, min)
      //  }
        //return PressureDataClass(0f, 0f, 0f)
    }
}