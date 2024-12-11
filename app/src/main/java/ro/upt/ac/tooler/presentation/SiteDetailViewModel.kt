package ro.upt.ac.tooler.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ro.upt.ac.tooler.domain.Site
import ro.upt.ac.tooler.domain.SiteRepository
import ro.upt.ac.tooler.domain.Tool
import ro.upt.ac.tooler.domain.ToolRepository
import java.util.Calendar
import java.util.Date

class SiteDetailViewModel (private val siteRepository: SiteRepository, private val toolRepository: ToolRepository) : ViewModel() {

    fun getSiteById(id: Int) : Site?{
        return siteRepository.getSiteById(id)
    }


    fun updateTools(siteId: Int, newTools: List<Int>){
        for(tool in newTools){
            val newTool = toolRepository.getToolById(tool)
            if (newTool != null) {
                newTool.siteId = siteId
                newTool.startDate = Date()
                val calendar = Calendar.getInstance()
                calendar.time = newTool.startDate!!
                calendar.add(Calendar.MONTH, 3)
                newTool.endDate = calendar.time
            }
            if (newTool != null) {
                toolRepository.updateTool(newTool)

            }
        }
    }

    fun getToolsForSite(siteId: Int): List<Int>{
        val siteWithTools = siteRepository.getSiteWithTools(siteId)
        return siteWithTools.tools.map { tool -> tool.id }
    }

    fun removeTool(tool: Tool){
        tool.siteId = null
        tool.startDate = null
        tool.endDate = null
        toolRepository.updateTool(tool)
    }
}