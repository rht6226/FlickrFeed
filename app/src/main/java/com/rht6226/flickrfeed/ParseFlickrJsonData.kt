package com.rht6226.flickrfeed

import android.os.AsyncTask
import android.util.Log
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception

class ParseFlickrJsonData(private val listener: OnDataAvailable) :
    AsyncTask<String, Void, ArrayList<Photo>>() {

    private val TAG = "ParseFlickrJsonData"

    interface OnDataAvailable {
        fun onDataAvailable(data: List<Photo>)
        fun onError(exception: Exception)
    }

    override fun doInBackground(vararg params: String?): ArrayList<Photo> {

        val photoList = ArrayList<Photo>()

        try {
            val jsonData = JSONObject(params[0])
            val itemsArray = jsonData.getJSONArray("items")

            for (i in 0 until itemsArray.length()) {
                val jsonPhoto = itemsArray.getJSONObject(i)
                val title = jsonPhoto.getString("title")
                val author = jsonPhoto.getString("author")
                val authorId = jsonPhoto.getString("author_id")
                val tags = jsonPhoto.getString("tags")
                val photoUrl = jsonPhoto.getJSONObject("media").getString("m")
                val link = photoUrl.replaceFirst("_m.jpg", "_b.jpg")

                val photoObject = Photo(title, author, authorId, link, tags, photoUrl)
                photoList.add(photoObject)

                Log.d(TAG, ".doInBackground : $photoObject")
            }
        } catch (e: JSONException) {
            e.printStackTrace()
            Log.e(TAG, ".doInBackground :Error processing Json data. ${e.message}")
            cancel(true)
            listener.onError(e)
        }

        return photoList
    }

    override fun onPostExecute(result: ArrayList<Photo>) {
        super.onPostExecute(result)
        listener.onDataAvailable(result)
    }
}