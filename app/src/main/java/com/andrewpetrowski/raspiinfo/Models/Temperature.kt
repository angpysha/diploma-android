package com.andrewpetrowski.raspiinfo.Models

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by andre on 30.12.2017.
 */

class Temperature(temperature: Float,date:Date) : BasicModel(date) {

    var temperature: Float = temperature

}