package com.vertigo.simpletranslatorretrofitmoshi

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit


interface TranslateApi {
    @POST("translate?api-version=3.0&from=en&to=ru")
    @Headers("Ocp-Apim-Subscription-Key: KEY",
        "Ocp-Apim-Subscription-Region: LOCATION",
        "Content-type: application/json")
    suspend fun translateRuText(
        @Body body: RequestBody
    ): List<TranslateApiText>
}

object TranslateApiImpl {
    private var client = getOkHttpClient()

    // Для логирования
    private fun getOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(getLoggingInterceptor())
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS).writeTimeout(90, TimeUnit.SECONDS).build()
    }

    // Для логирования по уровням, кроме Body можно Headers ловить и прочее
    private fun getLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
            else HttpLoggingInterceptor.Level.NONE
        )
    }

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create())
        .baseUrl("https://api.cognitive.microsofttranslator.com/")
        .client(client)
        .build()

    private val translateApiService = retrofit.create(TranslateApi::class.java)

    suspend fun translateRuText(text: String): List<Text> {
        val body = RequestBody.create(
            "application/json".toMediaTypeOrNull(),
            "[{\"Text\": \"$text\"}]"
        )
        return withContext(Dispatchers.Default) {
            translateApiService.translateRuText(body).map {
                result -> Text(result.translations[0].text)
            }
        }
    }
}

