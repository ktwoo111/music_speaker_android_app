package com.csci448.rphipps.musync

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment

class AddSongActivity: SingleFragmentActivity() {
    companion object {
        fun createIntent(context: Context): Intent {
            val intent = Intent(context, AddSongActivity::class.java)
            return intent
        }
        private const val LOG_TAG = "448.AddSongAct"
    }

    override fun createFragment(): Fragment {
        return AddSongFragment()
    }

    override fun getLogTag() = LOG_TAG
}