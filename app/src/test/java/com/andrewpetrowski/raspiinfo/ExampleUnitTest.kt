package com.andrewpetrowski.raspiinfo

import com.andrewpetrowski.raspiinfo.Controllers.AndroidDHTController
import com.andrewpetrowski.raspiinfo.Helpers.zeroTime
import io.github.angpysha.diploma_bridge.Controllers.DhtController
import io.github.angpysha.diploma_bridge.Models.DHT11_Data
import io.github.angpysha.diploma_bridge.Models.DhtSearch
import io.github.angpysha.diploma_bridge.Models.DisplayPeriod
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

    @Test
    fun TestDatesCount() {
        val contr = DhtController()

        val c = contr.GetDatesCount()

        assertEquals(c,20)
    }

    @Test
    fun TestGetWeeks() {
        var calendar = Calendar.getInstance()

        var controller = AndroidDHTController()

        var data = controller.GetByDate(calendar.time,1)

        var i =0
    }

    @Test
    fun TestWeeksLib() {
        var date = Date().zeroTime()

        var controller = DhtController()

        var data = controller.GetByPeriod(date,DisplayPeriod.WEEK,DHT11_Data::class.java,DhtSearch::class.java)

        var i =0
    }

}
