package com.oleksandrpriadko.backend.data.datasource

import com.oleksandrpriadko.backend.data.datasource.model.ResponseBackend
import com.oleksandrpriadko.kermit
import io.ktor.client.HttpClient
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.appendPathSegments
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.IOException

interface BackendApiV1 {
    suspend fun getDataResponseBackend(): ResponseBackend?
}

class BackendApiV1Impl(
    private val hostUrl: String
) : BackendApiV1 {

    // always the same, even if application/json requested
    // BODY Content-Type: text/plain; charset=utf-8
    private val contentType = ContentType.Text.Plain
    private val ktorClient = HttpClient(Android) {
        expectSuccess = true
        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                },
                // always the same, even if application/json requested
                // Response header `ContentType: text/plain; charset=utf-8`
                // Request header `Accept: application/json`
                contentType = contentType
            )
            engine {
                connectTimeout = 300000
                socketTimeout = 300000
            }
        }
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    kermit.withTag(this@BackendApiV1Impl.javaClass.simpleName).d { message }
                }
            }
            level = LogLevel.ALL
        }
        install(DefaultRequest) {
            url {
                protocol = URLProtocol.HTTPS
                host = hostUrl
                contentType(contentType)
            }
        }
    }

    override suspend fun getDataResponseBackend(): ResponseBackend? = ktorGet {
        url {
            // https://api.neds.com.au/rest/v1/racing/?method=nextraces&count=5
            // "" in path after "racing" for "racing/?"
            appendPathSegments("rest", "v1", "racing", "")
            parameter("method", "nextraces")
            // hardcoded to 50 so user will see at least 5 filtered races (potential area to
            // improve and make it smarter), can be dynamic if needed
            parameter("count", 50)
        }
    }

    private suspend inline fun <reified T> ktorGet(crossinline block: HttpRequestBuilder.() -> Unit): T? =
        try {
            ktorClient.get { block.invoke(this) }.body()
            // I did not implement extensive error handling, so just return null if related error
            // occurs
        } catch (e: ClientRequestException) {
            e.printStackTrace()
            null
        } catch (e: ServerResponseException) {
            e.printStackTrace()
            null
        } catch (e: IOException) {
            e.printStackTrace()
            null
        } catch (e: SerializationException) {
            e.printStackTrace()
            null
        } catch (e: NoTransformationFoundException) {
            e.printStackTrace()
            null
        }
}