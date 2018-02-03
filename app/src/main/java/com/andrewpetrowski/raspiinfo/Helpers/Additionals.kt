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

import org.joda.time.DateTime
import org.joda.time.Months
import org.joda.time.Weeks
import org.joda.time.Years
import java.time.Month
import java.time.Year
import java.util.*

/**
 * Created by andre on 29.01.2018.
 */

class Additionals {

    companion object {
        fun DateRange(date1: Date, date2: Date): String {
            if (date1 == date2)
                return ""
            val calendar = Calendar.getInstance()

            calendar.time = date1

            val day1 = calendar.get(Calendar.DAY_OF_MONTH)
            val month1 = calendar.get(Calendar.MONTH)
            val year1 = calendar.get(Calendar.YEAR)

            calendar.time = date2

            val day2 = calendar.get(Calendar.DAY_OF_MONTH)
            val month2 = calendar.get(Calendar.MONTH)
            val year2 = calendar.get(Calendar.YEAR)

            if (month1 == month2) {
                return "${day1}-${day2}\\0${month1+1}\\${year1}"
            } else {
                if (year1 == year2) {
                    return  "${day1}\\0${month1+1}-${day2}\\0${month2+1}\\${year1}"
                } else {
                    return  "${day1}\\0${month1+1}\\${year1}-${day2}\\0${month2+1}\\${year2}"
                }
            }

        }

        fun WeeksDiff(date1: Date,date2: Date) :Int {
            val datetime1: DateTime = DateTime(date1)
            val dateTime2: DateTime = DateTime(date2)

            return Weeks.weeksBetween(datetime1,dateTime2).weeks
        }

        fun MonthDiff(date1: Date,date2: Date) :Int {
            val datetime1: DateTime = DateTime(date1)
            val dateTime2: DateTime = DateTime(date2)

            return Months.monthsBetween(datetime1,dateTime2).months
        }

        fun YearsDiff(date1: Date,date2: Date) :Int {
            val datetime1: DateTime = DateTime(date1)
            val dateTime2: DateTime = DateTime(date2)

            return Years.yearsBetween(datetime1,dateTime2).years
        }
    }
}