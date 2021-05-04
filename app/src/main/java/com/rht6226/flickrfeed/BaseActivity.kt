package com.rht6226.flickrfeed

import android.util.Log
import androidx.appcompat.app.AppCompatActivity

internal const val FLICKR_QUERY = "FLICKR_QUERY"
internal const val PHOTO_TRANSFER = "PHOTO_TRANSFER"

open class BaseActivity : AppCompatActivity() {
    private val TAG = "BaseActivity"

    internal fun activateToolbar(enableHome: Boolean) {
        Log.d(TAG, ".activateToolbar")
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(enableHome)
    }
}