package com.spotteacher.admin.feature.lessonSchedule.domain

import com.spotteacher.domain.Pagination


interface LessonScheduleRepository {
    suspend fun create(lessonSchedule: DoingLessonSchedule): DoingLessonSchedule
    suspend fun findById(id: LessonScheduleId): LessonSchedule?
//    suspend fun filterByTeacherId(teacherId: TeacherId,pagination: Pagination<LessonSchedule>): List<LessonSchedule>
//    suspend fun filterBySchoolId(schoolId: SchoolId,pagination:Pagination<LessonSchedule>): List<LessonSchedule>
//    suspend fun cancel(schedule: CanceledLessonSchedule)
//    suspend fun findByFinishedId(reportId: LessonScheduleId): FinishedLessonSchedule
}