package com.csci448.rphipps.musync

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment

class PlayerQueueActivity : SingleFragmentActivity(), QueueListFragment.Callbacks, MusicPlayerFragment.Callbacks {
    override fun onSwitchFragments(newFrag: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, newFrag).commit()
    }

    companion object {
        private const val LOG_TAG = "448.HostAct"
        private val IP_EXTRA = "IP_EXTRA"
        private val TYPE_EXTRA = "TYPE_EXTRA"

        fun createIntent(context: Context, ip: String, type: String): Intent {
            val intent = Intent(context, PlayerQueueActivity::class.java)
            intent.putExtra(IP_EXTRA, ip)
            intent.putExtra(TYPE_EXTRA, type)
            return intent
        }
    }

    override fun getLogTag(): String {
        return LOG_TAG
    }

    override fun createFragment(): Fragment {
        val ip = intent?.getStringExtra(IP_EXTRA) ?: "000.00.00.00"
        val type = intent?.getStringExtra(TYPE_EXTRA) ?: "HOST"
        if(type == "HOST") {
            return QueueListFragment.createFragment(ip, type)
        } else {
            return MusicPlayerFragment.createFragment(ip, type)
        }
    }
}