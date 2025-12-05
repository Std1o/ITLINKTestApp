package com.stdio.it_link_testapp.domain.model

sealed interface ImageData<out R> {
    data object Placeholder : ImageData<Nothing>
}