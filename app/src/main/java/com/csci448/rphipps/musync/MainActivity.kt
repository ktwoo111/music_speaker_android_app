package com.csci448.rphipps.musync

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import com.crashlytics.android.Crashlytics
import com.csci448.rphipps.AudioRetrieval.allAudios
import com.csci448.rphipps.musync.Servers.ServerHolder
import com.instacart.library.truetime.TrueTime
import io.fabric.sdk.android.Fabric
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.onComplete
import org.jetbrains.anko.toast


class MainActivity : AppCompatActivity() {
    companion object {
        private const val LOG_TAG = "448.MainAct"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(LOG_TAG, "onCreate() called")
        Fabric.with(this, Crashlytics())
        setContentView(R.layout.activity_main)


        doAsync {
            //NOTE: this is what is needed to sync system clock on all the phones
            //it's async operation for obivous reason
            TrueTime.build().initialize()
            Log.d(LOG_TAG,"True Time Initialized")
            onComplete{
                runOnUiThread {
                    toast("True Time Initialized")
                }
            }
        }


        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    123)
            }
        } else {

        }

        host_button.setOnClickListener{
            val wifiMan = this.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val wifiInf = wifiMan.connectionInfo
            val ipAddress = wifiInf.ipAddress
            val ip = String.format(
                "%d.%d.%d.%d", ipAddress and 0xff, ipAddress shr 8 and 0xff, ipAddress shr 16 and 0xff,
                ipAddress shr 24 and 0xff
            )

            //fetching all the audio files in phone
            allAudios.getAllAudioFromDevice(this)
            //start server
            ServerHolder.RunServer()

            val intent = PlayerQueueActivity.createIntent(this.baseContext, ip, "HOST")
            startActivity(intent)
        }
        client_button.setOnClickListener{
            val intent = ClientActivity.createIntent(this.baseContext)
            startActivity(intent)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            123 -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(LOG_TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(LOG_TAG, "onResume() called")
    }

    override fun onPause() {
        Log.d(LOG_TAG, "onPause() called")
        super.onPause()
    }

    override fun onStop() {
        Log.d(LOG_TAG, "onStop() called")
        super.onStop()
    }

    override fun onDestroy() {
        Log.d(LOG_TAG,"onDestroy Called")
        ServerHolder.StopServer()
        Log.d(LOG_TAG,"ServerStop triggered at PlayerQueueActivity")
        super.onDestroy()
    }
}
