package com.rht6226.flickrfeed

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso


class PhotoDetailsActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_details)

        activateToolbar(true)

        val photo = intent.getSerializableExtra(PHOTO_TRANSFER) as Photo

        val photoTitle: TextView = findViewById(R.id.photo_title_text)
        val photoAuthor: TextView = findViewById(R.id.photo_author)
        val photoTags: TextView = findViewById(R.id.photo_tags)
        val photoImage: ImageView = findViewById(R.id.photo_image)


        photoTitle.text = resources.getString(R.string.photo_title_text, photo.title)
        photoAuthor.text = resources.getString(R.string.photo_author_text, photo.authorId)
        photoTags.text = resources.getString(R.string.photo_tags_text, photo.tags)

        Picasso.get().load(photo.link)
            .error(R.drawable.placeholder)
            .placeholder(R.drawable.placeholder)
            .into(photoImage)

    }
}