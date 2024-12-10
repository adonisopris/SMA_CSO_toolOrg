package ro.upt.ac.tooler.domain

import java.util.Date

data class Tool(
    val id: Int = 0,
    val name: String,
    val type: String,
    val image: String,
    val details: String = "",
    var siteId: Int? = null,
    var startDate: Date? = null,
    var endDate: Date? = null
)