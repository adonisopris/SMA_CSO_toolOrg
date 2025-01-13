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

class SiteDetailViewModel (private val siteRepository: SiteRepository, private val toolRepository: ToolRepository) : ViewModel() {

    fun getSiteById(id: Int) : Site?{
        return siteRepository.getSiteById(id)
    }
    private fun getToolById(id: Int) : Tool? {
        return toolRepository.getToolById(id)
    }


    fun updateTools(siteId: Int, newTools: List<Int>){
        for(tool in newTools){
            val newTool = toolRepository.getToolById(tool)
            if (newTool != null) {
                newTool.siteId = siteId
                newTool.startDate = Date()
                val calendar = Calendar.getInstance()
                calendar.time = newTool.startDate!!
                calendar.add(Calendar.MONTH, 3)
                newTool.endDate = calendar.time
            }
            if (newTool != null) {
                toolRepository.updateTool(newTool)

            }
        }
    }

    fun getToolsForSite(siteId: Int): List<Int>{
        val siteWithTools = siteRepository.getSiteWithTools(siteId)
        return siteWithTools.tools.map { tool -> tool.id }
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

    fun removeTool(tool: Tool){
        tool.siteId = null
        tool.startDate = null
        tool.endDate = null
        toolRepository.updateTool(tool)
    }
    fun checkBetterSuggestion(selectedTools: List<Int>, currentSite: Site): List<Int> {
        val tools = toolRepository.getTools()
        val results = FloatArray(3)
        val betterTools = mutableListOf<Int>()

        selectedTools.forEach { st ->
            val toolST = getToolById(st)
            val siteST = getSiteById(toolST?.siteId!!)!!
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
    }


}