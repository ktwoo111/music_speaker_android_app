package com.csci448.rphipps.musync.Client

import android.content.Context
import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.csci448.rphipps.AudioRetrieval.allAudios
import com.csci448.rphipps.musync.ClientActivity
import com.csci448.rphipps.musync.MainActivity
import com.csci448.rphipps.musync.MusicPlayer
import com.csci448.rphipps.musync.MusicPlayerFragment

import com.instacart.library.truetime.TrueTime
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.music_player_layout.*
import okhttp3.*
import okio.ByteString
import java.io.IOException
import kotlin.math.abs


class ClientWebSocket(var fragment: MusicPlayerFragment) : WebSocketListener() { //the websocket for the client

    companion object{
        private const val LOG_TAG= "ClientWebSocket"
        private const val NORMAL_CLOSURE_STATUS = 99
        private const val SYNC = "0"
        private const val PLAY = "1"
        private const val PAUSE = "2"
        private const val MUSIC_INDEX = "3"

    }

    var messageFromServer: List<String>? = null
    var currentCode : String?  = ""
    var handler: Handler = Handler()
    var mRunnable = Runnable {
        MusicPlayer.ClientStartMusic()
        Log.d(LOG_TAG,"TriggeredTime: ${TrueTime.now().time}")
    }

    fun retrieveMessage(t: String?){
        messageFromServer = t?.split(";")
    }
    fun decipherMessage(){
        currentCode = messageFromServer?.get(0) //the first portion of the message
      if (currentCode == SYNC){
          setSync(messageFromServer?.get(1)?.toLong() as Long, messageFromServer?.get(2)?.toInt() as Int)

      }
      else if (currentCode == PLAY){
            setDelayedPlay(messageFromServer?.get(1)?.toLong() as Long, messageFromServer?.get(2)?.toLong() as Long)

        }
        else if (currentCode == PAUSE){
            setPause()

        }
        else if (currentCode == MUSIC_INDEX){
          setMusicSelection(messageFromServer?.get(1)?.toInt() as Int)

      }

    }

    fun setMusicSelection(index: Int){
        MusicPlayer.musicIndex = index
        MusicPlayer.ResetMusicPlayer()
        MusicPlayer.initializeClientMusicPlayer()


        //http request via Okhttp to get title
        Log.d(LOG_TAG,"getting Title from Host")
        val url = MusicPlayer.httpStuff + MusicPlayer.wifi_address + MusicPlayer.titleSuffix + index.toString()
        val request_title = Request.Builder().url(url).build()
        var startTime = System.currentTimeMillis()
        Log.d(LOG_TAG,"http start: $startTime")
        MusicPlayer.client?.newCall(request_title)?.enqueue(object: Callback {
            override fun onResponse(call: Call?, response: Response?){
                //this is being run on a different thread,
                // so you have to trigger UIthread to make changes to UI with updated info
                val body = response?.body()?.string()
                fragment?.setText(fragment.title_text_view,body as String)
            }
            override fun onFailure(call: Call, e: IOException) {
                Log.d(LOG_TAG, "not good stuff for http for title")
            }
        })
        //end of section for getting title

        //http request via Okhttp to get artist
        Log.d(LOG_TAG,"getting Title from Host")
        val url_artist = MusicPlayer.httpStuff + MusicPlayer.wifi_address + MusicPlayer.artistSuffix + index.toString()
        val request_artist = Request.Builder().url(url_artist).build()
        startTime = System.currentTimeMillis()
        Log.d(LOG_TAG,"http start: $startTime")
        MusicPlayer.client?.newCall(request_artist)?.enqueue(object: Callback {
            override fun onResponse(call: Call?, response: Response?){
                //this is being run on a different thread,
                // so you have to trigger UIthread to make changes to UI with updated info
                val body = response?.body()?.string()
                fragment?.setText(fragment.artist_text_view,body as String)
            }
            override fun onFailure(call: Call, e: IOException) {
                Log.d(LOG_TAG, "not good stuff for http for artist")
            }
        })
        //end of section for getting artist
    }

    fun setPause(){
        Log.d(LOG_TAG, "pausing music")
        MusicPlayer.ClientPauseMusic()
        Log.d(LOG_TAG, "paused")
    }
    
    fun setDelayedPlay(systemTimeFromServer: Long, delayTime: Long){
        var clientTime = TrueTime.now().time
        var diff = clientTime - systemTimeFromServer
        var newDelay = delayTime - abs(diff)
        Log.d(LOG_TAG, "client: $clientTime, server: $systemTimeFromServer, diff: $diff")
        handler.postDelayed(mRunnable,newDelay)
        Log.d(LOG_TAG,"delayed play set")
    }

    fun setSync(systemTimeFromServer: Long, timePosition: Int){
        //Syncing only works with mp3 files that contains LAME/XING Header. I don't know why, but that's how Android's MediaPlayer works for streaming
        MusicPlayer.ClientSyncMusic(timePosition)
        Log.d(LOG_TAG,"musicPosition: ${ MusicPlayer.getPosition()}")
    }
    override fun onOpen(webSocket: WebSocket, response: Response) {
        //if socket connnection to host is successful, "connected to master" log message should appear
        Log.d(LOG_TAG, "connected to master")

    }

    override fun onMessage(webSocket: WebSocket?, text: String?){
        Log.d(LOG_TAG, "message: $text")
        Log.d(LOG_TAG, "currentTime: ${System.currentTimeMillis()}")
        retrieveMessage(text) //splitting the message to each element and put it into a list
        decipherMessage() //deciphering message according to whatever the first number is

    }

    override fun onMessage(webSocket: WebSocket?, bytes: ByteString) {
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String?) {
        webSocket.close(NORMAL_CLOSURE_STATUS, null)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        Log.d(LOG_TAG, "Connection Failed")
        Log.d(LOG_TAG, "the response: ${response?.body()?.toString()}" )
        Log.d(LOG_TAG, "the response: ${t.message}" )
    }

}