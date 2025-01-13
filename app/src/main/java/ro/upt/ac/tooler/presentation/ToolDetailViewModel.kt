package ro.upt.ac.tooler.presentation

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ro.upt.ac.tooler.domain.Site
import ro.upt.ac.tooler.domain.SiteRepository
import ro.upt.ac.tooler.domain.Tool
import ro.upt.ac.tooler.domain.ToolRepository
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ToolDetailViewModel(
    private val toolRepository: ToolRepository,
    private val siteRepository: SiteRepository
) : ViewModel() {
    private val _sitesListState = MutableStateFlow(siteRepository.getSites())
    val sitesListState: StateFlow<List<Site>> = _sitesListState.asStateFlow()

    fun retrieveSites() {
        _sitesListState.value = siteRepository.getSites()
    }

    fun getToolById(id: Int): Tool? {
        return toolRepository.getToolById(id)
    }

    fun getSiteOfTool(tool: Tool): Site? {
        return toolRepository.getSiteOfTool(tool)
    }

    fun removeTool(tool: Tool) {
        tool.siteId = null
        tool.startDate = null
        tool.endDate = null
        toolRepository.updateTool(tool)
    }

    fun updateTool(siteId: Int, tool: Tool) {
        tool.siteId = siteId
        tool.startDate = Date()
        val calendar = Calendar.getInstance()
        calendar.time = tool.startDate!!
        calendar.add(Calendar.MONTH, 3)
        tool.endDate = calendar.time
        toolRepository.updateTool(tool)
    }

    fun checkBetterSuggestion(tool: Tool, selectedSite: Site): Tool? {
        val tools = toolRepository.getTools()
        val results = FloatArray(3)
        var betterTool: Tool? = null

        val siteOfTool = tool.siteId?.let { siteRepository.getSiteById(it) }!!
        Location.distanceBetween(
            selectedSite.latitude,
            selectedSite.longitude,
            siteOfTool.latitude,
            siteOfTool.longitude,
            results
        )

        val distanceToSelectedLocation = results[0]

        tools.filter { it.type == tool.type && it.siteId != null }.forEach { t ->
            val site = siteRepository.getSiteById(t.siteId!!)
            site?.let {
                Location.distanceBetween(
                    selectedSite.latitude,
                    selectedSite.longitude,
                    it.latitude,
                    it.longitude,
                    results
                )

                val distanceToCurrentTool = results[0]
                if (distanceToCurrentTool < distanceToSelectedLocation && site.id != selectedSite.id && t.id!= tool.id) {
                    betterTool = t
                }
            }
        }
        return betterTool
    }
    fun getAddressFromLatLng(context: Context, latitude: Double, longitude: Double): Address? {
        val addresses: List<Address>
        val geocoder = Geocoder(context, Locale.getDefault())

        try {
            addresses = geocoder.getFromLocation(
                latitude,
                longitude,
                5
            )!!
            return if (addresses.isNotEmpty()) {
                /*val address =
                    addresses[0].getAddressLine(0)

                val city = addresses[0].locality
                val state = addresses[0].adminArea
                val country = addresses[0].countryName
                val postalCode = addresses[0].postalCode
                address*/
                addresses[0]
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    /*fun checkBetterSuggestion(selectedTools: List<Int>, currentSite: Site): List<Int> {
        val tools = toolRepository.getTools()
        val results = FloatArray(3)
        val betterTools = mutableListOf<Int>()

        selectedTools.forEach { st ->
            val toolST = getToolById(st)
            val siteST = siteRepository.getSiteById(toolST?.siteId!!)!!
            Location.distanceBetween(
                currentSite.latitude,
                currentSite.longitude,
                siteST.latitude,
                siteST.longitude,
                results
            )

            val distanceStToCurrentLocation = results[0]

            tools.filter { it.type == toolST.type }.forEach { tool ->
                val site = getSiteById(tool.siteId!!)
                site?.let {
                    Location.distanceBetween(
                        currentSite.latitude,
                        currentSite.longitude,
                        it.latitude,
                        it.longitude,
                        results
                    )

                    val distanceToCurrentLocation = results[0]
                    if (distanceToCurrentLocation < distanceStToCurrentLocation && toolST.id != it.id) {
                        betterTools.add(tool.id)
                    }
                }
            }
        }
        return betterTools
    }*/

}