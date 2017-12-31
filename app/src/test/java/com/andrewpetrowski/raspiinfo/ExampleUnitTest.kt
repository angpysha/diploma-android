package com.andrewpetrowski.raspiinfo

import com.andrewpetrowski.raspiinfo.Controllers.TemperatureController
import org.junit.Test

import org.junit.Assert.*
import java.time.LocalDate
import java.time.ZoneId
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
            val temperatureContorller = TemperatureController()

            val date: Date = Date.from(LocalDate.of(2017, 12, 29).atStartOfDay(ZoneId.systemDefault())
                    .toInstant())
            var data = temperatureContorller.GetByDate(date)

            assertEquals(data!![0].temperature,25f,1e-3f)

        } catch (ex: Exception)
        {
            ex.printStackTrace()
        }
    }
}
