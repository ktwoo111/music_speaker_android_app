package com.csci448.rphipps.AudioRetrieval

import android.content.Context
import android.provider.MediaStore
import android.util.Log

object allAudios {
    private const val LOG_TAG = ".448AllAudios"

    val AudioList = mutableListOf<AudioModel>() //a list of all the mp3 files in the phone
    val QueueList = mutableListOf<AudioModel>() //list for the music queue
    fun getMusicList() = AudioList
    fun getMusicQueue() = QueueList

    fun getAllAudioFromDevice(context: Context) { //function to scrape all mp3 files and put it into AudioList

        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Audio.AudioColumns.DATA,
            MediaStore.Audio.AudioColumns.TITLE,
            MediaStore.Audio.AudioColumns.ALBUM,
            MediaStore.Audio.AudioColumns.ARTIST,
            MediaStore.Audio.AudioColumns.DURATION
        )
        val sortOrder = MediaStore.Audio.Media.TITLE + " ASC"
        val selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0"
        val c = context.contentResolver.query(
            uri,
            projection,
            selection,
            null,
            sortOrder
        )
        Log.d(LOG_TAG,"before checking the 'c'")


        var counter = 0
        if (c != null) {
            while (c.moveToNext()) {
                Log.d(LOG_TAG,"geting one file")

                val path = c.getString(0)   // Retrieve path.
                val name = c.getString(1)   // Retrieve name.
                val album = c.getString(2)  // Retrieve album name.
                val artist = c.getString(3) // Retrieve artist name.
                val duration = c.getString(4) //get duration

                val oneAudio = AudioModel(path, name, album, artist, duration,counter)

                Log.d(LOG_TAG,"Name :$name, Album :$album, Counter: $counter")
                Log.d(LOG_TAG,"Path :$path, Artist :$artist")
                Log.d(LOG_TAG, "duration: $duration")

                // Add the model object to the list .
                AudioList.add(oneAudio)
                counter++
            }
            c.close()
        }
    }
}