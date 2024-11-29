package ro.upt.ac.tooler.domain


interface ToolRepository {
    fun getTools() : List<Tool>
    fun addTool(tool: Tool)
    fun removeTool(tool: Tool)
    fun getToolById(id: Int): Tool?
}