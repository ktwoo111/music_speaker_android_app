package com.csci448.rphipps.musync

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import kotlinx.android.synthetic.main.music_player_layout.*

class MusicPlayerActivity : AppCompatActivity() {
    companion object {
        private val IP_EXTRA = "IP_EXTRA"
        private var ip = ""
        fun createIntent(context: Context, ip: String) : Intent {
            val intent = Intent(context, MusicPlayerActivity::class.java)
            intent.putExtra(IP_EXTRA, ip)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ip = intent.getStringExtra(IP_EXTRA)
        setContentView(R.layout.music_player_layout)
        ip_text_view.text = ip
    }

}