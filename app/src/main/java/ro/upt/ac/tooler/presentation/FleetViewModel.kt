package ro.upt.ac.tooler.presentation

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ro.upt.ac.tooler.domain.Tool
import ro.upt.ac.tooler.domain.ToolRepository

class FleetViewModel(private val toolRepository: ToolRepository) : ViewModel() {
    private val _fleetListState =  MutableStateFlow(toolRepository.getTools())
    val fleetListState: StateFlow<List<Tool>> = _fleetListState.asStateFlow()

    fun retrieveTools() {
        _fleetListState.value = toolRepository.getTools()
    }

    fun addTool(name: String, type: String, details:String, image: Uri) {
        toolRepository.addTool(Tool(name = name,type = type, details = details, image = image.toString()))
        retrieveTools()
    }

    fun getToolById(id: Int) : Tool? {
        return toolRepository.getToolById(id)
    }


    fun removeTool(tool : Tool) {
        deleteImage(tool.image)
        toolRepository.removeTool(tool)
        retrieveTools()
    }
    private fun deleteImage(imagePath: String) {
        try {
            val file = java.io.File(imagePath)
            if (file.exists()) {
                if (file.delete()) {
                    println("Image deleted successfully")
                } else {
                    println("Failed to delete image")
                }
            } else {
                println("Image file not found")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}

