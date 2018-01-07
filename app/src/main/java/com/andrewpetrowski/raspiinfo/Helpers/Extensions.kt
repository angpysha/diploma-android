package com.andrewpetrowski.raspiinfo.Helpers

import java.util.*

/**
 * Created by andre on 31.12.2017.
 */
fun Date.zeroTime() : Date {
    val calendar: Calendar = Calendar.getInstance()

    calendar.time = this

    calendar.set(Calendar.MILLISECOND,0)
    calendar.set(Calendar.SECOND,0)
    calendar.set(Calendar.MINUTE,0)
    calendar.set(Calendar.HOUR_OF_DAY,0)
    return calendar.time
}

fun Date.Increment() : Date {
    var calendar = Calendar.getInstance()

    calendar.time = this

    calendar.add(Calendar.DATE,1)

    if (calendar.time.zeroTime() > Date().zeroTime())
        return Date()

    return calendar.time
}

fun Date.Decrement() : Date {
    var calendar = Calendar.getInstance()

    calendar.time = this

    calendar.add(Calendar.DATE,-1)

    return calendar.time
}

