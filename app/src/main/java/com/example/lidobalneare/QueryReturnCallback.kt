package com.example.lidobalneare


interface QueryReturnCallback<T> {
    fun onReturnValue(response: T, message: String)

    fun onQueryFailed(fail: String)

    fun onQueryError(error: String)
}