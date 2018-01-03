package com.andrewpetrowski.raspiinfo

import com.andrewpetrowski.raspiinfo.Controllers.AndroidDHTController
import com.andrewpetrowski.raspiinfo.Helpers.zeroTime
import org.junit.Test

import org.junit.Assert.*
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testDateTemperature() {
        try {
            val temperatureContorller = AndroidDHTController()

//            val date: Date = Date.from(LocalDate.of(2017, 12, 29).atStartOfDay(ZoneId.systemDefault())
//                    .toInstant())
            var date: Date = Date()
            var calendar = Calendar.getInstance()

            calendar.time = date.zeroTime()
//            calendar.set(Calendar.MILLISECOND,0)
//            calendar.set(Calendar.SECOND,0)
//            calendar.set(Calendar.MINUTE,0)
//            calendar.set(Calendar.HOUR_OF_DAY,0)
            calendar.set(Calendar.DATE,-1)

            date = calendar.time
            var data = temperatureContorller.GetByDate(date)

            assertEquals(data!![0].temperature,25f,1e-3f)

        } catch (ex: Exception)
        {
            ex.printStackTrace()
        }
    }

}
