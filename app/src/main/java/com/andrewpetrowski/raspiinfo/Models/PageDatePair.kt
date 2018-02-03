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

package com.andrewpetrowski.raspiinfo.Models

import com.andrewpetrowski.raspiinfo.Controllers.AndroidDHTController
import com.andrewpetrowski.raspiinfo.Helpers.Decrement
import com.andrewpetrowski.raspiinfo.Helpers.checkAvailable
import com.andrewpetrowski.raspiinfo.Helpers.zeroTime
import java.util.*
import kotlin.collections.LinkedHashMap

/**
 * Created by andre on 07.01.2018.
 */

class PageDatePair(val size: Int) {
    private lateinit var data: MutableMap<Int, Date>

    init {
        data = LinkedHashMap<Int, Date>()
        var incr = size - 1
        var date = Date()
        while (incr >= 0) {
            if (checkAvailable(date)) {
                data.put(incr, date)
                incr--
                date = date.Decrement()
            } else {
                date = date.Decrement()
            }
        }
    }

    fun Get(page: Int): Date? {
        val entr = data.get(page)

        return entr
    }

    companion object {
        fun GetDate(pos: Int, size: Int, type: Int): Date {
            var minus = size - pos - 1
            val adh = AndroidDHTController()
            var cal = Calendar.getInstance()
            cal.time = Date()

            when (type) {
                0 -> cal.add(Calendar.DATE, -minus)
                1 -> cal.add(Calendar.WEEK_OF_YEAR,-minus)
                2->cal.add(Calendar.MONTH,-minus)
                3->cal.add(Calendar.YEAR,-minus)
            }
            return cal.time.zeroTime()
        }

        fun GetDate(pos: Int, size: Int): Date {
            return GetDate(pos,size,0)
        }

        fun GetByDate(date: Date, size: Int): Int {
            var diff = size - ((Date().zeroTime().time - date.zeroTime().time) / (1000 * 60 * 60 * 24)).toInt() - 1

            return diff
        }
    }
}