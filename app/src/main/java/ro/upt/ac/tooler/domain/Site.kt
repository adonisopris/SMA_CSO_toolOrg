package ro.upt.ac.tooler.domain

data class Site(
    val id: Int = 0,
    val name: String,
    val type: String,
    val latitude: Double,
    val longitude: Double,
    val details: String = "",
    var tools: List<Int>? = null
)