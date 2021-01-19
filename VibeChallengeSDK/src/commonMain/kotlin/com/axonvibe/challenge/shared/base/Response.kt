package com.axonvibe.challenge.shared.base

open class Response<out T> {
    class Success<out T>(val data: T) : Response<T>()
    data class Error(val exception: Exception) : Response<Nothing>()
}