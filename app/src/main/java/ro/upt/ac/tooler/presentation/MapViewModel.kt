package ro.upt.ac.tooler.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ro.upt.ac.tooler.domain.Site
import ro.upt.ac.tooler.domain.SiteRepository

class MapViewModel(private val siteRepository: SiteRepository): ViewModel(){
    private val _siteListState =  MutableStateFlow(siteRepository.getSites())
    val siteListState: StateFlow<List<Site>> = _siteListState.asStateFlow()

    fun retrieveTools() {
        _siteListState.value = siteRepository.getSites()
    }
}