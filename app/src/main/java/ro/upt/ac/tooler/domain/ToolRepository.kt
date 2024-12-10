package ro.upt.ac.tooler.domain

import ro.upt.ac.tooler.data.database.ToolEntity


interface ToolRepository {
    fun getTools() : List<Tool>
    fun addTool(tool: Tool)
    fun removeTool(tool: Tool)
    fun getToolById(id: Int): Tool?
    fun updateTool(tool: Tool)
    fun getSiteOfTool(tool: Tool) : Site?
}