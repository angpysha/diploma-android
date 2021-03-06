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

import com.orm.SugarRecord
import java.util.*

/**
 * Created by andre on 08.04.2018.
 */

class DHT11() : SugarRecord<DHT11>() {
    var temperature: Double? = 0.0
    var humidity: Double? =0.0
    var date: String? = ""

    constructor(temperature: Double?,humidity: Double?,date: String?) : this() {
        this.temperature = temperature
        this.humidity = humidity
        this.date = date
    }
}