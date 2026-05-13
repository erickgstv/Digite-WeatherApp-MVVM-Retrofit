package com.erick.previsaodotempo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherViewModel : ViewModel() {

    val weatherData = MutableLiveData<WeatherResponse?>()
    val forecastData = MutableLiveData<ForecastResponse?>()
    val error = MutableLiveData<String?>()
    val isLoading = MutableLiveData<Boolean>()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org/data/2.5/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service = retrofit.create(WeatherService::class.java)

    fun fetchWeather(city: String, apiKey: String) {
        isLoading.value = true
        service.getWeather(city, apiKey).enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.isSuccessful) {
                    weatherData.value = response.body()
                    fetchForecast(city, apiKey)
                } else {
                    isLoading.value = false
                    error.value = "Erro: Verifique o nome da cidade."
                }
            }
            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                isLoading.value = false
                error.value = "Falha na conexão: ${t.message}"
            }
        })
    }

    private fun fetchForecast(city: String, apiKey: String) {
        service.getForecast(city, apiKey).enqueue(object : Callback<ForecastResponse> {
            override fun onResponse(call: Call<ForecastResponse>, response: Response<ForecastResponse>) {
                isLoading.value = false
                if (response.isSuccessful) {
                    forecastData.value = response.body()
                }
            }
            override fun onFailure(call: Call<ForecastResponse>, t: Throwable) {
                isLoading.value = false
            }
        })
    }

    fun fetchWeatherByLocation(lat: Double, lon: Double, apiKey: String) {
        isLoading.value = true
        service.getWeatherByLocation(lat, lon, apiKey).enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.isSuccessful) {
                    weatherData.value = response.body()
                    fetchForecastByLocation(lat, lon, apiKey)
                } else {
                    isLoading.value = false
                    error.value = "Erro ao buscar localização."
                }
            }
            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                isLoading.value = false
                error.value = "Falha na conexão: ${t.message}"
            }
        })
    }

    private fun fetchForecastByLocation(lat: Double, lon: Double, apiKey: String) {
        service.getForecastByLocation(lat, lon, apiKey).enqueue(object : Callback<ForecastResponse> {
            override fun onResponse(call: Call<ForecastResponse>, response: Response<ForecastResponse>) {
                isLoading.value = false
                if (response.isSuccessful) {
                    forecastData.value = response.body()
                }
            }
            override fun onFailure(call: Call<ForecastResponse>, t: Throwable) {
                isLoading.value = false
            }
        })
    }
}
