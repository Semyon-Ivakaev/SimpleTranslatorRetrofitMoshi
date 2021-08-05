package com.vertigo.simpletranslatorretrofitmoshi

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TranslateApiText(
    @Json(name = "translations") val translations: List<Translations>
)

@JsonClass(generateAdapter = true)
data class Translations(
    @Json(name = "text") val text: String
)
