package com.csci448.rphipps.musync

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.widget.Toast
import com.csci448.rphipps.AudioRetrieval.AudioModel
import com.csci448.rphipps.AudioRetrieval.allAudios
import kotlinx.android.synthetic.main.queue_item_list.view.*
import kotlinx.android.synthetic.main.queue_list.*
import kotlinx.android.synthetic.main.queue_list.view.*
import org.jetbrains.anko.support.v4.toast

class AddSongFragment: Fragment() {

    private lateinit var adapter: MusicListAdapter
    private class MusicListAdapter(val fragment: AddSongFragment,
                                   val musicList: List<AudioModel>, val clickListener: (AudioModel) -> Unit)
        : RecyclerView.Adapter<MusicHolder>() {
        override fun getItemCount(): Int {
            return musicList.size
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicHolder {
            val layoutInflater = LayoutInflater.from(fragment.context)
            val view = layoutInflater.inflate(R.layout.add_song_list_item, parent, false)
            return MusicHolder(fragment, view)
        }

        override fun onBindViewHolder(holder: MusicHolder, position: Int) {
            holder.bind(musicList[position], position, clickListener)
        }
    }
    private class MusicHolder(val fragment: AddSongFragment, val view: View)
        : RecyclerView.ViewHolder(view) {
        fun bind(music: AudioModel, position: Int, clickListener: (AudioModel) -> Unit) {
            view.list_item_run_time.text = allAudios.convertTime(music)
            view.list_item_song_title.text = music._name
            view.list_item_album.text = music._album
            view.list_item_artist.text = music._artist
            view.setOnClickListener{ clickListener(music) }
        }
    }
    private fun songAdd(song: AudioModel) {
        allAudios.QueueList.add(song)
        activity?.finish()
    }
    companion object {
        private const val LOG_TAG = "448.SongAdd"
    }

    private var ip = ""
    private var userType : String = "HOST"

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    private fun updateUI() {

        adapter = MusicListAdapter(this, allAudios.getMusicList(),  { song : AudioModel -> songAdd(song) })
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
        val rootView = inflater.inflate(R.layout.add_song_fragment, container, false)
        rootView.music_queue_recycler_view.layoutManager = LinearLayoutManager( activity )
        adapter = MusicListAdapter(this, allAudios.getMusicList(), { song : AudioModel -> songAdd(song) } )
        rootView.music_queue_recycler_view.adapter = adapter
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        super.onViewCreated(view, savedInstanceState)
    }

}