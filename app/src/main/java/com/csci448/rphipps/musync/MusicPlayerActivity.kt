package com.csci448.rphipps.musync

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity

import kotlinx.android.synthetic.main.music_player_layout.*

class MusicPlayerActivity : SingleFragmentActivity() {
    override fun getLogTag(): String {
        return LOG_TAG
    }

    override fun createFragment(): Fragment {
        return MusicPlayerFragment()
    }
    companion object {
        private const val LOG_TAG = "csci448.MusicPlayerAct"
    }
}