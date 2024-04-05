package com.enrech.mondly.db.data.repository

import com.enrech.mondly.db.domain.MondlyDb
import com.enrech.mondly.db.domain.repository.DbRepository
import javax.inject.Inject

class DbRepositoryImpl @Inject constructor(
    private val db: MondlyDb
): DbRepository {
    override fun getDb(): MondlyDb = db
}