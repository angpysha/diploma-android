/*
 * Copyright 2018 Andrew Petrowsky
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.andrewpetrowski.raspiinfo.Adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.app.FragmentStatePagerAdapter
import com.andrewpetrowski.raspiinfo.Models.PageDatePair
import com.andrewpetrowski.raspiinfo.TemperatureFragment

/**
 * Created by andre on 07.01.2018.
 */

class TemperatureFragmentAdapter(fragmentManager: FragmentManager, size:Int) : FragmentStatePagerAdapter(fragmentManager) {
    private var size: Int = 0
    private var type: Int = 0
   // private lateinit var pair : PageDatePair
    init {
        this.size = size
      //  pair = PageDatePair(count)

    }

    constructor(fragmentManager: FragmentManager, size:Int,type: Int) : this(fragmentManager,size) {
        this.type = type
    }


    override fun getItem(position: Int): Fragment {
       return TemperatureFragment.newInstance(position,type,size)
    }

    override fun getCount(): Int {
        return size
    }

}