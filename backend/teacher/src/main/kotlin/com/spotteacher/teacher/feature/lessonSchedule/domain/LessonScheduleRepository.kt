package com.spotteacher.teacher.feature.lessonSchedule.domain

import com.spotteacher.domain.Pagination
import com.spotteacher.teacher.feature.school.domain.SchoolId
import com.spotteacher.teacher.feature.teacher.domain.TeacherId

interface LessonScheduleRepository {
    suspend fun create(lessonSchedule: DoingLessonSchedule): DoingLessonSchedule
    suspend fun findById(id: LessonScheduleId): LessonSchedule?
//    suspend fun filterByTeacherId(teacherId: TeacherId,pagination: Pagination<LessonSchedule>): List<LessonSchedule>
//    suspend fun filterBySchoolId(schoolId: SchoolId,pagination:Pagination<LessonSchedule>): List<LessonSchedule>
//    suspend fun cancel(schedule: CanceledLessonSchedule)
//    suspend fun toFinishWithReport(schedule: FinishedLessonSchedule)
//    suspend fun findByFinishedId(reportId: LessonScheduleId): FinishedLessonSchedule
}