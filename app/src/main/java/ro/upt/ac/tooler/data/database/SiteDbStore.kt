package ro.upt.ac.tooler.data.database

import ro.upt.ac.tooler.domain.Site
import ro.upt.ac.tooler.domain.SiteRepository


class SiteDbStore (private val appDatabase: AppDatabase) : SiteRepository {
    override fun getSites(): List<Site> {
        return appDatabase.siteDao().getAll().map { it.toDomainModel() }
    }

    override fun addSite(site: Site) {
        appDatabase.siteDao().add(site.toDbModel())
    }

    override fun removeSite(site: Site) {
        appDatabase.siteDao().delete(site.toDbModel())
    }
    override  fun getSiteById(id: Int): Site? {
        return appDatabase.siteDao().getSiteById(id)?.toDomainModel()
    }

    override fun getSiteWithTools(siteId: Int): SiteWithTools {
        return appDatabase.siteDao().getSiteWithTools(siteId)
    }

    override fun getAllSitesWithTools(): List<SiteWithTools> {
        return appDatabase.siteDao().getAllSitesWithTools()
    }


    private fun Site.toDbModel() = SiteEntity(id,name,type,latitude,longitude,details)

    private fun SiteEntity.toDomainModel() = Site(id,name,type,latitude,longitude,details)
}