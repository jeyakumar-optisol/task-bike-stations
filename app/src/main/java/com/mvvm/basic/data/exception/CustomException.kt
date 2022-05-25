package com.mvvm.basic.data.exception


import retrofit2.HttpException
import java.io.IOException

class CustomException(exception: Throwable) : Throwable() {
    private var exception: Throwable
    private var errorBody: String? = null

    init {
        when (exception) {
            is HttpException -> {
                errorBody = exception.response()?.errorBody()?.string()
            }
        }
        this.exception = exception
    }

    fun getError(): String {
        return when (exception) {
            is HttpException -> exception.localizedMessage ?: "undefined error"
            is IOException -> "No internet connection"
            else -> exception.localizedMessage ?: "uncaught error"
        }
    }
}