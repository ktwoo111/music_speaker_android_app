package com.csci448.rphipps.musync

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        host_button.setOnClickListener{
            val intent = QueueListActivity.createIntent(this.baseContext)
            startActivity(intent)
        }
        client_button.setOnClickListener{
            val intent = Intent(this.baseContext, MusicPlayerActivity::class.java)
            startActivity(intent)
        }
    }
}
