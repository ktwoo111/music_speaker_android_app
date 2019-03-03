package com.csci448.rphipps.musync

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import com.csci448.rphipps.musync.ClientActivity.Companion.ipAddress
import kotlinx.android.synthetic.main.queue_item_list.view.*
import kotlinx.android.synthetic.main.queue_list.*
import kotlinx.android.synthetic.main.queue_list.view.*
import java.net.InetAddress

class QueueListFragment: Fragment() {

    private lateinit var adapter: MusicListAdapter
    private class MusicListAdapter(val fragment: QueueListFragment,
                                   val musicList: List<Music>)
        : RecyclerView.Adapter<MusicHolder>() {
        override fun getItemCount(): Int {
            return musicList.size
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicHolder {
            val layoutInflater = LayoutInflater.from(fragment.context)
            val view = layoutInflater.inflate(R.layout.queue_item_list, parent, false)
            return MusicHolder(fragment, view)
        }

        override fun onBindViewHolder(holder: MusicHolder, position: Int) {
            holder.bind(musicList[position], position)
        }
    }
    private class MusicHolder(val fragment: QueueListFragment, val view: View)
        : RecyclerView.ViewHolder(view) {
        fun bind(music: Music, position: Int) {
            view.list_item_run_time.text = music.runTime.toString()
            view.list_item_song_title.text = music.songTitle
            view.list_item_album.text = music.artistName
            view.list_item_artist.text = music.artistName
        }
    }
    companion object {
        private const val LOG_TAG = "448.QueueListFrag"
        private const val REQUEST_CODE_DETAILS_FRAGMENT = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }
    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_queue_music_player, menu)
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean =
        when(item?.itemId) {
            R.id.go_to_player_item -> {
                //getting wifi address
                val wifiMan = this.context?.getSystemService(Context.WIFI_SERVICE) as WifiManager
                val wifiInf = wifiMan.connectionInfo
                val ipAddress = wifiInf.ipAddress
                val ip = String.format(
                    "%d.%d.%d.%d", ipAddress and 0xff, ipAddress shr 8 and 0xff, ipAddress shr 16 and 0xff,
                    ipAddress shr 24 and 0xff
                )
                val intent = MusicPlayerActivity.createIntent(this.context, ip)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    private fun updateUI() {

        adapter = MusicListAdapter(this, MusicLab.getMusicList() )
        music_queue_recycler_view.adapter = adapter
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int,
                                  data: Intent?) {
        //Possibly need later
    }
    override fun onResume() {
        super.onResume()
        updateUI()
        Log.d(LOG_TAG, "onResume called")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.queue_list, container, false)
        rootView.music_queue_recycler_view.layoutManager = LinearLayoutManager( activity )
        adapter = MusicListAdapter(this, MusicLab.getMusicList() )
        rootView.music_queue_recycler_view.adapter = adapter
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        add_song_button.setOnClickListener{
            val intent = Intent(this.context, AddSongActivity::class.java)
            startActivity(intent)
        }
        super.onViewCreated(view, savedInstanceState)
    }

}