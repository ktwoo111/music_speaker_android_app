package com.csci448.rphipps.musync.Servers

import android.util.Log
import com.csci448.rphipps.AudioRetrieval.allAudios
import com.csci448.rphipps.musync.ModifiedLibrary.NanoHTTPD
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream



class HttpServer (val port_num : Int = 8080) : NanoHTTPD(port_num) {

    override fun serve(session: IHTTPSession?): Response { //what the http server returns when these url requests are made from server
        if (session?.uri == "/position") {
        return newFixedLengthResponse("TODO")
        }
        else if (session?.uri == "/"){
            return newFixedLengthResponse("WHAT UP")
        }
        else if (session?.uri?.contains("/music") == true){
            return getSound(session?.uri)
        }
        else if (session?.uri?.contains("/title") == true){
            return getTitle(session?.uri)
        }
        else if (session?.uri?.contains("/artist") == true){
            return getArtist(session?.uri)
        }
        else {
            return newFixedLengthResponse("DEFAULT RESPONSE")
        }
    }


    //cutomized functions TAEWOO KIM
    private fun getSound(input: String?) : Response{
        var t = input?.split("/")
        Log.d("HTTPSERVER", t.toString())

        var myInput: InputStream? = null
        try {
            var file = File(allAudios.AudioList[t?.get(2)?.toInt() as Int]._path)
            myInput = FileInputStream(file)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return createResponse(NanoHTTPD.Response.Status.OK, "audio/mpeg", myInput)
    }

    private fun getTitle(input: String?) : Response{
        var t = input?.split("/")
        Log.d("HTTPSERVER", t.toString())


        return newFixedLengthResponse(allAudios.AudioList[t?.get(2)?.toInt() as Int]._name)
    }

    private fun getArtist(input: String?) : Response{
        var t = input?.split("/")
        Log.d("HTTPSERVER", t.toString())


        return newFixedLengthResponse(allAudios.AudioList[t?.get(2)?.toInt() as Int]._artist)
    }

    //Announce that the file server accepts partial content requests
    private fun createResponse(
        status: Response.Status, mimeType: String,
        message: InputStream?
    ): NanoHTTPD.Response {
        return NanoHTTPD.newChunkedResponse(status, mimeType, message)
    }

}