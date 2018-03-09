package com.andrewpetrowski.raspiinfo.Helpers

import com.andrewpetrowski.raspiinfo.Controllers.AndroidDHTController
import org.joda.time.DateTime
import org.joda.time.LocalDateTime
import org.joda.time.Weeks
import org.joda.time.format.DateTimeFormat
import java.text.SimpleDateFormat
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

fun Date.ToLocal() : String {
    val time = LocalDateTime(this)
    val dtf = DateTimeFormat.forPattern("dd MMMM yyyy HH:mm:ss")

    val ttime = time.toString(dtf)

    return ttime
}



fun Date.fullTime() : Date {
    val calendar: Calendar = Calendar.getInstance()
    calendar.time = this

    calendar.set(Calendar.SECOND,59)
    calendar.set(Calendar.MINUTE,59)
    calendar.set(Calendar.HOUR_OF_DAY,23)

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

fun Date.toFormatedString() :String{
    val df  = SimpleDateFormat("MM-dd-yyyy HH:mm:ss")
    return df.format(this)
}

fun String.toDate() : Date {
    val df  = SimpleDateFormat("MM-dd-yyyy HH:mm:ss")
    return df.parse(this)
}

fun checkAvailable(date: Date) : Boolean {
    val dhtcon = AndroidDHTController()

    val data = dhtcon.GetByDate(date.zeroTime())

    return data?.isNotEmpty()?:false
}

fun <T> List<out T>.getLast(): T {
    return this[this.size-1]
}

