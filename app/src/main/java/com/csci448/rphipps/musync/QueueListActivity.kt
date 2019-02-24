package com.csci448.rphipps.musync

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.widget.FrameLayout

class QueueListActivity: SingleFragmentActivity() {
    /*override fun onCrimeSelected(crime: Crime, position: Int) {
        if(findViewById<FrameLayout>(R.id.detail_fragment_container)==null) {
            val intent = CrimePagerActivity.createIntent(this, position)
            startActivity(intent)
        } else {
            val newDetail = CrimeDetailsFragment.createFragment(crime.id,
                position)
            supportFragmentManager.beginTransaction()
                .replace(R.id.detail_fragment_container, newDetail)
                .commit()
        }
    }*/

    /*override fun onCrimeUpdated() {
        val listFragment: CrimeListFragment? =
            supportFragmentManager.findFragmentById(R.id.fragment_container)
                    as CrimeListFragment
        listFragment?.updateUI()
    }*/

    companion object {
        fun createIntent(context: Context): Intent {
            val intent = Intent(context, QueueListActivity::class.java)
            return intent
        }
        private const val LOG_TAG = "448.CrimeListAct"
    }

    override fun createFragment(): Fragment {
        return QueueListFragment()
    }

    override fun getLogTag() = LOG_TAG
}