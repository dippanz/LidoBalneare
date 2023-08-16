package com.example.lidobalneare

import androidx.annotation.DrawableRes
import java.io.Serializable


class ViewModelHomePage(@DrawableRes val image: Int, val title: String, val desc: String): Serializable {

    fun hasData(): Boolean{
        return title.isNotEmpty() && desc.isNotEmpty()
    }
}