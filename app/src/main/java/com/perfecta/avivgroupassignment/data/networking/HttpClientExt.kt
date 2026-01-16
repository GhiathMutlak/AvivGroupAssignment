package com.perfecta.avivgroupassignment.data.networking

import com.perfecta.avivgroupassignment.di.BASE_URL
import com.perfecta.avivgroupassignment.domain.util.AvivResult
import com.perfecta.avivgroupassignment.domain.util.DataError
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.SerializationException
import java.nio.channels.UnresolvedAddressException

suspend inline fun <reified Response: Any> HttpClient.get(
    route: String,
    queryParameters: Map<String, Any?> = mapOf()
): AvivResult<Response, DataError.Network> {
    return safeCall {
        get {
            url(constructRoute(route))
            queryParameters.forEach { (key, value) ->
                parameter(key, value)
            }
        }
    }
}

suspend inline fun <reified Request, reified Response: Any> HttpClient.post(
    route: String,
    body: Request
): AvivResult<Response, DataError.Network> {
    return safeCall {
        post {
            url(constructRoute(route))
            setBody(body)
        }
    }
}

suspend inline fun <reified Response: Any> HttpClient.delete(
    route: String,
    queryParameters: Map<String, Any?> = mapOf()
): AvivResult<Response, DataError.Network> {
    return safeCall {
        delete {
            url(constructRoute(route))
            queryParameters.forEach { (key, value) ->
                parameter(key, value)
            }
        }
    }
}

suspend inline fun <reified T> safeCall(execute: () -> HttpResponse): AvivResult<T, DataError.Network> {
    val response = try {
        execute()
    } catch(e: UnresolvedAddressException) {
        return AvivResult.Failure(DataError.Network.NO_INTERNET, e)
    } catch (e: SerializationException) {
        return AvivResult.Failure(DataError.Network.SERIALIZATION, e)
    } catch(e: Exception) {
        currentCoroutineContext().ensureActive()
        return AvivResult.Failure(DataError.Network.UNKNOWN, e)
    }

    return responseToResult(response)
}

suspend inline fun <reified T> responseToResult(response: HttpResponse): AvivResult<T, DataError.Network> {
    return try {
        when(response.status.value) {
            in 200..299 -> AvivResult.Success(response.body<T>())
            401 -> AvivResult.Failure(DataError.Network.UNAUTHORIZED)
            408 -> AvivResult.Failure(DataError.Network.REQUEST_TIMEOUT)
            409 -> AvivResult.Failure(DataError.Network.CONFLICT)
            413 -> AvivResult.Failure(DataError.Network.PAYLOAD_TOO_LARGE)
            429 -> AvivResult.Failure(DataError.Network.TOO_MANY_REQUESTS)
            in 500..599 -> AvivResult.Failure(DataError.Network.SERVER_ERROR)
            else -> AvivResult.Failure(DataError.Network.UNKNOWN)
        }
    } catch (e: SerializationException) {
        AvivResult.Failure(DataError.Network.SERIALIZATION, e)
    }
}

fun constructRoute(route: String): String {
    return when {
        route.contains(BASE_URL) -> route
        route.startsWith("/") -> BASE_URL + route
        else -> BASE_URL + "/$route"
    }
}