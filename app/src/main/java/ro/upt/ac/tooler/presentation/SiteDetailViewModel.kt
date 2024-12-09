package ro.upt.ac.tooler.presentation

import androidx.lifecycle.ViewModel
import ro.upt.ac.tooler.domain.Site
import ro.upt.ac.tooler.domain.SiteRepository

class SiteDetailViewModel (private val siteRepository: SiteRepository) : ViewModel() {
    fun getSiteById(id: Int) : Site?{
        return siteRepository.getSiteById(id)
    }

}