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

package com.andrewpetrowski.raspiinfo.Helpers

import com.orm.SugarRecord
import com.andrewpetrowski.raspiinfo.Enums.EORMFilter
import io.github.angpysha.diploma_bridge.Decorators.DateEx
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by andre on 09.04.2018.
 */

open abstract class ORMBase<T> where T: SugarRecord<T> {
    fun GetByPeriod(date: Date, period: EORMFilter,tClass: Class<T>) : MutableList<T>? {
        when (period) {
            EORMFilter.Week -> {
                val tmp = date
                val calendar = Calendar.getInstance()
                calendar.time = date
                val dayOfWeek: Int = DateEx.GetLocalWeekDay(calendar.get(Calendar.DAY_OF_WEEK),
                        calendar.firstDayOfWeek)
                val data: MutableList<T>? = FilterData(dayOfWeek,tmp,tClass)
                return data
            }

            EORMFilter.Month -> {
                val tmp = date
                val calendar = Calendar.getInstance()
                calendar.time = date
                val dayOfWeek: Int = calendar.get(Calendar.DAY_OF_MONTH)
                val data: MutableList<T>? = FilterData(dayOfWeek,tmp,tClass)
                return data
            }

            EORMFilter.Year -> {
                val tmp = date
                val calendar = Calendar.getInstance()
                calendar.time = date
                val dayOfWeek: Int = calendar.get(Calendar.MONTH)
                val data: MutableList<T>? = FilterData(dayOfWeek,tmp,tClass)
                return data
            }
        }

        return null
    }

    private fun FilterData(numElements: Int,date: Date,tClass: Class<T>):MutableList<T>? {
            var dateMutable = date
            val dat : MutableList<T>? = ArrayList()
        for (i in 0 until numElements) {
            val dayAfter = dateMutable.Increment()
//            val localData = SugarRecord.find(BMP180::class.java,"date >= ? and date <= ?", date.time.toString(),dayAfter.time.toString())
            val localData = SugarRecord.find(tClass,"date >= ? and date <= ?",date.time.toString(),
                    dayAfter.time.toString())
            val aver: T = GetAverage(localData,i)
            dat!!.add(aver)
            dateMutable = date.Decrement()
        }

        return dat
    }

    protected abstract fun GetAverage(elems: MutableList<T>?,pos: Int) : T
}