package com.andrewpetrowski.raspiinfo.Models

import io.github.angpysha.diploma_bridge.Models.Bmp180_Data
import io.github.angpysha.diploma_bridge.Models.DHT11_Data

data class AllData(val bmp: List<Bmp180_Data>?,val dht: List<DHT11_Data>?)