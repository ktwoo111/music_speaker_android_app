package com.csci448.rphipps.musync

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.widget.FrameLayout

class QueueListActivity: SingleFragmentActivity() {
    companion object {
        fun createIntent(context: Context, ip: String, type:String) : Intent {
            val intent = Intent(context, MusicPlayerActivity::class.java)
            intent.putExtra(IP_EXTRA, ip)
            intent.putExtra(TYPE_EXTRA, type)
            return intent
        }

        private const val LOG_TAG = "448.QueueListAct"
        private val IP_EXTRA = "IP_EXTRA"
        private val TYPE_EXTRA = "TYPE_EXTRA"
    }

    override fun createFragment(): Fragment {
        val ip = intent?.getStringExtra(IP_EXTRA) ?: "000.00.00.00"
        val type = intent?.getStringExtra(TYPE_EXTRA) ?: "HOST"
        return QueueListFragment.createFragment(ip, type)
    }

    override fun getLogTag() = LOG_TAG
}