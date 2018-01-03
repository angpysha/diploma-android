package com.andrewpetrowski.raspiinfo

import android.support.test.runner.AndroidJUnit4
import com.andrewpetrowski.diploma.bridgelib.Controllers.DhtController
import com.andrewpetrowski.diploma.bridgelib.Models.DHT11_Data
import com.andrewpetrowski.diploma.bridgelib.Models.DhtSearch
import com.andrewpetrowski.raspiinfo.Helpers.zeroTime
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

/**
 * Created by andre on 02.01.2018.
 */
@RunWith(AndroidJUnit4::class)
 class TemperatureListTest {

    @Test
    fun TestGetList() {
        try {
            var date : Date = Date()

            date = date.zeroTime()
            val controller = DhtController()

            var calendar = Calendar.getInstance()

            calendar.time = date

            calendar.set(Calendar.DATE,1)

            val filter = DhtSearch(date,calendar.time,
                    null,null,
                    null,null)


            val data = controller.Search(filter,DHT11_Data::class.java)

            val i = 0
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}