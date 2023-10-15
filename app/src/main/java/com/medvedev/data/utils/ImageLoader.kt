package com.medvedev.data.utils

import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.Toast
import com.medvedev.app.App
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.load.DataSource
import com.medvedev.mechanic.R

fun loadRoundImage(url: String, imageView: ImageView, listener: RequestListener<Drawable>) {
    Glide.with(App.instance)
        .load(url)
        .circleCrop()
        .listener(listener)
        .into(imageView)
}

fun loadRoundImage(url: String, imageView: ImageView) {
    Glide.with(App.instance)
        .load(url)
        .error(R.drawable.ic_image_not_found_24dp)
        .circleCrop()
        .listener(object : RequestListener<Drawable> {
            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                return false
            }

            override fun onLoadFailed(
                exception: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                Toast.makeText(
                    App.instance,
                    App.instance.resources.getString(R.string.image_not_found),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }
        })
        .into(imageView)
}

fun loadImage(url: String, imageView: ImageView) {
    Glide.with(App.instance)
        .load(url)
        .error(R.drawable.ic_image_not_found_24dp)
        .listener(object : RequestListener<Drawable> {
            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                return false
            }

            override fun onLoadFailed(
                exception: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                Toast.makeText(
                    App.instance,
                    App.instance.resources.getString(R.string.image_not_found),
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }
        })
        .into(imageView)
}

fun loadImage(url: String, imageView: ImageView, listener: RequestListener<Drawable>) {
    Glide.with(App.instance)
        .load(url)
        .error(R.drawable.ic_image_not_found_24dp)
        .listener(listener)
        .into(imageView)
}