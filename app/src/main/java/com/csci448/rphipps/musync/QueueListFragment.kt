package com.csci448.rphipps.musync

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.queue_item_list.view.*
import kotlinx.android.synthetic.main.queue_list.*

class QueueListFragment: Fragment() {

    private lateinit var adapter: CrimeListAdapter
    private class CrimeListAdapter(val fragment: QueueListFragment,
                                   val crimes: List<Music>)
        : RecyclerView.Adapter<CrimeHolder>() {
        override fun getItemCount(): Int {
            return crimes.size
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeHolder {
            val layoutInflater = LayoutInflater.from(fragment.context)
            val view = layoutInflater.inflate(R.layout.queue_item_list, parent, false)
            return CrimeHolder(fragment, view)
        }

        override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
            holder.bind(crimes[position], position)
        }
    }
    private class CrimeHolder(val fragment: QueueListFragment, val view: View)
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

    /*private fun updateUI() {

        adapter = CrimeListAdapter(this, MusicLab.getMusicList() )
        music_queue_recycler_view.adapter = adapter
    }*/
    override fun onActivityResult(requestCode: Int, resultCode: Int,
                                  data: Intent?) {
        //Possibly need later
    }
}