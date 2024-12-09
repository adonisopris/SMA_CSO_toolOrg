package ro.upt.ac.tooler.presentation

import android.net.Uri
import androidx.lifecycle.ViewModel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ro.upt.ac.tooler.domain.Site
import ro.upt.ac.tooler.domain.SiteRepository
import ro.upt.ac.tooler.domain.Tool

class SitesViewModel(private val siteRepository: SiteRepository): ViewModel() {
    private val _sitesListState =  MutableStateFlow(siteRepository.getSites())
    val sitesListState: StateFlow<List<Site>> = _sitesListState.asStateFlow()

    private fun retrieveSites() {
        _sitesListState.value = siteRepository.getSites()
    }

    fun addSite(name: String, type: String, details: String, latitude: Double, longitude: Double) {
        siteRepository.addSite(Site(name = name, type= type,details = details,latitude = latitude, longitude = longitude))
        retrieveSites()
    }

    fun removeSite(site : Site) {
        siteRepository.removeSite(site)
        retrieveSites()
    }
}