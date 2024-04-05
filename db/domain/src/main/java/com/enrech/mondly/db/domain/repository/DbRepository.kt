package com.enrech.mondly.db.domain.repository

import com.enrech.mondly.db.domain.MondlyDb

interface DbRepository {
    fun getDb(): MondlyDb
}