package com.rht6226.flickrfeed


import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.lang.Exception


class MainActivity : BaseActivity(), GetRawData.OnDownloadComplete,
    ParseFlickrJsonData.OnDataAvailable,
    RecyclerItemClickListener.OnRecyclerClickListener {

    private val TAG = "MainActivity"
    private val flickrRecyclerViewAdapter = FlickrRecyclerViewAdapter(ArrayList())
    private val recyclerView: RecyclerView by lazy { findViewById(R.id.recycler_view) }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, ".onCreate called")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        activateToolbar(false)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addOnItemTouchListener(RecyclerItemClickListener(this, recyclerView, this))
        recyclerView.adapter = flickrRecyclerViewAdapter

        Log.d(TAG, ".onCreate End")
    }

    override fun onResume() {
        Log.d(TAG, "onResume Starts")
        super.onResume()

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val queryResult = sharedPreferences.getString(FLICKR_QUERY, "")


        if (queryResult != null && queryResult.isNotEmpty()) {
            val url = createUri(
                "https://api.flickr.com/services/feeds/photos_public.gne",
                queryResult,
                "en-us",
                true
            )

            val getRawData = GetRawData(this)
            getRawData.execute(url)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        Log.d(TAG, ".onCreateOptionsMenu called")
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d(TAG, ".onOptionsItemSelected called")
        return when (item.itemId) {
            R.id.action_search -> {
                startActivity(Intent(this, SearchActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDownloadComplete(data: String, status: DownloadStatus) {
        if (status == DownloadStatus.OK) {
            val parseFlickrJsonData = ParseFlickrJsonData(this)
            parseFlickrJsonData.execute(data)
        } else {
            Log.d(TAG, ".onDownloadComplete: failed with status $status.\n Error message: $data")
        }
    }

    override fun onDataAvailable(data: List<Photo>) {
        Log.d(TAG, ".onDataAvailable called")
        flickrRecyclerViewAdapter.loadNewData(data)
    }

    override fun onError(exception: Exception) {
        Log.d(TAG, ".onError called with Exception $exception")
    }

    override fun onItemClick(view: View, position: Int) {
        Log.d(TAG, ".onItemClick called")
        Toast.makeText(this, "normal tap at position $position", Toast.LENGTH_SHORT).show()
    }

    override fun onItemLongClick(view: View, position: Int) {
        Log.d(TAG, ".onItemLongClick called")
        val photo = flickrRecyclerViewAdapter.getPhoto(position)
        if (photo != null) {
            val intent = Intent(this, PhotoDetailsActivity::class.java)
            intent.putExtra(PHOTO_TRANSFER, photo)
            startActivity(intent)
        }
    }

    private fun createUri(
        baseUrL: String,
        searchCriteria: String,
        lang: String,
        matchAll: Boolean
    ): String {
        Log.d(TAG, ".createUri starts")

        return Uri.parse(baseUrL)
            .buildUpon().appendQueryParameter("tags", searchCriteria)
            .appendQueryParameter("tagmode", if (matchAll) "All" else "ANY")
            .appendQueryParameter("lang", lang).appendQueryParameter("format", "json")
            .appendQueryParameter("nojsoncallback", "1").build().toString()
    }
}

