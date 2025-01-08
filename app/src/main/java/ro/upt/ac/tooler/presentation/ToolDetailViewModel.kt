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

class ToolDetailViewModel (private val toolRepository: ToolRepository, private val siteRepository: SiteRepository) : ViewModel() {
    private val _sitesListState =  MutableStateFlow(siteRepository.getSites())
    val sitesListState: StateFlow<List<Site>> = _sitesListState.asStateFlow()

    fun retrieveSites() {
        _sitesListState.value = siteRepository.getSites()
    }

    fun getToolById(id: Int) : Tool?{
        return toolRepository.getToolById(id)
    }
    fun getSiteOfTool(tool: Tool) : Site?{
        return toolRepository.getSiteOfTool(tool)
    }
    fun removeTool(tool: Tool){
        tool.siteId = null
        tool.startDate = null
        tool.endDate = null
        toolRepository.updateTool(tool)
    }
    fun updateTool(siteId: Int, tool: Tool){
        tool.siteId = siteId
        tool.startDate = Date()
        val calendar = Calendar.getInstance()
        calendar.time = tool.startDate!!
        calendar.add(Calendar.MONTH, 3)
        tool.endDate = calendar.time
        toolRepository.updateTool(tool)
    }

}