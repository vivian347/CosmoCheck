package kinyua.vivian.domain.repository

import kinyua.vivian.domain.model.MissionReport
import kotlinx.coroutines.flow.Flow

interface MissionHistoryRepository {
    suspend fun save(report: MissionReport)

    fun observeAll(): Flow<List<MissionReport>>

    suspend fun getById(id: String): MissionReport?

    suspend fun deleteById(id: String)

    suspend fun deleteAll()
}