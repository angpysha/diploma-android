package com.andrewpetrowski.raspiinfo.Models

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by andre on 30.12.2017.
 */

abstract class BasicModel(date: Date) {

    var date: Date = date

    fun toFormattedDate(): String {
        val dateFormat = SimpleDateFormat("HH:mm")
        return dateFormat.format(date)
    }
}