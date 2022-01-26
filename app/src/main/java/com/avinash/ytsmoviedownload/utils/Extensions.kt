package com.avinash.ytsmoviedownload.utils

import android.content.Context
import android.content.Intent
import android.webkit.MimeTypeMap
import androidx.activity.ComponentActivity
import androidx.core.content.FileProvider
import com.avinash.ytsmoviedownload.BuildConfig
import com.avinash.ytsmoviedownload.repository.remote.DownloadState
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.cio.*
import io.ktor.utils.io.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retryWhen
import org.koin.android.ext.koin.androidContext
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.context.GlobalContext
import java.io.File
import kotlin.math.roundToInt


@KoinInternalApi
val globalContext: Context =
    GlobalContext.getKoinApplicationOrNull()!!.koin.scopeRegistry.rootScope.androidContext()

suspend fun HttpClient.downloadFile(
    file: File,
    url: String,
    callback: suspend (boolean: Boolean) -> Unit
) {
    val call = request<HttpResponse> {
        url(url)
        method = HttpMethod.Get
    }
    if (!call.response.status.isSuccess()) {
        callback(false)
    }
    call.response.content.copyAndClose(file.writeChannel())
    callback(true)
}

suspend fun HttpClient.downloadFile(file: File, url: String): Flow<DownloadState> {
    return flow {
        val response: HttpResponse = request {
            headers {
                append(HttpHeaders.Accept, "application/octet-stream")
                append(HttpHeaders.Connection, "keep-alive")
                append(HttpHeaders.AcceptEncoding, "gzip, deflate, br")
                append(HttpHeaders.ContentType, "application/vnd.api+json")
                append(HttpHeaders.UserAgent, "Ktor client")
            }
            url(url)
            method = HttpMethod.Get
        }
        val data = ByteArray(response.contentLength()!!.toInt())
        var offset = 0
        do {
            val currentRead = response.content.readAvailable(data, offset, data.size)
            offset += currentRead
            val progress = (offset * 100f / data.size).roundToInt()
            emit(DownloadState.Progress(progress))
        } while (currentRead > 0)
        response.close()
        if (response.status.isSuccess()) {
            file.writeBytes(data)
            emit(DownloadState.Success)
        } else {
            emit(DownloadState.Error("File not downloaded"))
        }
    }.retryWhen { cause, attempt ->
        cause.printStackTrace()
        cause is NullPointerException || cause is ServerResponseException
    }.catch {
        emit(DownloadState.Error("File not downloaded"))
    }
}

var globalActivity: ComponentActivity? = null

fun openFile(file: File) {
    Intent(Intent.ACTION_VIEW).apply {
        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION

        addCategory(Intent.CATEGORY_DEFAULT)
        val uri = FileProvider.getUriForFile(
            globalActivity!!,
            BuildConfig.APPLICATION_ID + ".provider",
            file
        )
        setDataAndType(uri, "application/x-bittorrent")
        globalActivity!!.startActivity(this)

    }
}

fun getMimeType(file: File): String? {
    val extension = file.extension
    return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
}
