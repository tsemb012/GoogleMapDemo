package com.example.googlemapdemo.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.googlemapdemo.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.MarkerOptions

class MainFragment : Fragment() {

    companion object { fun newInstance() = MainFragment() }

    private val places: List<Place> by lazy { PlacesReader(requireContext()).read() }
    private val bicycleIcon: BitmapDescriptor by lazy {
        val color = ContextCompat.getColor(requireContext(), R.color.black)
        BitmapHelper.vectorToBitmap(requireContext(), R.drawable.ic_launcher_foreground, color)
    }
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        val mapFragment = this.childFragmentManager.findFragmentById(R.id.map_fragment) as? SupportMapFragment
        mapFragment?.getMapAsync { googleMap ->
            addMarkers(googleMap)
            googleMap.setInfoWindowAdapter(MarkerInfoWindowAdapter(requireContext()))
        }
    }

    private fun addMarkers(googleMap: GoogleMap) {
        places.forEach { place ->
            val marker = googleMap.addMarker(
                MarkerOptions()
                    .title(place.name)
                    .position(place.latLng)
                    .icon(bicycleIcon)
            )
            // Set place as the tag on the marker object so it can be referenced within MarkerInfoWindowAdapter
            marker?.tag = place
        }
    }
}