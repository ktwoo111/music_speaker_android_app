package com.csci448.rphipps.musync

import android.content.Context
import android.opengl.Visibility
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.*
import kotlinx.android.synthetic.main.music_player_layout.*

class MusicPlayerFragment: Fragment() {
    interface Callbacks {
        fun onSwitchFragments(newFrag : Fragment)
    }

    private var callbacks : Callbacks? = null

    companion object {
        private const val LOG_TAG = "448.MusicPlayerAct"
        private val IP_BUNDLE = "IP_BUNDLE"
        private val TYPE_BUNDLE = "TYPE_BUNDLE"
        private var ip = ""

        fun createFragment(ip : String, type : String) : Fragment {
            var arguments = Bundle()
            arguments.putString(IP_BUNDLE, ip)
            arguments.putString(TYPE_BUNDLE, type)
            var fragment = MusicPlayerFragment()
            fragment.arguments = arguments
            return fragment
        }
    }
    private var userType: String = "HOST"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        ip = arguments?.getString(IP_BUNDLE) ?: "000.00.00.00"
        userType = arguments?.getString(TYPE_BUNDLE) ?: "HOST"

        ip_text_view.text = ip

        play_pause_button.setOnClickListener {
            if(play_pause_button.text == getString(R.string.play)) {
                play_pause_button.text = getString(R.string.pause)
            } else {
                play_pause_button.text = getString(R.string.play)
            }
        }

        if(userType == "CLIENT") {
            button_panel.visibility = View.INVISIBLE
        }

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
                val newFrag = QueueListFragment.createFragment(ip, userType)
                callbacks?.onSwitchFragments(newFrag)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        Log.d(LOG_TAG, "onAttach() called")
        callbacks = context as Callbacks
    }

    override fun onDetach() {
        Log.d(LOG_TAG, "onDetach() called")
        callbacks = null
        super.onDetach()
    }
}