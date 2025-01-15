package ro.upt.ac.tooler.presentation

import android.content.Context
import android.location.Address
import android.location.Geocoder
import androidx.lifecycle.ViewModel
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



}