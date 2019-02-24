package com.csci448.rphipps.musync

import java.util.*

object MusicLab {
    private val musicList: MutableList<Music> = mutableListOf()
    fun getMusicList() = musicList
    init {
        for(i in 0..100) {
            val music = Music()
            music.songTitle = "Song #$i"
            music.artistName = "Artist #$i"
            music.albumName = "Album #$i"
            music.runTime = (260)
            musicList.add(music)
        }
    }

    fun getCrime(id: UUID): Music? {
        for(music in musicList) {
            if(music.id == id) {
                return music
            }
        }
        return null
    }
}