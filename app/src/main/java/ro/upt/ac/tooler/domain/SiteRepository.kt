package ro.upt.ac.tooler.domain

import ro.upt.ac.tooler.domain.Site

interface SiteRepository{
    fun getSites() : List<Site>
    fun addSite(site: Site)
    fun removeSite(site: Site)
    fun getSiteById(id: Int): Site?
}