package com.csci448.rphipps.musync.Servers

import android.util.Log
import com.csci448.rphipps.musync.MusicPlayer

object ServerHolder { //object to initialize and run both http and webserver
    private const val LOG_TAG = "ServerHolder"
    var clients :MutableList<Ws> = mutableListOf<Ws>() //list of client websockets
    val httpStuff : HttpServer = HttpServer()
    val websocketStuff : WebSocketServer = WebSocketServer()

    fun clientAdded(a : Ws){
        clients.add(a)
    }

    fun clientRemoved(a : Ws){
        clients.remove(a)
    }

    fun sendSyncToAllClients(startTime: Long,time: Int?){
        for (client in clients) {
            client.send("0;${startTime};${time}")
        }
    }
    fun sendPlayToAllClients(startTime: Long, delay: Long){
        for (client in clients) {
            client.send("1;${startTime};${delay}")
            Log.d(LOG_TAG,"startTimeToClient: ${startTime}")
        }

    }
    fun sendPauseToAllClients(){
        for (client in clients) {
            client.send("2")
        }
    }

    fun sendMusicToAllClients(){
        for (client in clients) {
            client.send("3;${MusicPlayer.musicIndex}")
        }
    }

    fun displayNumOfConnections() : Int {
        return clients.size
    }

    fun RunServer(){
        httpStuff.start() //starting http file server on port 8080
        websocketStuff.start() //starting websocket serve ron port 8090
    }
    fun StopServer(){
        Log.d(LOG_TAG,"closing http connection")
        httpStuff.closeAllConnections()
        httpStuff.stop()
        Log.d(LOG_TAG,"closed")

        Log.d(LOG_TAG,"closing websocket connection")
        websocketStuff.closeAllConnections()
        websocketStuff.stop()
        Log.d(LOG_TAG,"closed")
    }
}