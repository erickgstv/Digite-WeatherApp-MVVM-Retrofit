package com.erick.previsaodotempo

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("main") val main: Main,
    @SerializedName("weather") val weather: List<Weather>,
    @SerializedName("wind") val wind: Wind,
    @SerializedName("name") val name: String,
    @SerializedName("dt_txt") val dtTxt: String? = null
)

data class ForecastResponse(
    @SerializedName("list") val list: List<WeatherResponse>
)

data class Main(
    @SerializedName("temp") val temp: Double,
    @SerializedName("humidity") val humidity: Int,
    @SerializedName("feels_like") val feelsLike: Double
)

data class Wind(
    @SerializedName("speed") val speed: Double
)

data class Weather(
    @SerializedName("description") val description: String,
    @SerializedName("icon") val icon: String
)

fun Weather.getIconRes(): Int {
    return when (icon) {
        "01d", "01n" -> R.drawable.ic_sun
        "02d", "02n", "03d", "03n", "04d", "04n" -> R.drawable.ic_cloud
        "09d", "09n", "10d", "10n" -> R.drawable.ic_rain
        else -> R.drawable.ic_sun
    }
}

fun Weather.getBackgroundColor(): String {
    return when (icon) {
        "01d", "01n" -> "#87CEEB"
        "02d", "02n", "03d", "03n", "04d", "04n" -> "#B0C4DE"
        "09d", "09n", "10d", "10n" -> "#708090"
        else -> "#87CEEB"
    }
}