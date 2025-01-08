package ro.upt.ac.tooler.presentation

import androidx.lifecycle.ViewModel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import ro.upt.ac.tooler.domain.Site
import ro.upt.ac.tooler.domain.SiteRepository
import ro.upt.ac.tooler.domain.SiteType
import ro.upt.ac.tooler.domain.SiteTypeRepository

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
}