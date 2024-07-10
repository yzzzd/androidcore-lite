package com.nuvyz.core.utils

import android.location.Geocoder
import android.os.Build
import com.google.android.gms.maps.model.LatLng

class AddressHelper(private val geocoder: Geocoder) {
    fun getAddress(latLng: LatLng, address: (String) -> Unit) {
        // address("Getting address...")
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1) { addresses ->
                    val addressFound = addresses[0].getAddressLine(0)
                    val city = addresses[0].locality
                    val subCity = addresses[0].subLocality

                    val state = addresses[0].adminArea
                    val subState = addresses[0].subAdminArea

                    val addressComplete = "${addressFound}, ${city}, ${subCity}, ${state}, $subState"

                    address(addressComplete)
                }
            } else {
                val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
                if (!addresses.isNullOrEmpty()) {
                    val addressFound = addresses[0].getAddressLine(0)
                    val city = addresses[0].locality
                    val subCity = addresses[0].subLocality

                    val state = addresses[0].adminArea
                    val subState = addresses[0].subAdminArea

                    val addressComplete = "${addressFound}, ${city}, ${subCity}, ${state}, $subState"

                    address(addressComplete)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            address("Unable getting address, please check your location and try again.")
        }
    }
}