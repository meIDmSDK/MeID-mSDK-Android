package sk.minv.base.base.interactor

import retrofit2.Response
import sk.minv.base.exceptions.ServerException
import sk.minv.base.exceptions.UnexpectedException

inline fun <T : Any> Response<T>.onSuccess(allowEmptyBody: Boolean = false, action: (T?) -> Unit): Response<T> {
    if (isSuccessful) {
        if (allowEmptyBody || body() != null) {
            action(body())
        }
    }
    return this
}

inline fun <T : Any> Response<T>.onFailure(action: (ServerException) -> Unit) {
    if (!isSuccessful) {
        action(ServerException(code(), errorBody()?.string()))
    }
}

fun <T : Any> Response<T>.getResult(allowEmptyBody: Boolean = false, defaultValue: T? = null): Result<T> {
    onSuccess(allowEmptyBody) {
        if (it != null) {
            return Success(it)
        } else if (allowEmptyBody && defaultValue != null) {
            // Return a predefined default value like `Unit` or a placeholder object
            return Success(defaultValue)
        }
    }
    onFailure { return Failure(it) }
    return Failure(UnexpectedException("Response parsing error"))
}