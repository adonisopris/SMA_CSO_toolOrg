package ro.upt.ac.tooler.presentation

import android.location.Location
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ro.upt.ac.tooler.domain.Site
import ro.upt.ac.tooler.domain.SiteRepository

class SitesViewModel(private val siteRepository: SiteRepository): ViewModel() {
    private val _sitesListState =  MutableStateFlow(siteRepository.getSites())
    val sitesListState: StateFlow<List<Site>> = _sitesListState.asStateFlow()

    private fun retrieveSites() {
        _sitesListState.value = siteRepository.getSites()
    }

    fun addSite(id: Int, name: String, type:String) {
        siteRepository.addSite(Site(id,name,type))
        retrieveSites()
    }

    fun removeSite(site : Site) {
        siteRepository.removeSite(site)
        retrieveSites()
    }
}