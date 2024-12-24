package com.aantriav.intermediate2.ui.screen

import android.content.Intent
import android.content.res.Resources
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.aantriav.intermediate2.R
import com.aantriav.intermediate2.databinding.FragmentStoryMapsBinding
import com.aantriav.intermediate2.ui.viewModel.StoryMapsViewModel
import com.aantriav.intermediate2.ui.viewModel.factory.StoryMapsViewModelFactory
import com.aantriav.intermediate2.data.Result
import com.aantriav.intermediate2.data.remote.response.ListStoryItem
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions

class StoryMapsFragment : Fragment(), OnMapReadyCallback {

    private val storyMapViewModel: StoryMapsViewModel by viewModels {
        StoryMapsViewModelFactory.getInstance(requireActivity())
    }

    private lateinit var mMap: GoogleMap
    private var _binding: FragmentStoryMapsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStoryMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        setupUI()
        observeViewModel()
    }

    private fun setupUI() {
        binding.addFab.setOnClickListener {
            Intent(requireContext(), AddStoryActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    private fun observeViewModel() {
        storyMapViewModel.stories.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val stories = result.data
                    if (stories.isNotEmpty()) {
                        addAllMarker(stories)
                    } else {
                       Toast.makeText(requireContext(), (""), Toast.LENGTH_SHORT).show()
                    }
                }

                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                   Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
                }
            }
        }

        storyMapViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true

        storyMapViewModel.getAllStoriesWithMap()
    }

    private fun addAllMarker(stories: List<ListStoryItem>) {
        if (::mMap.isInitialized) {
            setMapStyle()
            val boundsBuilder = LatLngBounds.Builder()

            stories.forEach { story ->
                val lat = story.lat
                val lon = story.lon
                val latLng = LatLng(lat, lon)
                mMap.addMarker(
                    MarkerOptions()
                        .position(latLng)
                        .title(story.name)
                        .snippet(story.description)
                )
                boundsBuilder.include(latLng)
            }

            val bounds = boundsBuilder.build()
            val padding = 100
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding))
        }
    }

    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                        requireContext(),
                        R.raw.style_map
                    )
                )
            if (!success) {
               Toast.makeText(requireContext(), "Style parsing failed", Toast.LENGTH_SHORT).show()
            }
        } catch (exception: Resources.NotFoundException) {
            Toast.makeText(requireContext(), "Can't find style. Error: $exception", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}