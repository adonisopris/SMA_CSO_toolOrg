package ro.upt.ac.tooler.domain

interface SiteTypeRepository {
    fun getSiteTypes() : List<SiteType>
    fun addSiteType(siteType: SiteType)
    fun removeSiteType(siteType: SiteType)
    fun deleteAll()
}