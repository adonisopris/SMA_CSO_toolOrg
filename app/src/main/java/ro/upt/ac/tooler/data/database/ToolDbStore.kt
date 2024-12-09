package ro.upt.ac.tooler.data.database

import ro.upt.ac.tooler.domain.Tool
import ro.upt.ac.tooler.domain.ToolRepository


class ToolDbStore (private val appDatabase: AppDatabase) : ToolRepository {
    override fun getTools(): List<Tool> {
        return appDatabase.toolDao().getAll().map { it.toDomainModel() }
    }

    override fun addTool(tool: Tool) {
        appDatabase.toolDao().add(tool.toDbModel())
    }

    override fun removeTool(tool : Tool) {
        appDatabase.toolDao().delete(tool.toDbModel())
    }
    override  fun getToolById(id: Int): Tool? {
        return appDatabase.toolDao().getToolById(id)?.toDomainModel()
    }


    private fun Tool.toDbModel() = ToolEntity(id,name,type,image,available, details)

    private fun ToolEntity.toDomainModel() = Tool(id,name,type,image,available,details)
}