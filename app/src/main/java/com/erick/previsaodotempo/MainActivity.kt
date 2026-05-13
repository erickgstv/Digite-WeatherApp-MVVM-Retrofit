package com.erick.previsaodotempo

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: WeatherViewModel
    private val apiKey = "SUA_CHAVE_API_AQUI"

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)

        val rootLayout = findViewById<ConstraintLayout>(R.id.rootLayout)
        val etCity = findViewById<EditText>(R.id.etCity)
        val btnSearch = findViewById<Button>(R.id.btnSearch)
        val btnLocation = findViewById<ImageButton>(R.id.btnLocation)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        
        val tvCityName = findViewById<TextView>(R.id.tvCityName)
        val ivWeatherIcon = findViewById<ImageView>(R.id.ivWeatherIcon)
        val tvTemperature = findViewById<TextView>(R.id.tvTemperature)
        val tvDescription = findViewById<TextView>(R.id.tvDescription)
        val tvHumidity = findViewById<TextView>(R.id.tvHumidity)
        val tvFeelsLike = findViewById<TextView>(R.id.tvFeelsLike)
        val tvWindSpeed = findViewById<TextView>(R.id.tvWindSpeed)

        val rvForecast = findViewById<RecyclerView>(R.id.rvForecast)
        val forecastAdapter = ForecastAdapter(emptyList())
        rvForecast.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvForecast.adapter = forecastAdapter

        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        val savedCity = sharedPref.getString("last_city", null)
        if (savedCity != null) {
            etCity.setText(savedCity)
            viewModel.fetchWeather(savedCity, apiKey)
        }

        btnSearch.setOnClickListener {
            val city = etCity.text.toString()
            if (city.isNotEmpty()) {
                hideKeyboard()
                with(sharedPref.edit()) {
                    putString("last_city", city)
                    apply()
                }
                viewModel.fetchWeather(city, apiKey)
            } else {
                Toast.makeText(this, "Por favor, digite uma cidade", Toast.LENGTH_SHORT).show()
            }
        }

        btnLocation.setOnClickListener {
            checkLocationPermission()
        }

        viewModel.isLoading.observe(this) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.weatherData.observe(this) { weather ->
            weather?.let {
                tvCityName.text = it.name
                tvTemperature.text = "${it.main.temp.toInt()}°C"
                tvDescription.text = it.weather[0].description
                ivWeatherIcon.setImageResource(it.weather[0].getIconRes())
                
                tvHumidity.text = "${it.main.humidity}%"
                tvFeelsLike.text = "${it.main.feelsLike.toInt()}°C"
                tvWindSpeed.text = "${it.wind.speed} km/h"

                val color = Color.parseColor(it.weather[0].getBackgroundColor())
                rootLayout.setBackgroundColor(color)
            }
        }

        viewModel.forecastData.observe(this) { forecast ->
            forecast?.let {
                val dailyForecast = it.list.filterIndexed { index, _ -> index % 8 == 0 }
                forecastAdapter.updateData(dailyForecast)
            }
        }

        viewModel.error.observe(this) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
                resetUI(tvCityName, tvTemperature, tvDescription, ivWeatherIcon, tvHumidity, tvFeelsLike, tvWindSpeed, rootLayout)
            }
        }
    }

    private fun resetUI(cityName: TextView, temp: TextView, desc: TextView, icon: ImageView, hum: TextView, feels: TextView, wind: TextView, root: ConstraintLayout) {
        cityName.text = "Cidade"
        temp.text = "0°C"
        desc.text = "Descrição"
        icon.setImageDrawable(null)
        hum.text = "0%"
        feels.text = "0°C"
        wind.text = "0 km/h"
        root.setBackgroundColor(Color.parseColor("#455A64"))
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 100)
        } else {
            getUserLocation()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getUserLocation()
        }
    }

    private fun getUserLocation() {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        try {
            val location: Location? = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            if (location != null) {
                viewModel.fetchWeatherByLocation(location.latitude, location.longitude, apiKey)
            } else {
                Toast.makeText(this, "Não foi possível obter a localização", Toast.LENGTH_SHORT).show()
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }
}
