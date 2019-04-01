package com.csci448.rphipps.musync.Servers

import com.csci448.rphipps.musync.ModifiedLibrary.NanoWSD


class WebSocketServer (val port_num : Int = 8090) : NanoWSD(port_num) {

    override fun openWebSocket(handshake: IHTTPSession?): WebSocket {
        return Ws(handshake)
    }

}