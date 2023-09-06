package com.example.lidobalneare

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.Serializable


class ViewModelHomePage(private val image: ByteArray, val title: String, val desc: String): Serializable {
    fun hasData(): Boolean{
        return title.isNotEmpty() && desc.isNotEmpty()
    }

    fun getImageBitmap(): Bitmap {
        return BitmapFactory.decodeByteArray(image, 0, image.size)
    }


}