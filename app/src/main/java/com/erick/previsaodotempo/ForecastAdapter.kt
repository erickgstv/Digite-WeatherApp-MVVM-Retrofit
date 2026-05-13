package com.erick.previsaodotempo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ForecastAdapter(private var forecastList: List<WeatherResponse>) :
    RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>() {

    class ForecastViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDate: TextView = view.findViewById(R.id.tvForecastDate)
        val ivIcon: ImageView = view.findViewById(R.id.ivForecastIcon)
        val tvTemp: TextView = view.findViewById(R.id.tvForecastTemp)
        val tvDesc: TextView = view.findViewById(R.id.tvForecastDesc)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_forecast, parent, false)
        return ForecastViewHolder(view)
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        val item = forecastList[position]
        
        // Formata a data simples (ex: 2023-10-27 12:00:00 -> 27/10 12h)
        val datePart = item.dtTxt?.substring(5, 16)?.replace("-", "/")?.replace(" ", " ") ?: ""
        
        holder.tvDate.text = datePart
        holder.tvTemp.text = "${item.main.temp.toInt()}°C"
        holder.tvDesc.text = item.weather[0].description
        holder.ivIcon.setImageResource(item.weather[0].getIconRes())
    }

    override fun getItemCount() = forecastList.size

    fun updateData(newList: List<WeatherResponse>) {
        forecastList = newList
        notifyDataSetChanged()
    }
}
