package com.csci448.rphipps.musync

import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Handler
import android.util.Log
import com.csci448.rphipps.AudioRetrieval.allAudios
import com.csci448.rphipps.musync.Servers.ServerHolder
import com.csci448.rphipps.musync.Servers.Ws
import com.instacart.library.truetime.TrueTime
import okhttp3.OkHttpClient
import org.jetbrains.anko.doAsync

object MusicPlayer {
    private const val LOG_TAG = "448.MUSICPLAYER"
    var musicIndex = 0 //for client, this is index referencing AudioList. For host, this is index referencing QueueList
    var musicPlayer: MediaPlayer? = MediaPlayer()
    var initialized :Boolean? = false
    var clientWebSocket: Ws? = null
    var handler: Handler = Handler() //used for Host
    var delay: Long = 1500 //delay of amount
    var mRunnable = Runnable {
        Log.d(LOG_TAG,"TriggeredTime for audio start: ${System.currentTimeMillis()}")
        musicPlayer?.start()
    }

    //variables mainly for Client
    val musicSuffix = ":8080/music/"
    val titleSuffix = ":8080/title/"
    val artistSuffix = ":8080/artist/"
    val positionSuffix = ":8080/position"
    var httpStuff = "http://"
    var wsStuff = "ws://"
    var wifi_address = ""
    val client: OkHttpClient? = OkHttpClient()



    fun getPosition(): Int?{
        return musicPlayer?.currentPosition

    }

    fun getPlayingPauseStatusOfMediaPlayer(): Boolean?{
        return musicPlayer?.isPlaying
    }


    fun HostStartMusic(){
        var startTime = TrueTime.now().time
        Log.d(LOG_TAG,"startTime for handler to initiate: ${startTime}")
        handler.postDelayed(
            mRunnable, // Runnable
            delay
        )
        doAsync {
            ServerHolder.sendPlayToAllClients(startTime, delay)
        }
    }

    fun HostPauseMusic(){
        musicPlayer?.pause()
        doAsync {
            ServerHolder.sendPauseToAllClients()
        }

    }

    fun HostSyncMusic(){
        var startTime = TrueTime.now().time
        doAsync {
            ServerHolder.sendSyncToAllClients( startTime, musicPlayer?.currentPosition)
        }


    }

    fun HostSelectMusic(){
        doAsync {
            ServerHolder.sendMusicToAllClients()
        }
    }

    fun ClientSyncMusic(time: Int){
        musicPlayer?.seekTo(time)

    }

    fun ClientStartMusic(){
        musicPlayer?.start()
    }

    fun ClientPauseMusic(){
        musicPlayer?.pause()

    }

    fun initializeHostMusicPlayer(){
        Log.d("musicind", musicIndex.toString())
        musicPlayer?.setDataSource(allAudios.QueueList[musicIndex]._path)
        musicPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)
        musicPlayer?.prepareAsync()
    }

    fun initializeClientMusicPlayer(){
        Log.d(LOG_TAG,httpStuff+wifi_address+musicSuffix+ musicIndex)
        musicPlayer?.setDataSource(httpStuff+wifi_address+musicSuffix+musicIndex)
        musicPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)
        musicPlayer?.prepareAsync()
    }


    fun ResetMusicPlayer(){
        musicPlayer?.reset()
    }
}