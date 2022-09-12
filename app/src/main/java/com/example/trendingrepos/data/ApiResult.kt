package com.example.trendingrepos.data

/**
 * This sealed class is responsible for dynamically handle network apis result with the
 * state Loading, Success, and Error
 */
sealed class ApiResult<T>(val data: T? = null, val message: String? = null) {

    class Success<T>(data: T) : ApiResult<T>(data)

    class Error<T>(message: String?, data: T? = null) : ApiResult<T>(data, message)

    class Loading<T> : ApiResult<T>()

}
