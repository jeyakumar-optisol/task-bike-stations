package com.mvvm.basic.data.exception


import retrofit2.HttpException
import java.io.IOException

/*handle network error within coroutines call*/
class CustomException(exception: Throwable) : Throwable() {
    private var exception: Throwable
    private var errorBody: String? = null

    init {
        when (exception) {
            is HttpException -> { //when http exception occur
                errorBody = exception.response()?.errorBody()?.string() //returning actual message
            }
        }
        this.exception = exception
    }

    fun getError(): String {
        //masking the message by it type

        return when (exception) {
            is HttpException -> exception.localizedMessage ?: "undefined error"
            is IOException -> "No internet connection"
            else -> exception.localizedMessage ?: "uncaught error"
        }
    }
}