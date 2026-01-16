package com.perfecta.avivgroupassignment.domain.util

sealed interface Result<out D, out E: Error> {
    data class Success<out D> (val data: D): Result<D, Nothing>
    data class Failure<out E: Error> (val error: E): Result<Nothing, E>
}

inline fun <D, E: Error, R> Result<D, E>.map(map: (D) -> R): Result<R, E> {
    return when(this) {
        is Result.Success -> Result.Success(map(data))
        is Result.Failure -> Result.Failure(error)
    }
}

fun <D, E : Error> Result<D, E>.asEmptyResult(): EmptyResult<E> {
    return map {  }
}

inline fun <D, E : Error> Result<D, E>.onSuccess(action: (D) -> Unit): Result<D, E> {
    if (this is Result.Success) {
        action(data)
    }
    return this
}

inline fun <D, E : Error> Result<D, E>.onFailure(action: (E) -> Unit): Result<D, E> {
    if (this is Result.Failure) {
        action(error)
    }
    return this
}

typealias EmptyResult<E> = Result<Unit, E>