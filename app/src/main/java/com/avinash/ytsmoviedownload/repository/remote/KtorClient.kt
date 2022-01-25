package com.avinash.ytsmoviedownload.repository.remote

import android.util.Log
import com.avinash.ytsmoviedownload.repository.local.database.model.ApiResponse
import com.avinash.ytsmoviedownload.repository.remote.model.MovieFromApi
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.features.observer.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retryWhen
import java.lang.StringBuilder


val ApiUtility.ktorHttpClient: HttpClient
    get() = HttpClient(Android) {
        install(JsonFeature) {
            serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
            engine {
                connectTimeout = 5000
                socketTimeout = 5000
            }
        }

        install(Logging) {
            logger = Logger.ANDROID
            level = LogLevel.BODY
        }

        install(ResponseObserver) {
            onResponse { response ->
                Log.d("HTTP status:", "${response.status.value}")
            }
        }

        install(DefaultRequest) {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
        }
    }


class ApiUtility {

    private val baseUrl = "https://yts.mx/api/v2/"

    suspend fun getMovies(): Flow<Result<ApiResponse>> = request(endPoint = "list_movies.json", addParams = {
        //addParam("","")
    })


    private suspend inline fun <reified T> request(
        requestType: HttpMethod = HttpMethod.Get,
        endPoint: String,
        crossinline addParams: StringBuilder.() -> Unit = {}
    ) =
        flow<Result<T>> {
            val urlBuilder = StringBuilder(baseUrl).append(endPoint).append("?")
            urlBuilder.addParams()
            emit(Result.success(ktorHttpClient.request {
                url(urlBuilder.toString())
                method = requestType
            }))
        }.catch { error ->
            emit(Result.failure(error))
        }


    private fun StringBuilder.addParam(param : String, value: String){
        append("&$param").append("=$value")
    }

    infix fun StringBuilder.param(param: String): StringBuilder {

        return this
    }

    infix fun StringBuilder.value(value: String) {

    }
}
