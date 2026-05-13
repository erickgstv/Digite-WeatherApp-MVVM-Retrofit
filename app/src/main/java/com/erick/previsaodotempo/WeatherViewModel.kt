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
                isLoading.value = false
                if (response.isSuccessful) {
                    weatherData.value = response.body()
                    error.value = null
                } else {
                    error.value = "Erro: Verifique o nome da cidade ou sua chave API."
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                isLoading.value = false
                error.value = "Falha na conexão: ${t.message}"
            }
        })
    }

    fun fetchWeatherByLocation(lat: Double, lon: Double, apiKey: String) {
        isLoading.value = true
        service.getWeatherByLocation(lat, lon, apiKey).enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                isLoading.value = false
                if (response.isSuccessful) {
                    weatherData.value = response.body()
                    error.value = null
                } else {
                    error.value = "Erro ao buscar localização."
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                isLoading.value = false
                error.value = "Falha na conexão: ${t.message}"
            }
        })
    }
}