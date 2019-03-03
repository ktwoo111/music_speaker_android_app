package com.csci448.rphipps.musync

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import kotlinx.android.synthetic.main.music_player_layout.*

class MusicPlayerFragment: Fragment() {
    companion object {
        private const val LOG_TAG = "csci448.MusicPlayerAct"
        private val IP_EXTRA = "IP_EXTRA"
        private val TYPE_EXTRA = "type_extra"
        private var ip = ""
        fun createIntent(context: Context, ip: String, type:String) : Intent {
            val intent = Intent(context, MusicPlayerActivity::class.java)
            intent.putExtra(IP_EXTRA, ip)
            intent.putExtra(TYPE_EXTRA, type)
            return intent
        }
    }
    private var userType: String? = "HOST"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        ip = activity?.intent?.getStringExtra(IP_EXTRA) as String
        ip_text_view.text = ip
        userType = activity?.intent?.getStringExtra(TYPE_EXTRA)
        super.onViewCreated(view, savedInstanceState)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.music_player_layout, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }
    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_queue_music_player, menu)
        if(userType == "HOST") {
            val menuItemPlayer = menu?.findItem(R.id.go_to_player_item)
            menuItemPlayer?.isVisible = false
            val menuItemQueue = menu?.findItem(R.id.go_to_queue_item)
            menuItemQueue?.isVisible = true
        }
        else{
            val menuItemPlayer = menu?.findItem(R.id.go_to_player_item)
            menuItemPlayer?.isVisible = false
            val menuItemQueue = menu?.findItem(R.id.go_to_queue_item)
            menuItemQueue?.isVisible = false
        }
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean =
        when(item?.itemId) {
            R.id.go_to_queue_item -> {
                val intent = Intent(this.context, QueueListActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
}