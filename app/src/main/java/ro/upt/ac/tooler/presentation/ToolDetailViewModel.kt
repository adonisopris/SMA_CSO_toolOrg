package ro.upt.ac.tooler.presentation

import androidx.lifecycle.ViewModel
import ro.upt.ac.tooler.domain.Tool
import ro.upt.ac.tooler.domain.ToolRepository

class ToolDetailViewModel (private val toolRepository: ToolRepository) : ViewModel() {
    fun getToolById(id: Int) : Tool?{
        return toolRepository.getToolById(id)
    }

}