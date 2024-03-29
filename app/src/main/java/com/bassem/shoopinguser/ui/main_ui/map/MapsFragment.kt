package com.bassem.shoopinguser.ui.main_ui.map

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.location.Geocoder
import android.location.Location
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bassem.shoopinguser.R
import com.bassem.shoopinguser.databinding.FragmentMapsBinding
import com.bassem.shoopinguser.ui.login.SignupFragment
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import pub.devrel.easypermissions.EasyPermissions
import java.util.*

class MapsFragment : Fragment(R.layout.fragment_maps), OnMapReadyCallback,
    EasyPermissions.PermissionCallbacks {
    var binding: FragmentMapsBinding? = null
    private var fusedLocation: FusedLocationProviderClient? = null
    private var map: GoogleMap? = null
    private var location: Location? = null
    private var currentMarker: Marker? = null
    private var currentAddress: String? = null
    private var locationCallback: LocationCallback? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding?.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocation = LocationServices.getFusedLocationProviderClient(requireContext())


        getLocation()

        binding?.confirm?.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("address", currentAddress)
            val signup = SignupFragment()
            signup.arguments = bundle

            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerLogin, signup)
            transaction.commit()


        }


    }


    @SuppressLint("MissingPermission")
    private fun getLocation() {
        Log.d("Location", "trying too")

        if (hasPermission()) {
            Log.d("Location", "i have the permission")

            fusedLocation?.lastLocation?.addOnSuccessListener { loc ->
                if (loc != null) {
                    val mapFragment =
                        childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
                    mapFragment.getMapAsync(this)

                    location = loc
                } else {
                    Log.d("Location", "Else")

                    // getCurrentLocation()
                    val dialog = Dialog(requireContext())
                    dialog.apply {
                        setTitle("We can't access your location")
                        show()

                    }

                }

            }

        } else {
            requestPermission()
            Log.d("Location", "Don't have it")
        }


    }


    private fun hasPermission() =
        EasyPermissions.hasPermissions(
            requireContext(),
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )

    private fun requestPermission() {
        EasyPermissions.requestPermissions(
            this,
            "please allow to detect your address",
            101,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
    }


    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        val latlon = LatLng(location!!.latitude, location!!.longitude)
        Log.d("Location", latlon.toString())
        binding?.mylocation?.setOnClickListener {
            Log.d("Location", "onClick")

            currentMarker?.remove()
            drawMarker(latlon)


        }



        map?.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
            override fun onMarkerDrag(p0: Marker) {


            }

            override fun onMarkerDragEnd(p0: Marker) {
                if (currentMarker != null) {
                    currentMarker?.remove()
                    val newlatlong = LatLng(p0.position.latitude, p0.position.longitude)
                    drawMarker(newlatlong)
                }

                println("you dragged")
            }

            override fun onMarkerDragStart(p0: Marker) {
            }
        })


    }

    private fun drawMarker(latlon: LatLng) {
        val markerOptions = MarkerOptions().position(latlon).title("Your current location")
            .snippet(getAddress(latlon.latitude, latlon.longitude)).draggable(true)
        map?.apply {
            animateCamera(CameraUpdateFactory.newLatLng(latlon))
            animateCamera(CameraUpdateFactory.newLatLngZoom(latlon, 25f))
        }
        currentMarker = map?.addMarker(markerOptions)
        currentMarker?.showInfoWindow()
    }

    private fun getAddress(latitude: Double, longitude: Double): String {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        val address = geocoder.getFromLocation(latitude, longitude, 1)
        currentAddress = address[0].getAddressLine(0).toString()
        return currentAddress as String
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {

        getLocation()

    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
    }


    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        val locationRequest = LocationRequest().setInterval(2000).setFastestInterval(2000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        fusedLocation?.requestLocationUpdates(locationRequest, object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)

                for (current in p0.locations) {
                    location = current
                }


            }
        }, Looper.myLooper()!!)
    }


}