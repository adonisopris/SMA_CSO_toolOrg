package ro.upt.ac.tooler.presentation

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ro.upt.ac.tooler.data.database.ToolEntity
import ro.upt.ac.tooler.domain.Tool
import ro.upt.ac.tooler.domain.ToolRepository

class FleetViewModel(private val toolRepository: ToolRepository) : ViewModel() {
    private val _fleetListState =  MutableStateFlow(toolRepository.getTools())
    val fleetListState: StateFlow<List<Tool>> = _fleetListState.asStateFlow()

    fun retrieveTools() {
        _fleetListState.value = toolRepository.getTools()
    }

    fun addTool(name: String, type: String, image: Uri) {
        toolRepository.addTool(Tool(name = name,type = type, image = image.toString()))
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

