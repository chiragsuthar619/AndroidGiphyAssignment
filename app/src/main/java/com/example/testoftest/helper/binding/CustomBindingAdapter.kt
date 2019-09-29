package com.example.androidassignment.helper.binding


import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.testoftest.R
import com.example.testoftest.helper.extention.getParentActivity

// Handle all this event from xml

object CustomBindingAdapter {


    /**
     * This method used for loading image from server with ic_placeholder
     * @param image_path: image url
     *
     */

    @BindingAdapter("setImage")
    @JvmStatic
    fun setImage(view: AppCompatImageView, image_path : String?) {
        Glide.with(view.context)
            .load(image_path?:"")
            .apply(RequestOptions().placeholder(R.color.colorGreyLite).error(R.color.colorGreyLite))
            .into(view)

    }

    /**
     * This method used for load the GIF file
     * @param image_path: image url
     *
     */

    @BindingAdapter("setGif")
    @JvmStatic
    fun setGif(view: AppCompatImageView, image_path : String?) {
        Glide.with(view.context).asGif()
            .load(image_path?:"")
            .apply(RequestOptions().placeholder(R.color.colorGreyLite).error(R.color.colorGreyLite))
            .into(view)

    }

    /**
     * Visibility handle from view model
     */
    @BindingAdapter("mutableVisibility")
    @JvmStatic
    fun setMutableVisibility(view: View, visibility: MutableLiveData<Int>?) {
        val parentActivity: AppCompatActivity? = view.getParentActivity()
        if(parentActivity != null && visibility != null) {
            visibility.observe(parentActivity, Observer { value -> view.visibility = value?:View.VISIBLE})
        }
    }

}