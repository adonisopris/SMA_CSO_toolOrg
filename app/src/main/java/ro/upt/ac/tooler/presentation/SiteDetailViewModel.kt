package ro.upt.ac.tooler.presentation

import androidx.lifecycle.ViewModel
import ro.upt.ac.tooler.data.database.Converters
import ro.upt.ac.tooler.domain.Site
import ro.upt.ac.tooler.domain.SiteRepository
import ro.upt.ac.tooler.domain.Tool
import ro.upt.ac.tooler.domain.ToolRepository

class SiteDetailViewModel (private val siteRepository: SiteRepository) : ViewModel() {
    fun getSiteById(id: Int) : Site?{
        return siteRepository.getSiteById(id)
    }

    fun updateTools(siteId: Int, newTools: List<Int>){
        siteRepository.updateTools(siteId,Converters().convertToolListToJSONString(newTools))
    }
}