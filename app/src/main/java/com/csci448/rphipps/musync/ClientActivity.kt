package com.csci448.rphipps.musync

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import com.csci448.rphipps.musync.Client.ClientWebSocket

import kotlinx.android.synthetic.main.client_layout.*
import okhttp3.Request

class ClientActivity : AppCompatActivity() {
    companion object {
        var ipAddress = ""
        private const val LOG_TAG = "ClientActivity"

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
            if(ipAddress.matches(Regex("^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\$"))){
                val intent = PlayerQueueActivity.createIntent(this.baseContext, ipAddress, "CLIENT")
                startActivity(intent)
            }
            else{
                Toast.makeText(this, "Invalid IP address", Toast.LENGTH_SHORT).show()
                ip_text_box.text.clear()
            }
        }
    }
}