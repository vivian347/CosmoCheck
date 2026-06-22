package kinyua.vivian.domain.usecase

import kinyua.vivian.domain.model.MissionReport
import kinyua.vivian.domain.repository.ReportRepository
import javax.inject.Inject

class GenerateMissionLogUseCase @Inject constructor(
    private val repository: ReportRepository
) {
    suspend operator fun invoke(report: MissionReport, format: ReportFormat) : String =
        when(format) {
            ReportFormat.TEXT -> repository.generateTextReport(report)
            ReportFormat.JSON -> repository.generateJsonReport(report)
        }
}

enum class ReportFormat {
 TEXT, JSON
}
