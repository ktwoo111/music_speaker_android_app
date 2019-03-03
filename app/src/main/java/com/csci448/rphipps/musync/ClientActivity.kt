package com.csci448.rphipps.musync

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher

import kotlinx.android.synthetic.main.client_layout.*

class ClientActivity : AppCompatActivity() {
    companion object {
        var ipAddress = ""

        fun createIntent(context: Context) : Intent {
            val intent = Intent(context, ClientActivity::class.java)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.client_layout)

        ip_text_box.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                ipAddress = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        submit_button.setOnClickListener {
            val intent = MusicPlayerActivity.createIntent(this.baseContext, ipAddress)
            startActivity(intent)
        }
    }
}