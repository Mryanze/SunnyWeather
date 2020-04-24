package com.yzh.sunnyweather.ui.place

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yzh.sunnyweather.R
import com.yzh.sunnyweather.logic.model.Place
import com.yzh.sunnyweather.ui.weather.WeatherActivity

class PlaceAdapter(private val fragment: PlaceFragment, private val placeList: List<Place>) :RecyclerView.Adapter<PlaceAdapter.ViewHodler>(){

    inner class ViewHodler(view :View):RecyclerView.ViewHolder(view){
        val placeName:TextView=view.findViewById(R.id.placeName)
        val placeAddress:TextView=view.findViewById(R.id.placeAddress)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHodler {
  val view= LayoutInflater.from(parent.context).inflate(R.layout.place_item,parent,false)

        val hodler = ViewHodler(view)
        hodler.itemView.setOnClickListener {
            val location=placeList[hodler.adapterPosition]
            fragment.viewModel.savePlace(location)
            fragment.startActivity(Intent(parent.context,WeatherActivity::class.java).apply {
                putExtra("location_lng",location.location.lng)
                putExtra("location_lat",location.location.lat)
                putExtra("place_name",location.name)
            })
            fragment.activity?.finish()

        }
        return hodler
    }

    override fun onBindViewHolder(holder: ViewHodler, position: Int) {
        val place=placeList[position]
        holder.placeName.text=place.name
        holder.placeAddress.text=place.address

    }

    override fun getItemCount(): Int {
       return placeList.size
    }
}