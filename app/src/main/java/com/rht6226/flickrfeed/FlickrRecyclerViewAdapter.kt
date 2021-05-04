package com.rht6226.flickrfeed


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class FlickrImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var thumbnail: ImageView = view.findViewById(R.id.thumbnail)
    var title: TextView = view.findViewById(R.id.titleText)
}

class FlickrRecyclerViewAdapter(private var photoList: List<Photo>) :
    RecyclerView.Adapter<FlickrImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlickrImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.browse, parent, false)
        return FlickrImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: FlickrImageViewHolder, position: Int) {

        if (photoList.isEmpty()) {
            holder.thumbnail.setImageResource(R.drawable.placeholder)
            holder.title.setText(R.string.not_found)

        } else {
            val photoItem = photoList[position]
            Picasso.get().load(photoItem.image)
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(holder.thumbnail)

            holder.title.text = photoItem.title
        }
    }

    override fun getItemCount(): Int {
        return if (photoList.isNotEmpty()) photoList.size else 1
    }

    fun getPhoto(position: Int): Photo? {
        return if (photoList.isNotEmpty()) photoList[position] else null
    }

    fun loadNewData(newPhotos: List<Photo>) {
        photoList = newPhotos
        notifyDataSetChanged()
    }
}