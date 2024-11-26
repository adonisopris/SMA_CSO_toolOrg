package ro.upt.ac.tooler.domain


interface ToolRepository {
    fun getTools() : List<Tool>
    fun addTool(tool: Tool)
    fun removeTool(tool: Tool)
}