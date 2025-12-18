package com.stdio.it_link_testapp.data.repository

import com.stdio.it_link_testapp.data.local.ResourceManager
import com.stdio.it_link_testapp.domain.repository.ResourcesRepository
import javax.inject.Inject

class ResourcesRepositoryImpl @Inject constructor(private val resourceManager: ResourceManager) :
    ResourcesRepository {
    override fun getString(resId: Int) = resourceManager.getString(resId)
}