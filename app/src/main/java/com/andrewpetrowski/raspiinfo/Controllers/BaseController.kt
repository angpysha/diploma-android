package com.andrewpetrowski.raspiinfo.Controllers

import com.andrewpetrowski.diploma.bridgelib.Controllers.DhtController
import com.andrewpetrowski.diploma.bridgelib.Models.DhtSearch
import com.andrewpetrowski.diploma.bridgelib.Models.Entity
import com.andrewpetrowski.raspiinfo.Models.BasicModel
import java.util.*

/**
 * Created by andre on 30.12.2017.
 */

abstract class BaseController<T : BasicModel>() {

//    fun GetByDate(date: Date,className: Class<out Entity>): List<T> {
//        var calendar = Calendar.getInstance()
////        calendar.time = date
////        calendar.add(Calendar.DATE,-1)
////        var dayBefore = calendar.time
//
//        calendar.time = date
//        calendar.add(Calendar.DATE,1)
//        var dayAfter = calendar.time
//
//        var filter = DhtSearch(date,dayAfter,null,null, null,null)
//
//        var data = DhtController().SearchAsync(filter,className).get()
//
//        return data
//    }

}