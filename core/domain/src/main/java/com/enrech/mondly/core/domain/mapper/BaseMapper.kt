package com.enrech.mondly.core.domain.mapper

abstract class BaseMapper<in From, To> {

    abstract fun mapFrom(from: From): To

    fun mapFromList(from: List<From?>?) = from?.filterNotNull()?.map { mapFrom(it) } ?: emptyList()
}