package kinyua.vivian.domain.repository

import kinyua.vivian.domain.model.MissionReport

interface ReportRepository {
    suspend fun generateTextReport(report: MissionReport): String

    suspend fun generateJsonReport(report: MissionReport): String
}