package kinyua.vivian.domain.usecase

import kinyua.vivian.domain.model.MissionReport
import kinyua.vivian.domain.repository.MissionHistoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMissionHistoryUseCase @Inject constructor(
    private val repository: MissionHistoryRepository
) {
    suspend operator fun invoke() : Flow<List<MissionReport>> = repository.observeAll()
}