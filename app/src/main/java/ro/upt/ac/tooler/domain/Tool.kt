package ro.upt.ac.tooler.domain

data class Tool(
    val id: Int = 0,
    val name: String,
    val type: String,
    val image: String,
    val available: Boolean = true,
    val details: String = ""
)