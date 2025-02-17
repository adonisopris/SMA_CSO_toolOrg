package ro.upt.ac.tooler.data.database

import ro.upt.ac.tooler.domain.SiteType
import ro.upt.ac.tooler.domain.SiteTypeRepository

class SiteTypeDbStore(private val appDatabase: AppDatabase) : SiteTypeRepository {
    override fun getSiteTypes(): List<SiteType> {
        return appDatabase.siteTypeDao().getAll().map { it.toDomainModel() }
    }

    override fun addSiteType(siteType: SiteType) {
        appDatabase.siteTypeDao().add(siteType.toDbModel())
    }

    override fun removeSiteType(siteType: SiteType) {
        appDatabase.siteTypeDao().delete(siteType.toDbModel())
    }

    override fun deleteAll() {
        appDatabase.siteTypeDao().deleteAll()
    }
    private fun SiteType.toDbModel() = SiteTypeEntity(id,name)

    private fun SiteTypeEntity.toDomainModel() = SiteType(id,name)

}