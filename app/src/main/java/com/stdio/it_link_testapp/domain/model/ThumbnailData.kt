package com.stdio.it_link_testapp.domain.model

sealed interface ThumbnailData<out R> {
    data object Placeholder : ThumbnailData<Nothing>
}