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

package com.andrewpetrowski.raspiinfo.Helpers

import android.content.SharedPreferences

/**
 * Created by andre on 07.01.2018.
 */

class Prefferences private constructor(){

    private lateinit var prefs: SharedPreferences

    init {

    }

    private object Holder { val INSTANCE = Prefferences()}

    companion object {
        val instance: Prefferences by lazy { Holder.INSTANCE }
    }


}