package com.example.trendingrepos.component

import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.trendingrepos.data.ApiResult
import com.google.gson.Gson
import retrofit2.HttpException

/** Fun show toast message to input text
 * @param message: Display message
 */
fun Fragment.toast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
}

/** Handle network api errors
 * @return Error message [String]
 */
fun Exception.handleError(): String {
    return when (this) {
        is HttpException -> {
            val errorMessage = getErrorMessageFromGenericResponse(this)
            if (errorMessage.isNullOrEmpty()) this.message.toString()
            else errorMessage
        }
        else -> this.message.toString()
    }
}


/** Extract generic message from response body
 * @param httpException: Network Http Exception
 */
private fun getErrorMessageFromGenericResponse(httpException: HttpException): String? {
    return try {
        val body = httpException.response()?.errorBody()?.string()
        val adapter = Gson().getAdapter(ApiResult.Error::class.java).fromJson(body)
        adapter.message
    } catch (e: Exception) {
        e.message.toString()
    }
}