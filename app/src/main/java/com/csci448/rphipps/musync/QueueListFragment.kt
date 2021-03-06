package com.csci448.rphipps.musync

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v13.view.DragStartHelper
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.*
import android.widget.PopupWindow
import android.widget.Toast
import com.csci448.rphipps.AudioRetrieval.AudioModel
import com.csci448.rphipps.AudioRetrieval.allAudios
import kotlinx.android.synthetic.main.queue_item_list.view.*
import kotlinx.android.synthetic.main.queue_list.*
import kotlinx.android.synthetic.main.queue_list.view.*
import org.jetbrains.anko.support.v4.act
import java.util.*
import kotlin.math.abs

class QueueListFragment: Fragment() {

    interface Callbacks {
        fun onSwitchFragments(newFrag : Fragment)
    }

    private var callbacks : Callbacks? = null
    private lateinit var adapter: MusicListAdapter
    private class MusicListAdapter(val fragment: QueueListFragment,
                                   val musicList: List<AudioModel>)
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
        fun bind(music: AudioModel, position: Int) {
            if(position == MusicPlayer.musicIndex){
                view.setBackgroundColor(Color.rgb(13, 213, 252))
            }
            view.list_item_run_time.text = allAudios.convertTime(music)
            view.list_item_song_title.text = music._name
            view.list_item_album.text = music._album
            view.list_item_artist.text = music._artist
        }
    }
    companion object {
        private const val LOG_TAG = "448.QueueListFrag"
        private const val IP_BUNDLE = "IP_BUNDLE"
        private const val TYPE_BUNDLE = "TYPE_BUNDLE"

        fun createFragment(ip : String, type : String) : Fragment {
            var arguments = Bundle()
            arguments.putString(IP_BUNDLE, ip)
            arguments.putString(TYPE_BUNDLE, type)
            var fragment = QueueListFragment()
            fragment.arguments = arguments
            return fragment
        }
    }

    private var ip = ""
    private var userType : String = "HOST"

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }
    private lateinit var menuThing: Menu
    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        menuThing = menu as Menu
        inflater?.inflate(R.menu.menu_queue_music_player, menu)
        val menuItemPlayer = menu?.findItem(R.id.go_to_player_item)
        if(allAudios.QueueList.size != 0) {
            menuItemPlayer?.isVisible = true
        }
        else {
            menuItemPlayer?.isVisible = false
        }
        val menuItemQueue = menu?.findItem(R.id.go_to_queue_item)
        menuItemQueue?.isVisible = false

    }
    fun showPopUp(view: View) {

    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean =
        when(item?.itemId) {
            R.id.help_btn -> {
                val builder = AlertDialog.Builder(this.context)

                // Set the alert dialog title
                builder.setTitle("Help")

                // Display a message on alert dialog
                builder.setMessage("Add music that you would like to play with the ADD button." +
                        " Long press a song to drag it around the queue. Swiping left on a song will remove it from the queue. Use the arrow in the top right corner" +
                        " to get to the music player.")


                // Display a neutral button on alert dialog
                builder.setNeutralButton("Close"){_,_ ->
                }

                // Finally, make the alert dialog using builder
                val dialog: AlertDialog = builder.create()

                // Display the alert dialog on app interface
                dialog.show()
                true
            }
            R.id.go_to_player_item -> {
                val newFrag = MusicPlayerFragment.createFragment(ip, userType)
                callbacks?.onSwitchFragments(newFrag)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    private fun updateUI() {

        adapter = MusicListAdapter(this, allAudios.getMusicQueue() )
        music_queue_recycler_view.adapter = adapter

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int,
                                  data: Intent?) {
        //Possibly need later
    }
    override fun onResume() {
        super.onResume()
        activity?.invalidateOptionsMenu()
        updateUI()
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        val menuItemPlayer = menu?.findItem(R.id.go_to_player_item)
        if(allAudios.QueueList.size != 0) {
            Log.d(LOG_TAG, allAudios.QueueList.size.toString())
            menuItemPlayer?.isVisible = true
            menu?.findItem(R.id.help_btn)?.isVisible = true
        }
        else {
            menuItemPlayer?.isVisible = false
        }
        super.onPrepareOptionsMenu(menu)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.queue_list, container, false)
        rootView.music_queue_recycler_view.layoutManager = LinearLayoutManager( activity )
        adapter = MusicListAdapter(this, allAudios.getMusicQueue() )
        rootView.music_queue_recycler_view.adapter = adapter
        val touchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.LEFT){
            override fun onMove(p0: RecyclerView, p1: RecyclerView.ViewHolder, p2: RecyclerView.ViewHolder): Boolean {
                Log.d("INFO", "${MusicPlayer.musicIndex.toString()}, ${p1.adapterPosition.toString()},${p2.adapterPosition.toString()}")
                if(MusicPlayer.musicIndex == p1.adapterPosition){
                    MusicPlayer.musicIndex = p2.adapterPosition
                }
                else if( abs(MusicPlayer.musicIndex - p1.adapterPosition) == 1){
                    if(p1.adapterPosition > MusicPlayer.musicIndex){
                        Log.d("DECREM", MusicPlayer.musicIndex.toString())
                        MusicPlayer.musicIndex++
                    }
                    else {
                        MusicPlayer.musicIndex--
                        Log.d("INCREM", MusicPlayer.musicIndex.toString())
                    }
                }
                Collections.swap(adapter.musicList, p1.adapterPosition, p2.adapterPosition)

                adapter.notifyItemMoved(p1.adapterPosition,p2.adapterPosition)
                return true
            }

            override fun onSwiped(p0: RecyclerView.ViewHolder, p1: Int) {
                if(p1 == ItemTouchHelper.LEFT) {
                    if(p0.adapterPosition < MusicPlayer.musicIndex){
                        MusicPlayer.musicIndex--
                    }
                    else if(p0.adapterPosition == MusicPlayer.musicIndex){
                        if(MusicPlayer.musicIndex > 0){
                            MusicPlayer.musicIndex--
                        }
                        MusicPlayer.HostPauseMusic()
                        MusicPlayer.ResetMusicPlayer()
                        MusicPlayer.initialized = false

                    }
                    allAudios.QueueList.removeAt(p0.adapterPosition)
                    adapter.notifyDataSetChanged()
                    if(allAudios.QueueList.size == 0){
                        val menuItemPlayer = menuThing?.findItem(R.id.go_to_player_item)
                        menuItemPlayer?.isVisible = false
                        MusicPlayer.HostPauseMusic()
                        MusicPlayer.ResetMusicPlayer()
                        MusicPlayer.initialized = false
                        rootView.invalidate()
                    }
                }
            }

        })

        touchHelper.attachToRecyclerView(rootView.music_queue_recycler_view)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        add_song_button.setOnClickListener{
            val intent = Intent(this.context, AddSongActivity::class.java)
            startActivity(intent)
        }

        ip = arguments?.getString(IP_BUNDLE) ?: "000.00.00.00"
        userType = arguments?.getString(TYPE_BUNDLE) ?: "HOST"

        if(userType == "HOST") {
            add_song_button.isEnabled = true
        } else if(userType == "CLIENT") {
            add_song_button.isEnabled = false
        }

        ip_queue_text.text = ip

        super.onViewCreated(view, savedInstanceState)
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