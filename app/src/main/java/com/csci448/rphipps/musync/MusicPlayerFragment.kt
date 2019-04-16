package com.csci448.rphipps.musync

import android.content.Context
import android.opengl.Visibility
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import com.csci448.rphipps.AudioRetrieval.allAudios
import com.csci448.rphipps.musync.Client.ClientWebSocket
import kotlinx.android.synthetic.main.music_player_layout.*
import okhttp3.Request
import org.jetbrains.anko.support.v4.runOnUiThread

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
            InitializationForClient()
        }
        else{ // it is host
            InitializationForHost()
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
            menu?.findItem(R.id.help_btn)?.isVisible = false
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

    fun InitializationForHost(){
        //initialize title and artist tabs
        title_text_view.text = allAudios.QueueList[MusicPlayer.musicIndex]._name//allAudios.AudioList[MusicPlayer.musicIndex]._name
        artist_text_view.text = allAudios.QueueList[MusicPlayer.musicIndex]._artist//allAudios.AudioList[MusicPlayer.musicIndex]._artist

        if(MusicPlayer.initialized == false){
            //initialize player
            MusicPlayer.initializeHostMusicPlayer()
            //Tell clients about music selection
            MusicPlayer.HostSelectMusic()
            MusicPlayer.initialized = true
        }

        //button listener
        play_pause_button.setOnClickListener{
            if(MusicPlayer.getPlayingPauseStatusOfMediaPlayer() == false) {
                MusicPlayer.HostStartMusic()
                play_pause_button.text = "PAUSE"
                Toast.makeText(this.activity, "Play", Toast.LENGTH_SHORT).show()
            }
            else {

                MusicPlayer.HostPauseMusic()
                play_pause_button.text = "PLAY"
                Toast.makeText(this.activity, "Pause", Toast.LENGTH_SHORT).show()
            }
        }
        sync_button.setOnClickListener {
            MusicPlayer.HostSyncMusic()
            Toast.makeText(this.activity, "Sync", Toast.LENGTH_SHORT).show()
        }

        next_button.setOnClickListener{
            if (MusicPlayer.musicIndex < allAudios.QueueList.size - 1) {
                MusicPlayer.musicIndex++
            }
            else {
                MusicPlayer.musicIndex = 0
            }
            play_pause_button.text = "PLAY"
            //display title_text
            title_text_view.text = allAudios.QueueList[MusicPlayer.musicIndex]._name//allAudios.AudioList[MusicPlayer.musicIndex]._name
            artist_text_view.text = allAudios.QueueList[MusicPlayer.musicIndex]._artist//allAudios.AudioList[MusicPlayer.musicIndex]._artist
            MusicPlayer.ResetMusicPlayer()
            MusicPlayer.initializeHostMusicPlayer()
            MusicPlayer.HostSelectMusic()
        }

        previous_button.setOnClickListener{
            if (MusicPlayer.musicIndex > 0) {
                MusicPlayer.musicIndex--
            }
            else {
                MusicPlayer.musicIndex = allAudios.QueueList.size -1
            }
            play_pause_button.text = "PLAY"
            //display title_text
            title_text_view.text = allAudios.QueueList[MusicPlayer.musicIndex]._name
            artist_text_view.text = allAudios.QueueList[MusicPlayer.musicIndex]._artist
            MusicPlayer.ResetMusicPlayer()
            MusicPlayer.initializeHostMusicPlayer()
            MusicPlayer.HostSelectMusic()
        }
    }

    fun InitializationForClient(){
        MusicPlayer.wifi_address = ip
        //setting up websocket
        Log.d(LOG_TAG, "GETTING TO websocket")
        var web_url =  MusicPlayer.wsStuff+ MusicPlayer.wifi_address +":8090"
        var request_socket = Request.Builder().url(web_url).build()
        var listener = ClientWebSocket(this)
        var ws =  MusicPlayer.client?.newWebSocket(request_socket,listener)
        //TODO: get safe call for when it fails
        sync_button.visibility = View.INVISIBLE
        button_panel.visibility = View.INVISIBLE

    }

    fun setText(text: TextView, value: String) {
        runOnUiThread { text.text = value }
    }
}