package com.avinash.ytsmoviedownload.repository.remote

import com.avinash.ytsmoviedownload.repository.local.database.model.ApiResponse
import com.avinash.ytsmoviedownload.utils.downloadFile
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retryWhen
import java.io.File
import java.lang.StringBuilder


val ktorHttpClient: HttpClient
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
            logger = Logger.DEFAULT
            level = LogLevel.BODY
        }
    }


class ApiUtility {

    private val baseUrl = "https://yts.mx/api/v2/"

    suspend fun getMovies(query: String, page: Int): Flow<Result<ApiResponse>> =
        request(endPoint = "list_movies.json", addParams = {
            addParam("limit", "50")
            addParam("query_term", query)
            addParam("page", page.toString())

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
        }.retryWhen { cause, attempt ->
            cause.printStackTrace()
            cause is NullPointerException || cause is ServerResponseException
        }.catch { error ->
            emit(Result.failure(error))
        }


    private fun StringBuilder.addParam(param: String, value: String) {
        append("&$param").append("=$value")
    }

    suspend fun downloadFile(file: File, url: String): Flow<DownloadState> =
        ktorHttpClient.downloadFile(file = file, url = url)


//    fun downloadFile() = ktorHttpClient.downloadFile( )

}


sealed class DownloadState {

    object Success : DownloadState()

    object Pending : DownloadState()

    data class Error(val message: String, val cause: Exception? = null) : DownloadState()

    data class Progress(val progress: Int) : DownloadState()
}

