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
    private lateinit var data : MutableMap<Int,Date>
    init {
        data = LinkedHashMap<Int,Date>()
        var incr = size-1
        var date = Date()
        while (incr >= 0) {
            if (checkAvailable(date)) {
            data.put(incr,date)
            incr--
            date = date.Decrement()
            } else {
                date = date.Decrement()
            }
        }
    }

    fun Get(page: Int) : Date? {
        val entr = data.get(page)

        return entr
    }

    companion object {
        fun GetDate(pos: Int,size: Int) : Date {
            var minus = size-pos
            val adh = AndroidDHTController()
            var cal = Calendar.getInstance()

            cal.add(Calendar.DATE,-minus)

//            while (minus >=0) {
//                 cal.add(Calendar.DATE,-1)
//                if (adh.GetByDate(cal.time.zeroTime()).isNotEmpty())
//                minus--
//            }
            return cal.time.zeroTime()
        }
    }
}