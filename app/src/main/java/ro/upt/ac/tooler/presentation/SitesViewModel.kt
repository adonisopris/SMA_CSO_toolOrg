package ro.upt.ac.tooler.presentation

import android.content.Context
import android.location.Address
import android.location.Geocoder
import androidx.lifecycle.ViewModel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import ro.upt.ac.tooler.domain.Site
import ro.upt.ac.tooler.domain.SiteRepository
import ro.upt.ac.tooler.domain.SiteType
import ro.upt.ac.tooler.domain.SiteTypeRepository
import ro.upt.ac.tooler.domain.Tool
import java.util.Locale

class SitesViewModel(private val siteRepository: SiteRepository, private val siteTypeRepository: SiteTypeRepository): ViewModel() {
    private val _sitesListState =  MutableStateFlow(siteRepository.getSites())
    val sitesListState: StateFlow<List<Site>> = _sitesListState.asStateFlow()

    private val _siteTypes =  MutableStateFlow(siteTypeRepository.getSiteTypes())
    val siteTypes : StateFlow<List<SiteType>> = _siteTypes

    private fun retrieveSites() {
        _sitesListState.value = siteRepository.getSites()
    }

    private fun retrieveSiteTypes(){
        _siteTypes.value = siteTypeRepository.getSiteTypes()
    }

    fun addSiteType(name: String){
        siteTypeRepository.addSiteType(SiteType(name = name))
        retrieveSiteTypes()
    }

    fun addSite(name: String, type: String, details: String, latitude: Double, longitude: Double) {
        siteRepository.addSite(Site(
            name = name, type = type,
            details = details,
            latitude = latitude, longitude = longitude))
        retrieveSites()
    }


    fun removeSite(site : Site) {
        siteRepository.removeSite(site)
        retrieveSites()
    }
    fun getAddressFromLatLng(context: Context, latitude: Double, longitude: Double): String {
        val addresses: List<Address>
        val geocoder = Geocoder(context, Locale.getDefault())

        try {
            addresses = geocoder.getFromLocation(
                latitude,
                longitude,
                5
            )!!
            return if (addresses.isNotEmpty()) {
                val address =
                    addresses[0].getAddressLine(0)

                val city = addresses[0].locality?: ""
                val number= addresses[0].subThoroughfare?: ""
                val street = addresses[0].thoroughfare?:""
                "$city, $street, $number"
            } else {
                ""
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }
    }
}