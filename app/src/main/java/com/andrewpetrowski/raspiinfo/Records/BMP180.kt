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

package com.andrewpetrowski.raspiinfo.Records

import com.andrewpetrowski.raspiinfo.Helpers.ORMBase
import com.orm.SugarRecord
import java.util.*

/**
 * Created by andre on 08.04.2018.
 */

class BMP180() : SugarRecord<BMP180>() {
//    override fun GetAverage(elems: MutableList<ORMBase<BMP180>>?, pos: Int): BMP180 {
//        var temperature: Float
//        var altitude: Float
//        var pressure: Float
//
//        for ()
//    }

    var pressure: Double? = 0.0
    var temperature: Double? =0.0
    var altitude: Double? = 0.0
    var date: Long? = 0

    constructor(pressure: Double?,temperature: Double?,altitude: Double?,date: Long?) : this() {
        this.altitude = altitude
        this.pressure = pressure
        this.temperature = temperature
        this.date = date
    }
}