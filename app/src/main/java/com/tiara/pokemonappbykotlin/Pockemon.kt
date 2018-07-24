package com.tiara.pokemonappbykotlin

import android.location.Location

class Pockemon(image: Int, name: String, des: String, power: Double, lat: Double, long: Double) {
    var name : String? = name
    var des : String? = des
    var image : Int? = image
    var power : Double? = power
    var location : Location? = null
    var isCatch : Boolean? = false

    init {
        this.location = Location(name)
        this.location!!.latitude = lat
        this.location!!.longitude = long
        this.isCatch = false
    }

}