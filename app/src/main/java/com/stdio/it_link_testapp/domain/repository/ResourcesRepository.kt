package com.stdio.it_link_testapp.domain.repository

interface ResourcesRepository {
    fun getString(resId: Int): String
}