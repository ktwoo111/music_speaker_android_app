package com.csci448.rphipps.musync

import java.util.*

class Music {
    var id : UUID
        private set

    var songTitle : String = ""
    var artistName : String = ""
    var albumName : String = ""
    var runTime: Int = 0

    init {
        id = UUID.randomUUID()
    }
}