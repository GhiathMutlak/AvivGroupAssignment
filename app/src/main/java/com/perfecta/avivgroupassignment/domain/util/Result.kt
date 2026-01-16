package com.perfecta.avivgroupassignment.domain.util

sealed interface AvivResult<out D, out E: Error> {
    data class Success<out D> (val data: D): AvivResult<D, Nothing>
    data class Failure<out E: Error> (
        val error: E,
        val cause: Throwable? = null
    ): AvivResult<Nothing, E>
}

inline fun <D, E: Error, R> AvivResult<D, E>.map(map: (D) -> R): AvivResult<R, E> {
    return when(this) {
        is AvivResult.Success -> AvivResult.Success(map(data))
        is AvivResult.Failure -> AvivResult.Failure(error, cause)
    }
}

fun <D, E : Error> AvivResult<D, E>.asEmptyResult(): EmptyResult<E> {
    return map {  }
}

inline fun <D, E : Error> AvivResult<D, E>.onSuccess(action: (D) -> Unit): AvivResult<D, E> {
    if (this is AvivResult.Success) {
        action(data)
    }
    return this
}

inline fun <D, E : Error> AvivResult<D, E>.onFailure(action: (E, Throwable?) -> Unit): AvivResult<D, E> {
    if (this is AvivResult.Failure) {
        action(error, cause)
    }
    return this
}

typealias EmptyResult<E> = AvivResult<Unit, E>