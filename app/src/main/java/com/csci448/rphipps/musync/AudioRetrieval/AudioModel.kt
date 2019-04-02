package com.csci448.rphipps.AudioRetrieval

//the data model for how each mp3 files are stored
data class AudioModel(val _path :String, val _name: String, val _album: String, val _artist: String, val _duration: String, val _index: Int) {
}