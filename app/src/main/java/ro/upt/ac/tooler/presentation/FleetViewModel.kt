package ro.upt.ac.tooler.presentation

import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ro.upt.ac.tooler.domain.Tool
import ro.upt.ac.tooler.domain.ToolRepository

class FleetViewModel(private val toolRepository: ToolRepository) : ViewModel() {
    private val _fleetListState =  MutableStateFlow(toolRepository.getTools())
    val fleetListState: StateFlow<List<Tool>> = _fleetListState.asStateFlow()

    private fun retrieveTools() {
        _fleetListState.value = toolRepository.getTools()
    }

    fun addTool(name: String, type: String, image: Uri) {
        toolRepository.addTool(Tool(name = name,type = type, image = image.toString()))
        retrieveTools()
    }

    fun removeTool(tool : Tool) {
        toolRepository.removeTool(tool)
        retrieveTools()
    }
}