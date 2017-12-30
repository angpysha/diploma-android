package com.andrewpetrowski.raspiinfo

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.andrewpetrowski.diploma.bridgelib.Controllers.DhtController
import com.andrewpetrowski.diploma.bridgelib.Models.DHT11_Data
import org.junit.Assert

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("com.andrewpetrowski.raspiinfo", appContext.packageName)
    }

    @Test
    fun getLast() {
        try {
            var dhtController: DhtController = DhtController()
            var dtData:DHT11_Data = dhtController.GetLast(DHT11_Data::class.java)
            assertEquals(dtData.temperature,25f,1e-5f)
        } catch (ex: Exception) {
            System.out.println(ex.message)
            assertFalse(true)
        }
    }
}
