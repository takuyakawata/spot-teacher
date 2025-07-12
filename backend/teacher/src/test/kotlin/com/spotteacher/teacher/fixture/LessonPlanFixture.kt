package com.spotteacher.teacher.fixture

import arrow.core.nonEmptyListOf
import com.spotteacher.teacher.feature.company.domain.CompanyId
import com.spotteacher.teacher.feature.lessonPlan.domain.LessonLocation
import com.spotteacher.teacher.feature.lessonPlan.domain.LessonPlan
import com.spotteacher.teacher.feature.lessonPlan.domain.LessonPlanDate
import com.spotteacher.teacher.feature.lessonPlan.domain.LessonPlanDescription
import com.spotteacher.teacher.feature.lessonPlan.domain.LessonPlanEducations
import com.spotteacher.teacher.feature.lessonPlan.domain.LessonPlanGrades
import com.spotteacher.teacher.feature.lessonPlan.domain.LessonPlanId
import com.spotteacher.teacher.feature.lessonPlan.domain.LessonPlanSubjects
import com.spotteacher.teacher.feature.lessonPlan.domain.LessonPlanTitle
import com.spotteacher.teacher.feature.lessonPlan.domain.LessonType
import com.spotteacher.teacher.feature.lessonTag.domain.EducationId
import com.spotteacher.teacher.feature.lessonTag.domain.Grade
import com.spotteacher.teacher.feature.lessonTag.domain.Subject
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.LocalTime

@Component
class LessonPlanFixture {

    private var lessonPlanIdCount = 1L

    fun buildLessonPlan(
        id: LessonPlanId = LessonPlanId(lessonPlanIdCount++),
        companyId: CompanyId = CompanyId(1L),
        title: String = "Test Lesson Plan",
        description: String = "Test Description",
        lessonType: LessonType = LessonType.ONLINE,
        location: String = "Test Location",
        annualMaxExecutions: Int = 10,
        educations: Set<EducationId> = setOf(EducationId(1L)),
        subjects: Set<Subject> = setOf(Subject.JAPANESE),
        grades: Set<Grade> = setOf(Grade.ELEMENTARY_1),
        createdAt: LocalDateTime = LocalDateTime.now()
    ): LessonPlan {
        val lessonPlanDate = LessonPlanDate(
            startMonth = 1,
            startDay = 1,
            endMonth = 12,
            endDay = 31,
            startTime = LocalTime.of(9, 0),
            endTime = LocalTime.of(17, 0)
        )

        return LessonPlan(
            id = id,
            companyId = companyId,
            images = emptyList(),
            createdAt = createdAt,
            educations = LessonPlanEducations(educations),
            subjects = LessonPlanSubjects(subjects),
            grades = LessonPlanGrades(grades),
            title = LessonPlanTitle(title),
            description = LessonPlanDescription(description),
            lessonType = lessonType,
            location = LessonLocation(location),
            annualMaxExecutions = annualMaxExecutions,
            lessonPlanDates = nonEmptyListOf(lessonPlanDate)
        )
    }
}