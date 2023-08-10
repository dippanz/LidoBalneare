package com.example.lidobalneare

import android.graphics.drawable.Drawable
import java.io.Serializable


class ViewModelHomePage(val image: Drawable?, val title: String, val desc: String): Serializable {

    fun hasData(): Boolean{
        return title.isNotEmpty() && desc.isNotEmpty() && image != null
    }
}