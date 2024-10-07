package com.riggle.finza_finza.utils



data class Resource<out T>(val status: Status, val data: T?, val message: String?) {

    companion object {

        fun <T> success(data: T?, message: String?): Resource<T> {
            return Resource(Status.SUCCESS, data, message)
        }

        fun <T> error(data: T?, msg: String): Resource<T> {
            return Resource(Status.ERROR, data, msg)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }

        fun <T> warn(data: T?, msg: String?): Resource<T> {
            return Resource(Status.WARN, data, msg)
        }

    }

}