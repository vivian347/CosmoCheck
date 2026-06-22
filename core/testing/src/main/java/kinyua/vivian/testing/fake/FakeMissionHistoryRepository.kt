package kinyua.vivian.testing.fake

import kinyua.vivian.domain.model.MissionReport
import kinyua.vivian.domain.repository.MissionHistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class FakeMissionHistoryRepository: MissionHistoryRepository {
    private val _reports = MutableStateFlow<List<MissionReport>>(emptyList())

    val reports: List<MissionReport> get() = _reports.value

    var saveCallCount: Int = 0
        private set

    var deleteAllCallCount: Int = 0
        private set

    fun addReport(report: MissionReport) {
        _reports.update { current -> listOf(report) + current }
    }
    
    fun addReports(vararg reports: MissionReport) {
        reports.forEach { addReport(it) }
    }

    override suspend fun save(report: MissionReport) {
        TODO("Not yet implemented")
    }

    override fun observeAll(): Flow<List<MissionReport>> {
        TODO("Not yet implemented")
    }

    override suspend fun getById(id: String): MissionReport? {
        TODO("Not yet implemented")
    }

    override suspend fun deleteById(id: String) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAll() {
        TODO("Not yet implemented")
    }

}